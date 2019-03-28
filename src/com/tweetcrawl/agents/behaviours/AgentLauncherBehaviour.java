package com.tweetcrawl.agents.behaviours;

import java.util.Scanner;

import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.ontology.Crawl;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

/**
 * Behaviour of the Launcher agent
 */
public class AgentLauncherBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1L;
	private Logger logger;
	private Codec codec;
	private Ontology ontology;
	private Scanner reader;
	private boolean exitRequested = false;

	/**
	 * Constructor of the behaviour
	 * 
	 * @param agent   	corresponding Launcher agent 
	 * @param logger   	logger used to display errors
	 * @param codec    	codec used to communicate with the TweetCrawler agent
	 * @param ontology 	ontology used to communicate with the TweetCrawler agent
	 */
	public AgentLauncherBehaviour(Agent agent, Logger logger, Codec codec, Ontology ontology) {
		super(agent);
		this.logger = logger;
		this.codec = codec;
		this.ontology = ontology;
		this.reader = new Scanner(System.in);
	}

	@Override
	public void action() {
		logger.info("Waiting for a term to search... send EXIT to quit.");
		String term = reader.next();
		if (term.equals("EXIT")) {
			this.exitRequested = true;
			this.reader.close();
		} else {
			this.sendRequestToCrawler(term);
		}
	}

	/**
	 * Allows to send a search request to the TweetCrawler agent
	 * 
	 * @param term		term to be searched by the TweetCrawler agent
	 */
	private void sendRequestToCrawler(String term) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("TweetCrawlerAgent", AID.ISLOCALNAME));
		msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, "Tweetcrawler-service")[0].getName());
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		Crawl crawl = new Crawl();
		crawl.setTerm(term);
		Action action = new Action(myAgent.getAID(), (AgentAction) crawl);
		try {
			myAgent.getContentManager().fillContent(msg, action);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

	@Override
	public boolean done() {
		return this.exitRequested;
	}

}
