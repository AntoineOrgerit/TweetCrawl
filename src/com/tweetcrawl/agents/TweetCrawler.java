package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.TweetCrawlerBehaviour;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.CrawlRequestOntology;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

/**
 * Agent allowing to get tweets using Twitter's API
 */
public class TweetCrawler extends Agent {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

	/** Ontologies to be used **/
	private Codec codec = new SLCodec();
	private Ontology crawlRequestOntology = CrawlRequestOntology.getInstance();

	@Override
	protected void setup() {
		logger.info("Starting of the agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(crawlRequestOntology);
		this.addBehaviour(new TweetCrawlerBehaviour(this, this.logger));
		DFServiceManager.register(this, "Tweetcrawler-service");
		logger.info("Agent " + this.getLocalName() + " successfully started.");
	}

	@Override
	public void doDelete() {
		logger.info("Shutting down the agent " + this.getLocalName());
	}

}
