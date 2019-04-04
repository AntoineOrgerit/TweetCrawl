package com.tweetcrawl.ontology;


import jade.content.Predicate;

public class Cloud implements Predicate {
    private static final long serialVersionUID = 1L;

    private String term;
    private String date;
    private String word;

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
     * Allows to get the date of the content.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Allows to define the date of the content.
     *
     * @param date the date of the tweet
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Allows to get the word of the tweet.
     *
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Allows to define the word of the content.
     *
     * @param word the word of the tweet
     */
    public void setWord(String word) {
        this.word = word;
    }
}
