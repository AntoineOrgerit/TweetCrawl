package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * {@code Predicate} used to inform a {@code QuoteGraphGenerator} agent about a
 * current state of treatment of a {@code Processor} agent. This information
 * requires the {@code QuoteOntology} ontology to be used.
 */
public class QuotesAction implements Predicate {

	private static final long serialVersionUID = 1L;

	private String term;
	private String action;

	/**
	 * Allows to get the term associated with the {@code QuotesAction} predicate.
	 * 
	 * @return the term associated
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Allows to define the term associated with the {@code Quote} predicate.
	 * 
	 * @param term the term to associate
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Allows to get the action linked to the current state of treatment.
	 * 
	 * @return the current state action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Allows to define the action linked to the current state of treatment.
	 * 
	 * @param term the current state action
	 */
	public void setAction(String action) {
		this.action = action;
	}

}
