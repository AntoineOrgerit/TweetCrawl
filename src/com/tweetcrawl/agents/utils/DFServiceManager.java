package com.tweetcrawl.agents.utils;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.Logger;

/**
 * Manager de l'enregistrement et la consommation de services.
 */
public class DFServiceManager {
	
	private static Logger logger = (Logger) Logger.getLogger(DFServiceManager.class.getName());
	
	private DFServiceManager() {}

	/**
	 * Permets d'enregistrer un service.
	 * 
	 * @param agent - l'agent qui enregistre le service
	 * @param serviceType - le type de service proposé
	 */
	public static void register(Agent agent, String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID()); 
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(agent.getLocalName());
        dfd.addServices(sd);
        try {  
            DFService.register(agent, dfd);  
        } catch (FIPAException fe) {
        	logger.severe("Exception durant l'enregistrement du service " + serviceType + " de l'agent " + agent.getLocalName() + " : " + fe);
        }
	}
	
	/**
	 * Permets d'obtenir les AIDs des agents proposant un service précis
	 * 
	 * @param agent - l'agent demandant un service
	 * @param serviceType - le type de service demandé
	 * @return Les agents proposant le service demandé sous la forme d'un tableau de DFAgentDescription
	 */
	public static DFAgentDescription[] getAgentsForService(Agent agent, String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd  = new ServiceDescription();
		sd.setType(serviceType);
		dfd.addServices(sd);
		DFAgentDescription[] agents = null;
		try {
			agents = DFService.search(agent, dfd);
		} catch (FIPAException fe) {
        	logger.severe("Exception durant l'enregistrement du service " + serviceType + " de l'agent " + agent.getLocalName() + " : " + fe);
        }
		return agents;
	}
	
}
