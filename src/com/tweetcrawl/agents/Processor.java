package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.ProcessorBehaviour;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.CloudOntology;
import com.tweetcrawl.ontology.FileTwitterOntology;
import com.tweetcrawl.ontology.ProcessorActionOntology;
import com.tweetcrawl.ontology.QuoteOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

/**
 * Agent treating every line of the tweets file.
 */
public class Processor extends Agent {

    private static final long serialVersionUID = 1L;
    private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

    /**
     * Ontologies to be used
     **/
    private Codec codec = new SLCodec();
    private Ontology quoteOntology = QuoteOntology.getInstance();
    private Ontology cloudOntology = CloudOntology.getInstance();
    private Ontology quoteActionOntology = ProcessorActionOntology.getInstance();
    private Ontology fileTwitter = FileTwitterOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        logger.info("Starting of the agent " + this.getLocalName() + "...");
        this.getContentManager().registerLanguage(codec);
        this.getContentManager().registerOntology(quoteOntology);
        this.getContentManager().registerOntology(cloudOntology);
        this.getContentManager().registerOntology(quoteActionOntology);
        this.getContentManager().registerOntology(fileTwitter);
        addBehaviour(
                new ProcessorBehaviour(this, this.logger, this.codec, this.quoteOntology, this.cloudOntology, this.quoteActionOntology));
        DFServiceManager.register(this, "ParseFile-service");
        logger.info("Agent " + this.getLocalName() + " successfully started.");
    }

    @Override
    public void doDelete() {
        logger.info("Shutting down the agent " + this.getLocalName());
    }
}
