package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.AgentLauncherBehaviour;
import com.tweetcrawl.ontology.CrawlRequestOntology;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.util.Logger;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;
import jade.wrapper.AgentController;

/**
 * Repr�sente l'agent de lancement du syst�me.
 */
public class AgentLauncher extends Agent {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());
	// private int numberOfTreatmentAgents = 2;

	private Codec codec = new SLCodec();
	private Ontology ontology = CrawlRequestOntology.getInstance();

	@Override
	protected void setup() {
		logger.config("Lancement de l'agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);
		this.generateAgents();
		this.addBehaviour(new AgentLauncherBehaviour(this, this.logger, this.codec, this.ontology));
		logger.config("Agent " + this.getLocalName() + " lanc�.");
	}

	/**
	 * Permets de g�n�rer et lancer l'ensemble des agents du syst�me.
	 */
	private void generateAgents() {
		PlatformController container = this.getContainerController();
		try {
			AgentController tweetCrawler = container.createNewAgent("TweetCrawlerAgent",
					"com.tweetcrawl.agents.TweetCrawler", null);
			tweetCrawler.start();
		} catch (Exception e) {
			logger.severe("Exception durant le lancement de l'agent " + this.getLocalName() + " : " + e);
		}
	}

	@Override
	public void doDelete() {
		logger.warning("Arr�t de l'agent " + this.getLocalName());
		try {
			this.getContainerController().kill();
		} catch (StaleProxyException e) {
			logger.severe("Exception durant la demande d'arr�t des autres agents : " + e);
		}
	}

}
