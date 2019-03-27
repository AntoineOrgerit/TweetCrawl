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
 * Représente l'agent de lancement du système.
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
		logger.config("Agent " + this.getLocalName() + " lancé.");
	}

	/**
	 * Permets de générer et lancer l'ensemble des agents du système.
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
		logger.warning("Arrêt de l'agent " + this.getLocalName());
		try {
			this.getContainerController().kill();
		} catch (StaleProxyException e) {
			logger.severe("Exception durant la demande d'arrêt des autres agents : " + e);
		}
	}

}
