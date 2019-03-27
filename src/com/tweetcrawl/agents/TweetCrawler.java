package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.TweetCrawlerBehaviour;
import com.tweetcrawl.ontology.CrawlRequestOntology;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.util.Logger;

/**
 * Représente un agent permettant de récupérer les tweets à partir de l'API de
 * Twitter.
 */
public class TweetCrawler extends Agent {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	/** Ontologies à utiliser **/
	private Codec codec = new SLCodec();
	private Ontology crawlRequestOntology = CrawlRequestOntology.getInstance();

	@Override
	protected void setup() {
		logger.config("Lancement de l'agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(crawlRequestOntology);
		this.addBehaviour(new TweetCrawlerBehaviour(this, this.logger));
		logger.config("Agent " + this.getLocalName() + " lancé.");
	}

	@Override
	public void doDelete() {
		logger.warning("Arrêt de l'agent " + this.getLocalName());
	}

}
