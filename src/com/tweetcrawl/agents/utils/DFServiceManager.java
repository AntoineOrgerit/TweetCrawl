package com.tweetcrawl.agents.utils;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * Manager taking care of agents services.
 */
public class DFServiceManager {

	private static TweetCrawlerLogger logger = new TweetCrawlerLogger(DFServiceManager.class.getName());

	private DFServiceManager() {
	}

	/**
	 * Allows to register a service.
	 * 
	 * @param agent       the agent registering the service
	 * @param serviceType the type of offered service
	 */
	public static void register(Agent agent, String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(agent.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		sd.setName(agent.getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(agent, dfd);
		} catch (FIPAException fe) {
			logger.severe("Exception while trying to register the service " + serviceType + " of the agent "
					+ agent.getLocalName() + " : " + fe);
		}
	}

	/**
	 * Allows to get the {@code DFAgentDescription} of each agents offering a
	 * specified service.
	 * 
	 * @param agent       the agent requesting the service
	 * @param serviceType the type of resquested service
	 * @return DFAgentDescriptions {@code DFAgentDescription} Array of the agents
	 *         offering the requested service
	 */
	public static DFAgentDescription[] getAgentsForService(Agent agent, String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		DFAgentDescription[] agents = null;
		try {
			agents = DFService.search(agent, dfd);
		} catch (FIPAException fe) {
			logger.severe("Exception while trying to register the service " + serviceType + " of the agent "
					+ agent.getLocalName() + " : " + fe);
		}
		return agents;
	}

}
