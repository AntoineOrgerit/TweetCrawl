package com.tweetcrawl.agents;

import java.io.File;

import com.tweetcrawl.agents.behaviours.AgentLauncherBehaviour;
import com.tweetcrawl.agents.ui.AgentLauncherGUI;
import com.tweetcrawl.ontology.CrawlRequestOntology;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.wrapper.PlatformController;
import jade.wrapper.AgentController;

/**
 * Agent starting the system
 */
public class AgentLauncher extends GuiAgent {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());
	private AgentLauncherGUI gui;
	// private int numberOfTreatmentAgents = 2;

	private Codec codec = new SLCodec();
	private Ontology crawlRequestOntology = CrawlRequestOntology.getInstance();
	private Ontology jadeManagementOntology = JADEManagementOntology.getInstance();

	@Override
	protected void setup() {
		logger.config("Starting the agent " + this.getLocalName() + "...");
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(crawlRequestOntology);
		this.getContentManager().registerOntology(jadeManagementOntology);
		this.checkDirectories();
		this.generateAgents();
		this.gui = new AgentLauncherGUI(this);
		this.gui.setVisible(true);
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
		} catch (Exception e) {
			logger.severe("Exception during the starting of the agent " + this.getLocalName() + " : " + e);
		}
	}

	/**
	 * Checks the directory to see if they have to be created.
	 */
	private void checkDirectories() {
		File directory = new File("./data/");
		if (!directory.exists()) {
			directory.mkdir();
		}
		directory = new File("./visualisation/");
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	/**
	 * Allows to shutdown the AMS system
	 */
	public void shutdown() {
		logger.info("Stopping the system... It can take some time, please wait.");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(this.getAMS());
		msg.setLanguage(codec.getName());
		msg.setOntology(jadeManagementOntology.getName());
		try {
			getContentManager().fillContent(msg, new Action(getAID(), new ShutdownPlatform()));
			send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception encountered while stopping the AMS: " + e);
		}
	}

	@Override
	protected void onGuiEvent(GuiEvent e) {
		switch (e.getType()) {
		case AgentLauncherGUI.EXIT:
			this.shutdown();
			break;
		case AgentLauncherGUI.INPUT:
			this.addBehaviour(new AgentLauncherBehaviour(this, this.logger, this.codec, this.crawlRequestOntology, (String) e.getParameter(0)));
			break;
		default:
			break;
		}
	}

}
