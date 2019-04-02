package com.tweetcrawl.ontology;

import jade.content.Predicate;

/**
 * Predicate used to represent a retweet (quote).
 */
public class Quote implements Predicate {

	private static final long serialVersionUID = 1L;

	private String term;
	private String repeater;
	private String original;

	/**
	 * Allows to get the term linked to the quote.
	 * 
	 * @return The term of the quote as a String.
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * Allows to set the term linked to the quote.
	 * 
	 * @param term - the term of the quote as a String
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * Allows to get the repeater who quotes the content.
	 * 
	 * @return The repeater of the quote as a String.
	 */
	public String getRepeater() {
		return repeater;
	}

	/**
	 * Allows to set the repeater who quotes the content.
	 * 
	 * @param repeater - the repeater of the quote as a String
	 */
	public void setRepeater(String repeater) {
		this.repeater = repeater;
	}

	/**
	 * Allows to get the original writer of the content.
	 * 
	 * @return The original writer of the quote as a String.
	 */
	public String getOriginal() {
		return original;
	}

	/**
	 * Allows to set the original writer of the content.
	 * 
	 * @param original - the original writer of the quote as a String
	 */
	public void setOriginal(String original) {
		this.original = original;
	}

}
