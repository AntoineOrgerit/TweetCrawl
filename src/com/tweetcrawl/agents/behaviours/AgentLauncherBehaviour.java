package com.tweetcrawl.agents.behaviours;

import java.util.Scanner;

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
 * Représente le comportement de l'agent lanceur.
 */
public class AgentLauncherBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1L;
	private Logger logger;
	private Codec codec;
	private Ontology ontology;
	private Scanner reader;
	private boolean exitRequested = false;

	/**
	 * Constructeur du comportement de l'agent lanceur.
	 * 
	 * @param agent    - l'agent TweetCrawler auquel lié le comportement
	 * @param logger   - le logger à utiliser pour les messages d'erreurs
	 * @param codec    - le codec à utiliser pour communiquer avec l'agent crawler
	 * @param ontology - l'ontologie à utiliser pour communiquer avec l'agent
	 *                 crawler
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
		logger.info("En attente d'un terme à rechercher... Entrer EXIT pour arrêter le programme.");
		String term = reader.next();
		if (term.equals("EXIT")) {
			this.exitRequested = true;
			this.reader.close();
		} else {
			this.sendRequestToCrawler(term);
		}
	}

	/**
	 * Permets d'envoyer une requ�te de recherche à l'agent crawler.
	 * 
	 * @param term - le terme à rechercher par l'agent crawler
	 */
	private void sendRequestToCrawler(String term) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("TweetCrawlerAgent", AID.ISLOCALNAME));
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		Crawl crawl = new Crawl();
		crawl.setTerm(term);
		Action action = new Action(myAgent.getAID(), (AgentAction) crawl);
		try {
			myAgent.getContentManager().fillContent(msg, action);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception durant l'envoi du terme vers le TweetCrawlerAgent : " + e);
		}
	}

	@Override
	public boolean done() {
		return this.exitRequested;
	}

}
