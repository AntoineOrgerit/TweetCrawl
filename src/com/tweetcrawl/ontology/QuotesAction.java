package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * Predicate used to inform about the action on quotes treatement from a
 * treatement agent to the QuoteGraphGenerator agent
 */
public class QuotesAction implements Predicate {

	private static final long serialVersionUID = 1L;

	private String term;
	private String action;

	/**
	 * Allows to get the term linked to the quotes.
	 * 
	 * @return The term of the quotes as a String.
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Allows to set the term linked to the quotes.
	 * 
	 * @param term - the term of the quotes as a String
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Allows to get the action linked to the quotes.
	 * 
	 * @return The action of the quotes as a String.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Allows to set the action linked to the quotes.
	 * 
	 * @param term - the action of the quotes as a String
	 */
	public void setAction(String action) {
		this.action = action;
	}

}
