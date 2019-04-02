package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * {@code Predicate} used to inform a {@code QuoteGraphGenerator} agent about a
 * new quote (as a retweet) linked to a term. This information requires the
 * {@code QuoteOntology} ontology to be used.
 */
public class Quote implements Predicate {

	private static final long serialVersionUID = 1L;

	private String term;
	private String repeater;
	private String original;

	/**
	 * Allows to get the term associated with the {@code Quote} predicate.
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
	 * Allows to get the repeater who quotes the content.
	 * 
	 * @return the repeater
	 */
	public String getRepeater() {
		return repeater;
	}

	/**
	 * Allows to define the repeater who quotes the content.
	 * 
	 * @param repeater the repeater of the quote
	 */
	public void setRepeater(String repeater) {
		this.repeater = repeater;
	}

	/**
	 * Allows to get the original writer of the quoted content.
	 * 
	 * @return the original writer
	 */
	public String getOriginal() {
		return original;
	}

	/**
	 * Allows to define the original writer of the quoted content.
	 * 
	 * @param original the original writer
	 */
	public void setOriginal(String original) {
		this.original = original;
	}

}
