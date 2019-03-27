package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * Repr�sente un pr�dicat utilis� pour demander une action crawl au
 * TweetCrawler.
 */
public class Crawl implements Predicate {

	private static final long serialVersionUID = 1L;
	private String term;

	/**
	 * Permets d'obtenir le terme de la recherche � effectuer.
	 * 
	 * @return Le terme de la recherche � effectuer sous la forme d'un String.
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Permets de d�finir le terme de la recherche � effectuer.
	 * 
	 * @param term - le terme de la recherce � effectuer sous la forme d'un String
	 */
	public void setTerm(String term) {
		this.term = term;
	}

}
