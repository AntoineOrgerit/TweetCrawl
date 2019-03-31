package com.tweetcrawl.agents;

import java.io.File;

import javax.swing.JDialog;

import com.tweetcrawl.agents.ui.AgentLauncherGUI;
import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.Crawl;
import com.tweetcrawl.ontology.CrawlRequestOntology;

import jade.content.AgentAction;
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
import jade.wrapper.PlatformController;
import jade.wrapper.AgentController;

/**
 * Agent starting the system
 */
public class AgentLauncher extends GuiAgent {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());
	// private int numberOfTreatmentAgents = 2;

	private Codec codec = new SLCodec();
	private Ontology crawlRequestOntology = CrawlRequestOntology.getInstance();
	private Ontology jadeManagementOntology = JADEManagementOntology.getInstance();

	@Override
	protected void setup() {
		JDialog dialog = logger.info(
				"TweetCrawler launcher agent " + this.getLocalName() + " is launching the AMS, please wait a moment.",
				this.getLocalName(), false);
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(crawlRequestOntology);
		this.getContentManager().registerOntology(jadeManagementOntology);
		this.checkDirectories();
		this.generateAgents();
		AgentLauncherGUI gui = new AgentLauncherGUI(this);
		logger.info("TweetCrawler launcher agent " + this.getLocalName() + " has successfully started the AMS.");
		dialog.setVisible(false);
		gui.setVisible(true);
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
			AgentController quoteGraphGenerator = container.createNewAgent("QuoteGraphGeneratorAgent",
					"com.tweetcrawl.agents.QuoteGraphGenerator", null);
			quoteGraphGenerator.start();
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
		msg.setLanguage(this.codec.getName());
		msg.setOntology(this.jadeManagementOntology.getName());
		try {
			this.getContentManager().fillContent(msg, new Action(this.getAID(), new ShutdownPlatform()));
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
			this.sendRequestToCrawler((String) e.getParameter(0));
			break;
		default:
			break;
		}
	}

	/**
	 * Allows to send a search request to the TweetCrawler agent
	 * 
	 * @param term term to be searched by the TweetCrawler agent
	 */
	private void sendRequestToCrawler(String term) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(DFServiceManager.getAgentsForService(this, "Tweetcrawler-service")[0].getName());
		msg.setLanguage(this.codec.getName());
		msg.setOntology(this.crawlRequestOntology.getName());
		Crawl crawl = new Crawl();
		crawl.setTerm(term);
		Action action = new Action(this.getAID(), (AgentAction) crawl);
		try {
			this.getContentManager().fillContent(msg, action);
			this.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the TweetCrawler agent : " + e);
		}
	}

}
