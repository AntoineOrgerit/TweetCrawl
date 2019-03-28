package com.tweetcrawl.ontology;

import jade.content.Predicate;

public class FileTwitter implements Predicate {

    private String name;

    /**
     * Permet de recuperer le nom du fichier
     * qui a ete genere par le tweetCrawler
     * @return le nom du fichier
     */
    public String getName() {
        return name;
    }

    /**
     * Permet de modifier le nom du fichier
     * source des tweets recuperes.
     * @param name le nouveau nom du fichier
     */
    public void setName(String name) {
        this.name = name;
    }
}
