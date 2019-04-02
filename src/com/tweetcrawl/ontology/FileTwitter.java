package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * {@code Predicate} used to inform a {@code Processor} agent about a new
 * available file to treat. This information requires the
 * {@code FileTwitterOntology} ontology to be used.
 */
public class FileTwitter implements Predicate {

	private static final long serialVersionUID = 1L;
	private String term;

	/**
	 * Allows to get the term associated with the {@code FileTwitter} predicate.
	 * 
	 * @return the term associated
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Allows to define the term associated with the {@code FileTwitter} predicate.
	 * 
	 * @param term the term to associate
	 */
	public void setTerm(String term) {
		this.term = term;
	}
}
