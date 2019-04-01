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
 * Agent starting the system
 */
public class AgentLauncher extends Agent {

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getMyLogger(this.getClass().getName());
    private final static int numberOfTreatmentAgents = 2;

    private Codec codec = new SLCodec();
    private Ontology ontology = CrawlRequestOntology.getInstance();


    @Override
    protected void setup() {
        logger.config("Starting the agent " + this.getLocalName() + "...");
        this.getContentManager().registerLanguage(codec);
        this.getContentManager().registerOntology(ontology);
        this.generateAgents();
        this.addBehaviour(new AgentLauncherBehaviour(this, this.logger, this.codec, this.ontology));
        logger.config("Agent " + this.getLocalName() + " successfully started.");
    }

    /**
     * Allows to generate and start all of the agents in the system
     */
    private void generateAgents() {
        PlatformController container = this.getContainerController();
        try {
            AgentController tweetCrawler = container.createNewAgent("TweetCrawlerAgent",
                    "com.tweetcrawl.agents.TweetCrawler", null);
            tweetCrawler.start();
            // instanciation of treatment agents
            for (int i = 0; i < numberOfTreatmentAgents; i++) {
                AgentController agentTraitement = container.createNewAgent("AgentTraitement",
                        "com.tweetcrawl.agents.AgentTraitement", null);
                agentTraitement.start();
            }
        } catch (Exception e) {
            logger.severe("Exception during the starting of the agent " + this.getLocalName() + " : " + e);
        }
    }

    @Override
    public void doDelete() {
        logger.warning("Stopping the agent " + this.getLocalName());
        try {
            this.getContainerController().kill();
        } catch (StaleProxyException e) {
            logger.severe("Exception while requesting the shutdown of other agents : " + e);
        }
    }

}
