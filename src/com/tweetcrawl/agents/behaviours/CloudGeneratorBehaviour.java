package com.tweetcrawl.agents.behaviours;

import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import java.util.Map;

public class CloudGeneratorBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    private TweetCrawlerLogger logger;

    private transient Map<String, Map<String,Integer>> cloudConstruction;

    /**
     * Constructor of the behaviour.
     *
     * @param agent  the {@code QuoteGraphGenerator} agent using the behaviour
     * @param logger the {@code TweetCrawlerLogger} used to display errors
     *               encountered
     */
    public CloudGeneratorBehaviour(Agent agent, TweetCrawlerLogger logger) {
        super(agent);
        this.logger = logger;
    }

    @Override
    public void action() {

    }


}
