package com.tweetcrawl.agents.behaviours;

import com.tweetcrawl.agents.Processor;
import com.tweetcrawl.agents.utils.BBPetterson;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.ontology.FileTwitter;
import com.tweetcrawl.ontology.Quote;
import com.tweetcrawl.ontology.QuotesAction;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import twitter4j.Status;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateBehaviour extends FSMBehaviour {
	private Logger logger;
	private Codec codec;
	private Ontology quoteOntology;
	private Ontology quoteActionOntology;

	private static String nomFichier;
	private File fileTweet;
	private boolean isEnd;
	private String from = "";
	private ArrayList<String> rtTo = new ArrayList<>();

	/**
	 * Define the differents states of agent - Reception - Wait - Demand - Access to
	 * critical section (read line)
	 */
	public StateBehaviour(Agent agent, Logger logger, Codec codec, Ontology quoteOntology,
			Ontology quoteActionOntology) {
		super(agent);
		this.logger = logger;
		this.codec = codec;
		this.quoteOntology = quoteOntology;
		this.quoteActionOntology = quoteActionOntology;

		this.registerFirstState(new OneShotBehaviour(myAgent) {
			boolean result;

			@Override
			public void action() {
				result = receptionMsg();
				if (result) {
					fileTweet = new File("./data/tweets_" + nomFichier + ".txt");
					envoieMsgStartGraph();
					envoieMsgStartCloud();
					isEnd = false;
				}
			}

			@Override
			public int onEnd() {
				return result ? 1 : 0;
			}

		}, "receptionMessage");

		this.registerState(new OneShotBehaviour(myAgent) {
			@Override
			public void action() {
				//logger.info("Attente passive");
			}

			@Override
			public int onEnd() {
				BBPetterson.tour = (((Processor) myAgent).id % BBPetterson.demandes.length) + 1;
				BBPetterson.demandes[((Processor) myAgent).id - 1] = true;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.severe(e.getMessage());
				}
				return 1;
			}
		}, "attente");

		this.registerState(new OneShotBehaviour(myAgent) {
			@Override
			public void action() {
				//BBPetterson.afficheDemande();
			}

			@Override
			public int onEnd() {
				return ((BBPetterson.tour == (((Processor) myAgent).id) || noDemande()) ? 1 : 0);
			}
		}, "demande");

		this.registerState(new OneShotBehaviour(myAgent) {
			boolean line;

			@Override
			public void action() {
				if (fileTweet.exists()) {
					line = getandremoveline();
					if (line) {
						for (String to : rtTo) {
							envoieMsgGraph(from, to);
						}
						rtTo.clear();
						envoieMsgCloud();
						isEnd = false;
					} else {
						isEnd = fileTweet.delete();
						envoieMsgEndGraph();
					}
				} else {
					envoieMsgEndGraph();
					envoieMsgEndCloud();
					isEnd = true;
				}
			}

			@Override
			public int onEnd() {
				BBPetterson.demandes[((Processor) myAgent).id - 1] = false;
				return isEnd ? 1 : 0;
			}
		}, "lectureFichier");

		this.registerTransition("receptionMessage", "receptionMessage", 0);
		this.registerTransition("receptionMessage", "attente", 1);
		this.registerTransition("attente", "demande", 1);
		this.registerTransition("demande", "demande", 0);
		this.registerTransition("demande", "lectureFichier", 1);
		this.registerTransition("lectureFichier", "attente", 0);
		this.registerTransition("lectureFichier", "receptionMessage", 1);

	}// end of constructor

	// -------------- Messages for the Graph Agent - Start - Process - End

	/**
	 * Message to begin the treatment of GraphAgent, contains the Action
	 * (QuoteAction :term filename :action begin)
	 */
	private void envoieMsgStartGraph() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, "QuoteGraphGenerator-service")[0].getName());
		msg.setLanguage(codec.getName());
		msg.setOntology(quoteActionOntology.getName());
		QuotesAction qa = new QuotesAction();
		qa.setTerm(nomFichier);
		qa.setAction("begin");
		try {
			myAgent.getContentManager().fillContent(msg, qa);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

	/**
	 * Message sent to the GraphAgent, contains the Predicate (Quote :term filename
	 * :original from :repeater to)
	 *
	 * @param from the sender of the tweet
	 * @param to   the receiver of the tweet
	 */
	private void envoieMsgGraph(String from, String to) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, "QuoteGraphGenerator-service")[0].getName());
		msg.setLanguage(codec.getName());
		msg.setOntology(quoteOntology.getName());
		Quote q = new Quote();
		q.setTerm(nomFichier);
		q.setOriginal(from);
		q.setRepeater(to);

		try {
			myAgent.getContentManager().fillContent(msg, q);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

	/**
	 * Message to end the treatment of GraphAgent, contains the Action (QuoteAction
	 * :term filename :action end)
	 */
	private void envoieMsgEndGraph() {
		System.out.println(myAgent.getName() + " is sending endGraphAction");
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, "QuoteGraphGenerator-service")[0].getName());
		msg.setLanguage(codec.getName());
		msg.setOntology(quoteActionOntology.getName());
		QuotesAction qa = new QuotesAction();
		qa.setTerm(nomFichier);
		qa.setAction("end");
		try {
			myAgent.getContentManager().fillContent(msg, qa);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

	// -------------- Messages for the Cloud Agent - Start - Process - End
	private void envoieMsgStartCloud() {
	}

	private void envoieMsgEndCloud() {
	}

	private void envoieMsgCloud() {
	}

	/**
	 * Allows to read the file, treat the line and remove it
	 *
	 * @return true if there is still a line, return false otherwise
	 */
	private boolean getandremoveline() {
		String recherche = "";
		Scanner scanner = null;
		try {
			scanner = new Scanner(fileTweet);
			if (scanner.hasNextLine()) {
				recherche = scanner.nextLine();
				System.out.println(recherche);
				findPatternLine(recherche);
			}
		} catch (FileNotFoundException e) {
			logger.severe(e.getMessage());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		if (recherche.length() != 0) {
			removeLine(recherche);
			return true;
		}
		return false;
	}

	/**
	 * Find the name of the tweet sender and the receiver retweeted
	 *
	 * @param recherche the String to analyse
	 */
	private void findPatternLine(String recherche) {
		final Pattern patternFrom = Pattern.compile("from:'(.*?)'");
		final Pattern patternRT = Pattern.compile("@[a-zA-Z0-9_]*");
		Matcher matcherFROM = patternFrom.matcher(recherche);
		Matcher matcherRT = patternRT.matcher(recherche);
		if (matcherFROM.find()) {
			from = matcherFROM.group(1);
		}
		while (matcherRT.find()) {
			rtTo.add(matcherRT.group());
		}
	}

	/**
	 * Allows to remove the line passed in parameter and recreate the File
	 *
	 * @param recherche the line to delete
	 */
	private void removeLine(String recherche) {
		StringBuffer sb = new StringBuffer("");
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileTweet), StandardCharsets.UTF_8))) {
			while((line = br.readLine()) != null) {
				if(!line.equals(recherche)) {
					sb.append(line + "\n");
				}
			}
			br.close();
			FileWriter writer = new FileWriter(fileTweet);
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			logger.severe("Exception while storing the tweets from TweetCrawlerAgent : " + e);
		}
	}

	/**
	 * Check if there is demand from agent
	 *
	 * @return true if there is a demande, return false otherwise
	 */
	private boolean noDemande() {
		for (int i = 0; i < BBPetterson.demandes.length; i++) {
			if (i != (((Processor) myAgent).id - 1) && BBPetterson.demandes[i] == false) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Allows the agent to retrieve message from the TweetCrawler
	 *
	 * @return true if the message was received, return false otherwise
	 */
	private boolean receptionMsg() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			ContentManager cm = myAgent.getContentManager();

			try {
				Predicate pre = (Predicate) cm.extractContent(msg);
				FileTwitter fl = (FileTwitter) pre;
				nomFichier = fl.getTerm();
				logger.info(nomFichier);
				return true;
			} catch (Codec.CodecException | OntologyException e) {
				logger.severe(e.getMessage());
			}
		}
		return false;
	}
}
