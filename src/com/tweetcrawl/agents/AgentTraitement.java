package com.tweetcrawl.agents;

import com.tweetcrawl.agents.behaviours.StateBehaviour;
import jade.core.Agent;

public class AgentTraitement extends Agent {
    public int id;
    @Override
    protected void setup() {
        super.setup();
        id = Integer.parseInt(getLocalName());
        System.out.println("Lancement de l'agent +"+getAID());

        addBehaviour(new StateBehaviour());
    }

    @Override
    public void doDelete() {
        super.doDelete();
        System.out.println("I'm dying");
    }
}
