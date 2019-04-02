package com.tweetcrawl.agents.utils;

import jade.util.Logger;

public class BBPetterson {
    public static int tour;
    public static boolean[] demandes;
    public static int nbr;

    private static Logger log = Logger.getMyLogger("BB_Petterson");

    public BBPetterson(int n) {
        demandes = new boolean[n];
        for(int i = 0; i < n; i++){
            demandes[i]=false;
        }
        nbr = n;
        log.info("fin init demande");
    }

    public static void afficheDemande(){
        log.info("BB_Petterson");
        for(int i = 0 ; i < nbr; i++){
            log.info(demandes[i]+" ");
        }
        log.info("tour ="+tour);

    }
}