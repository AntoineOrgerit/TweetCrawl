package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.QuoteGraphGeneratorBehaviour;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.QuotesActionOntology;
import com.tweetcrawl.ontology.QuoteOntology;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

/**
 * Agent allowing to generate the graph of the retweets.
 */
public class QuoteGraphGenerator extends Agent {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

	/** ontologies to be used **/
	private Codec codec = new SLCodec();
	private Ontology quoteOntology = QuoteOntology.getInstance();
	private Ontology quotesActionOntology = QuotesActionOntology.getInstance();

	@Override
	protected void setup() {
		logger.info("Starting the agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(quoteOntology);
		this.getContentManager().registerOntology(quotesActionOntology);
		this.addBehaviour(new QuoteGraphGeneratorBehaviour(this, this.logger));
		DFServiceManager.register(this, "QuoteGraphGenerator-service");
		logger.info("Agent " + this.getLocalName() + " successfully started.");
	}

	@Override
	public void doDelete() {
		logger.info("Stopping the agent " + this.getLocalName());
	}

}
