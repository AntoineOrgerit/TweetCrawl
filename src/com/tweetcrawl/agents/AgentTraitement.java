package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.StateBehaviour;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.ontology.QuoteOntology;
import com.tweetcrawl.ontology.QuotesActionOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.util.Logger;

public class AgentTraitement extends Agent {
    private int id;
    private Logger logger = Logger.getMyLogger(this.getClass().getName());

    /**
     * Ontologies to be used
     **/
    private Codec codec = new SLCodec();
    private Ontology quoteOntology = QuoteOntology.getInstance();
    private Ontology quoteActionOntology = QuotesActionOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        this.id = (Integer.parseInt(getLocalName()));
        logger.config("Starting of the agent " + this.getLocalName() + "...");
        this.getContentManager().registerLanguage(codec);
        this.getContentManager().registerOntology(quoteOntology);
        this.getContentManager().registerOntology(quoteActionOntology);
        addBehaviour(new StateBehaviour(this, this.logger, this.codec, this.quoteOntology, this.quoteActionOntology));
        DFServiceManager.register(this, "ParseFile-service");
        logger.config("Agent " + this.getLocalName() + " successfully started.");
    }

    @Override
    public void doDelete() {
        super.doDelete();
        logger.info("I'm dying");
    }
}
