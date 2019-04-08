package com.tweetcrawl.agents.behaviours;

import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.Cloud;
import com.tweetcrawl.ontology.ProcessorAction;
import com.tweetcrawl.structure.CloudMap;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
 * Behaviour of the {@code CloudGenerator} agent.
 */
public class CloudGeneratorBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter originalDateFormat = DateTimeFormatter
			.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
	private TweetCrawlerLogger logger;

	private transient Map<String, CloudMap> cloudConstruction;

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
		this.cloudConstruction = new HashMap<>();
	}

	@Override
	public void action() {
		ContentElement element = this.getContentOfMessage();
		if (element != null) {
			if (element instanceof ProcessorAction) {
				this.treatCloudAction((ProcessorAction) element);
			} else {
				if (element instanceof Cloud) {
					this.addCloudMap((Cloud) element);
				}
			}
		} else {
			this.block();
		}
	}

	/**
	 * Allows to add an element to the {@code CloudMap} word cloud.
	 *
	 * @param cloud the word cloud in which to add the element
	 */
	private void addCloudMap(Cloud cloud) {
		String term = cloud.getTerm();
		String tag = cloud.getWord();
		String dateStr = cloud.getDate();
		ZonedDateTime date = ZonedDateTime.parse(dateStr, originalDateFormat);
		cloudConstruction.get(term).addTagToMap(tag, date);
	}

	/**
	 * Allows to treat {@code ProcessorAction} messages content.
	 * 
	 * @param processorAction the {@code ProcessorAction} to treat
	 */
	private void treatCloudAction(ProcessorAction processorAction) {
		if (processorAction.getAction().equals("begin")) {
			this.createCloudIfNotExists(processorAction.getTerm());
		} else {
			if (processorAction.getAction().equals("end")) {
				this.removeAgentFromCloud(processorAction.getTerm());
			}
		}
	}

	/**
	 * Allows to decrease the number of agents working on a {@code CloudMap} word
	 * cloud.
	 * 
	 * @param term the term associated to the word cloud
	 */
	private void removeAgentFromCloud(String term) {
		CloudMap cloudMap = this.cloudConstruction.get(term);
		int totalAgent = cloudMap.removeAgent();
		if (totalAgent == 0) {
			cloudMap.draw();
			this.cloudConstruction.remove(term);
		}

	}

	/**
	 * Allows to create a {@code CloudMap} word cloud if it doesn't already exist
	 * otherwise retrieve the existing one
	 *
	 * @param term the term associated to the word cloud
	 */
	private void createCloudIfNotExists(String term) {
		CloudMap cloudMap;
		if (this.cloudConstruction.containsKey(term)) {
			cloudMap = this.cloudConstruction.get(term);
			cloudMap.addAgent();
			this.cloudConstruction.put(term, cloudMap);
		} else {
			cloudMap = new CloudMap(term);
			this.cloudConstruction.put(term, cloudMap);
		}
	}

	/**
	 * Allows to retrieve the content of the message
	 *
	 * @return the (@code ContentElement) of the message
	 */
	private ContentElement getContentOfMessage() {
		ACLMessage msg = myAgent.receive();
		ContentElement element = null;
		if (msg != null) {
			try {
				ContentManager cm = myAgent.getContentManager();
				element = cm.extractContent(msg);
			} catch (Codec.CodecException | OntologyException e) {
				logger.severe("Exception during extraction of message" + e);
			}
		}
		return element;
	}

}
