package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.TweetCrawlerBehaviour;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.ontology.QuotesActionOntology;
import com.tweetcrawl.ontology.QuoteOntology;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.util.Logger;

public class QuoteGraphGenerator extends Agent  {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	private Codec codec = new SLCodec();
	private Ontology quoteOntology = QuoteOntology.getInstance();
	private Ontology endQuotesOntology = QuotesActionOntology.getInstance();
	
	@Override
	protected void setup() {
		logger.config("Starting the agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(quoteOntology);
		this.getContentManager().registerOntology(endQuotesOntology);
		this.addBehaviour(new TweetCrawlerBehaviour(this, this.logger));
		DFServiceManager.register(this, "QuoteGraphGenerator-service");
		logger.config("Agent " + this.getLocalName() + " successfully started.");
	}

	@Override
	public void doDelete() {
		logger.warning("Stopping the agent " + this.getLocalName());
	}
	
}
