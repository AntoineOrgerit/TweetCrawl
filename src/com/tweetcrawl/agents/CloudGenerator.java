package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.CloudGeneratorBehaviour;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.CloudOntology;
import com.tweetcrawl.ontology.ProcessorActionOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

/**
 * Agent allowing to generate word clouds of the words link to a term searched.
 */
public class CloudGenerator extends Agent {
	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

	/** ontologies to be used **/
	private Codec codec = new SLCodec();
	private Ontology cloudOntology = CloudOntology.getInstance();
	private Ontology processorActionOntology = ProcessorActionOntology.getInstance();

	@Override
	protected void setup() {
		logger.info("Starting the agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(cloudOntology);
		this.getContentManager().registerOntology(processorActionOntology);
		this.addBehaviour(new CloudGeneratorBehaviour(this, this.logger));
		DFServiceManager.register(this, "CloudGenerator-service");
		logger.info("Agent " + this.getLocalName() + " successfully started.");
	}

	@Override
	public void doDelete() {
		logger.info("Stopping the agent " + this.getLocalName());
	}

}
