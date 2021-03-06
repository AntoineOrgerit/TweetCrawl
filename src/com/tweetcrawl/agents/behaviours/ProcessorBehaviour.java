package com.tweetcrawl.agents.behaviours;

import com.tweetcrawl.agents.utils.BBPetterson;
import com.tweetcrawl.agents.utils.BBPettersonException;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.Cloud;
import com.tweetcrawl.ontology.FileTwitter;
import com.tweetcrawl.ontology.Quote;
import com.tweetcrawl.ontology.ProcessorAction;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Behaviour of the {@code Processor} agents.
 */
public class ProcessorBehaviour extends FSMBehaviour {

	private static final long serialVersionUID = 1L;
	private int id;
	private TweetCrawlerLogger logger;
	private Codec codec;
	private Ontology quoteOntology;
	private Ontology cloudOntology;
	private Ontology processorActionOntology;
	private static final String QUOTE_GRAPH_GENERATOR_SERVICE = "QuoteGraphGenerator-service";
	private static final String CLOUD_GENERATOR_SERVICE = "CloudGenerator-service";

	private String nomFichier;
	private File fileTweet;
	private boolean isEnd;
	private String from = "";
	private String datetweet = "";
	private ArrayList<String> rtTo = new ArrayList<>();
	private ArrayList<String> hashTags = new ArrayList<>();

	private BBPetterson petterson;
	private static final String RECEPTION_MESSAGE = "receptionMessage";
	private static final String ATTENTE = "attente";
	private static final String DEMANDE = "demande";
	private static final String LECTURE_FICHIER = "lectureFichier";

	/**
	 * Constructor of the behaviour.
	 *
	 * @param agent                   the {@code Processor} agent using the
	 *                                behaviour
	 * @param logger                  the {@code TweetCrawlerLogger} used to display
	 *                                errors encountered
	 * @param codec                   the {@code Codec} used to communicate with the
	 *                                {@code QuoteGraphGenerator} and
	 *                                {@code unknown} agents
	 * @param quoteOntology           the {@code Ontology} used to send
	 *                                {@code Quote} messages to the
	 *                                {@code QuoteGraphGenerator} agent
	 * @param cloudOntology           the {@code Ontology} used to send
	 *                                {@code Cloud} messages to the
	 *                                {@code CloudGenerator} agent
	 * @param processorActionOntology the {@code Ontology} used to send
	 *                                {@code ProcessorAction} messages to the
	 *                                {@code QuoteGraphGenerator} agent
	 */
	public ProcessorBehaviour(Agent agent, TweetCrawlerLogger logger, Codec codec, Ontology quoteOntology,
			Ontology cloudOntology, Ontology processorActionOntology) {
		super(agent);
		this.id = Integer.parseInt(myAgent.getLocalName().split("_")[1]);
		this.logger = logger;
		this.codec = codec;
		this.quoteOntology = quoteOntology;
		this.cloudOntology = cloudOntology;
		this.processorActionOntology = processorActionOntology;
		try {
			this.petterson = BBPetterson.getInstance();
			this.registerFirstState(new PassiveWaiting(myAgent), RECEPTION_MESSAGE);
			this.registerState(new ActiveWaiting(myAgent), ATTENTE);
			this.registerState(new Request(myAgent), DEMANDE);
			this.registerState(new ReadingFile(myAgent), LECTURE_FICHIER);
			this.registerTransition(RECEPTION_MESSAGE, RECEPTION_MESSAGE, 0);
			this.registerTransition(RECEPTION_MESSAGE, ATTENTE, 1);
			this.registerTransition(ATTENTE, DEMANDE, 1);
			this.registerTransition(DEMANDE, DEMANDE, 0);
			this.registerTransition(DEMANDE, LECTURE_FICHIER, 1);
			this.registerTransition(LECTURE_FICHIER, ATTENTE, 0);
			this.registerTransition(LECTURE_FICHIER, RECEPTION_MESSAGE, 1);
		} catch (BBPettersonException e) {
			logger.severe("Exception during behaviour generation on Processor: " + e);
		}
	}

	/**
	 * Allows to send a {@code ProcessorAction} message to the
	 * {@code QuoteGraphGenerator} or {@code CloudGenerator} indicating that the
	 * {@code Processor} agent is either starting or ending its work on a term.
	 *
	 * @param service the service to which send the {@code ProcessorAction} message
	 * @param action  the action of the {@code Processor} agent
	 */
	private void sendMessageAction(String service, String action) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, service)[0].getName());
		msg.setLanguage(codec.getName());
		msg.setOntology(processorActionOntology.getName());
		ProcessorAction qa = new ProcessorAction();
		qa.setTerm(nomFichier);
		qa.setAction(action);
		try {
			myAgent.getContentManager().fillContent(msg, qa);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

	/**
	 * {@code ProcessorBehaviour} state in which the {@code Processor} agent is
	 * waiting for message from the {@code TweetCrawler} agent.
	 */
	private class PassiveWaiting extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;
		boolean result;

		/**
		 * Constructor of the behaviour state.
		 *
		 * @param agent the {@code Processor} agent using the behaviour
		 */
		public PassiveWaiting(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			result = receiveMessage();
			if (result) {
				fileTweet = new File("./data/tweets_" + nomFichier + ".txt");
				isEnd = false;
				sendMessageAction(QUOTE_GRAPH_GENERATOR_SERVICE, "begin");
				sendMessageAction(CLOUD_GENERATOR_SERVICE, "begin");
			}
		}

		@Override
		public int onEnd() {
			return result ? 1 : 0;
		}

		/**
		 * Allows to receive new messages from the {@code TweetCrawler} agent
		 *
		 * @return {@code true} if a message has been received, {@code false} otherwise
		 */
		private boolean receiveMessage() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				ContentManager cm = myAgent.getContentManager();
				try {
					Predicate pre = (Predicate) cm.extractContent(msg);
					FileTwitter fl = (FileTwitter) pre;
					nomFichier = fl.getTerm();
					return true;
				} catch (Codec.CodecException | OntologyException e) {
					logger.severe(e.getMessage());
				}
			}
			return false;
		}
	}

	/**
	 * {@code ProcessorBehaviour} state in which the {@code Processor} agent is
	 * actively waiting to access a file to treat.
	 */
	private class ActiveWaiting extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor of the behaviour state.
		 *
		 * @param agent the {@code Processor} agent using the behaviour
		 */
		public ActiveWaiting(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			// no action necessary
		}

		@Override
		public int onEnd() {
			petterson.setTour((id % petterson.getNbRequests()) + 1);
			petterson.setRequest(id, true);
			return 1;
		}
	}

	/**
	 * {@code ProcessorBehaviour} state in which the {@code Processor} agent is
	 * requesting the access to a file to treat.
	 */
	private class Request extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor of the behaviour state.
		 *
		 * @param agent the {@code Processor} agent using the behaviour
		 */
		public Request(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			// no action necessary
		}

		@Override
		public int onEnd() {
			return ((petterson.getTour() == id || noDemande()) ? 1 : 0);
		}

		/**
		 * Allows to verify if no other {@code Processor} agent is requesting the access
		 * of a file.
		 *
		 * @return {@code true} if the file can be accessed, {@code false} otherwise
		 */
		private boolean noDemande() {
			for (int i = 1; i <= petterson.getNbRequests(); i++) {
				if (i != id && !petterson.getRequest(i)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * {@code ProcessorBehaviour} state in which the {@code Processor} agent is
	 * reading and treating a line of a file.
	 */
	private class ReadingFile extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;
		boolean line;

		/**
		 * Constructor of the behaviour state.
		 *
		 * @param agent the {@code Processor} agent using the behaviour
		 */
		public ReadingFile(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			if (fileTweet.exists()) {
				line = getAndRemoveLine();
				if (line) {
					for (String to : rtTo) {
						this.sendMessageGraph(from, to);
					}
					for (String tag : hashTags) {
						this.sendMessageCloud(datetweet, tag);
					}
					rtTo.clear();
					hashTags.clear();
					isEnd = false;
				} else {
					try {
						Files.delete(fileTweet.toPath());
					} catch (IOException e) {
						logger.severe("Exception while closing the file " + fileTweet.getName() + ": " + e);
					}
					isEnd = true;
					sendMessageAction(QUOTE_GRAPH_GENERATOR_SERVICE, "end");
					sendMessageAction(CLOUD_GENERATOR_SERVICE, "end");
				}
			} else {
				sendMessageAction(QUOTE_GRAPH_GENERATOR_SERVICE, "end");
				sendMessageAction(CLOUD_GENERATOR_SERVICE, "end");
				isEnd = true;
			}
		}

		@Override
		public int onEnd() {
			petterson.setRequest(id, false);
			return isEnd ? 1 : 0;
		}

		/**
		 * Allows to obtain and remove the first of a file.
		 *
		 * @return {@code true} if the function is successfull, {@code false} otherwise
		 */
		private boolean getAndRemoveLine() {
			String recherche = "";
			try (Scanner scanner = new Scanner(fileTweet)) {
				if (scanner.hasNextLine()) {
					recherche = scanner.nextLine();
					findPatternLineQuote(recherche);
					findPatternLineCloud(recherche);
				}
			} catch (FileNotFoundException e) {
				logger.severe("Exception while trying to read the file " + fileTweet.getName() + ": " + e.getMessage());
			}
			if (recherche.length() != 0) {
				removeLine(recherche);
				return true;
			}
			return false;
		}

		/**
		 * Allows to find user tags in a tweet in a line of a file.
		 *
		 * @param recherche the line to search in
		 */
		private void findPatternLineQuote(String recherche) {
			Pattern patternFrom = Pattern.compile("from:'(.*?)'");
			Pattern patternRT = Pattern.compile("@[a-zA-Z0-9_]*");
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
		 * Allows to find date and tags in a tweet in a line of a file.
		 *
		 * @param recherche the line to search in
		 */
		private void findPatternLineCloud(String recherche) {
			Pattern patternDatetime = Pattern.compile("datetime:'(.*?)'");
			Pattern patternHashtag = Pattern.compile("#[a-zA-Z0-9_]*");
			Matcher matcherDatetime = patternDatetime.matcher(recherche);
			Matcher matcherHashtag = patternHashtag.matcher(recherche);
			if (matcherDatetime.find()) {
				datetweet = matcherDatetime.group(1);
			}
			while (matcherHashtag.find()) {
				hashTags.add(matcherHashtag.group());
			}
		}

		/**
		 * Allows to remove a line of a file.
		 *
		 * @param recherche the line to remove
		 */
		private void removeLine(String recherche) {
			StringBuilder sb = new StringBuilder("");
			String currentReadingLine;
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileTweet), StandardCharsets.UTF_8))) {
				while ((currentReadingLine = br.readLine()) != null) {
					if (!currentReadingLine.equals(recherche)) {
						sb.append(currentReadingLine + "\n");
					}
				}
			} catch (IOException e) {
				logger.severe("Exception while removing line on " + myAgent.getName() + ": " + e);
			}
			try (FileWriter writer = new FileWriter(fileTweet)) {
				writer.write(sb.toString());
			} catch (IOException e) {
				logger.severe("Exception while removing line on " + myAgent.getName() + ": " + e);
			}
		}

		/**
		 * Allows to send a {@code Quote} message to the {@code QuoteGraphGenerator}
		 * agent.
		 *
		 * @param from the original writer of the {@code Quote}
		 * @param to   the repeater of the {@code Quote}
		 */
		private void sendMessageGraph(String from, String to) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, QUOTE_GRAPH_GENERATOR_SERVICE)[0].getName());
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
				logger.severe("Exception while sending the term to the QuoteGraphGenerator agent: " + e);
			}
		}

		/**
		 * Allows to send a {@code Cloud} message to the {@code CloudGenerator} agent.
		 *
		 * @param date the original writer of the {@code Cloud}
		 * @param word the repeater of the {@code Cloud}
		 */
		private void sendMessageCloud(String date, String word) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, CLOUD_GENERATOR_SERVICE)[0].getName());
			msg.setLanguage(codec.getName());
			msg.setOntology(cloudOntology.getName());
			Cloud c = new Cloud();
			c.setTerm(nomFichier);
			c.setDate(date);
			c.setWord(word);
			try {
				myAgent.getContentManager().fillContent(msg, c);
				myAgent.send(msg);
			} catch (CodecException | OntologyException e) {
				logger.severe("Exception while sending the term to the CloudGenerator agent: " + e);
			}
		}
	}
}
