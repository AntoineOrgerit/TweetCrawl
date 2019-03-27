package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * Représente un prédicat utilisé pour demander une action crawl au
 * TweetCrawler.
 */
public class Crawl implements Predicate {

	private static final long serialVersionUID = 1L;
	private String term;

	/**
	 * Permets d'obtenir le terme de la recherche à effectuer.
	 * 
	 * @return Le terme de la recherche à effectuer sous la forme d'un String.
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Permets de définir le terme de la recherche à effectuer.
	 * 
	 * @param term - le terme de la recherce à effectuer sous la forme d'un String
	 */
	public void setTerm(String term) {
		this.term = term;
	}

}
