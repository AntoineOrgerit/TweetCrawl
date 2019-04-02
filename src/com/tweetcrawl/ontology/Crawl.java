package com.tweetcrawl.ontology;

import jade.content.AgentAction;

/**
 * {@code AgentAction} used to request a crawl to a {@code TweetCrawler} agent.
 * This request requires the {@code CrawlOntology} ontology to be used.
 */
public class Crawl implements AgentAction {

	private static final long serialVersionUID = 1L;
	private String term;

	/**
	 * Allows to get the term associated with the {@code Crawl} action.
	 * 
	 * @return the term associated
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Allows to define the term associated with the {@code Crawl} action.
	 * 
	 * @param term the term to associate
	 */
	public void setTerm(String term) {
		this.term = term;
	}

}
