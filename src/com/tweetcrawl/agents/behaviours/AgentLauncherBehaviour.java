package com.tweetcrawl.agents.behaviours;

import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.ontology.Crawl;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

/**
 * Behaviour of the Launcher agent
 */
public class AgentLauncherBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private Logger logger;
	
	/** ontology to use **/
	private Codec codec;
	private Ontology ontology;
	
	private String term;
	

	/**
	 * Constructor of the behaviour
	 * 
	 * @param agent    corresponding Launcher agent
	 * @param logger   logger used to display errors
	 * @param codec    codec used to communicate with the TweetCrawler agent
	 * @param ontology ontology used to communicate with the TweetCrawler agent
	 * @parem term the term to crawl
	 */
	public AgentLauncherBehaviour(Agent agent, Logger logger, Codec codec, Ontology ontology, String term) {
		super(agent);
		this.logger = logger;
		this.codec = codec;
		this.ontology = ontology;
		this.term = term;
	}

	@Override
	public void action() {
		this.sendRequestToCrawler();
	}

	/**
	 * Allows to send a search request to the TweetCrawler agent
	 * 
	 * @param term term to be searched by the TweetCrawler agent
	 */
	private void sendRequestToCrawler() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(DFServiceManager.getAgentsForService(myAgent, "Tweetcrawler-service")[0].getName());
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		Crawl crawl = new Crawl();
		crawl.setTerm(this.term);
		Action action = new Action(myAgent.getAID(), (AgentAction) crawl);
		try {
			myAgent.getContentManager().fillContent(msg, action);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

}
