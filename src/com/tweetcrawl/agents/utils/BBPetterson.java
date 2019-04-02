package com.tweetcrawl.agents.utils;

public class BBPetterson {
    private int tour;
    private boolean[] demandes;
    private int nbr;
    
    private static BBPetterson instance = null;
    
    public static BBPetterson getInstance() throws BBPettersonException {
    	if(instance == null) {
    		throw new BBPettersonException("BBPetterson has not been instanciated.");
    	}
    	return instance;
    }
    
    public static synchronized void createInstance(int nbConcurrent) throws BBPettersonException {
    	if(instance == null) {
    		instance = new BBPetterson(nbConcurrent);
    	} else {
    		throw new BBPettersonException("BBPetterson has already been instanciated.");
    	}
    }

    private BBPetterson(int n) {
        this.demandes = new boolean[n];
        for(int i = 0; i < n; i++){
            this.demandes[i]=false;
        }
        this.nbr = n;
    }
    
    public synchronized int getTour() {
    	return this.tour;
    }
    
    public synchronized void setTour(int tour) {
    	this.tour = tour;
    }
    
    public synchronized boolean getDemande(int index) {
    	return this.demandes[index];
    }
    
    public synchronized void setDemande(int index, boolean value) {
    	this.demandes[index] = value;
    }
    
    public synchronized int getNbDemandes() {
    	return this.demandes.length;
    }
    
}
