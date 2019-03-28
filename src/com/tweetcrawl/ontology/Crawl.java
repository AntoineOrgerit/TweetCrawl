package com.tweetcrawl.ontology;

import jade.content.AgentAction;

/**
 * Action used to request a crawling to TweetCrawler
 */
public class Crawl implements AgentAction {

	private static final long serialVersionUID = 1L;
	private String term;

	/**
	 * Allows to get the search term
	 * 
	 * @return		String containing the search term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Allows to define the serch term
	 * 
	 * @param term 		String containing the search term
	 */
	public void setTerm(String term) {
		this.term = term;
	}

}
