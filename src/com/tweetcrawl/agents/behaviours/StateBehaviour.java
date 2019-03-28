package com.tweetcrawl.agents.behaviours;


import com.tweetcrawl.agents.AgentTraitement;
import com.tweetcrawl.agents.utils.BBPetterson;
import com.tweetcrawl.ontology.FileTwitter;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class StateBehaviour extends FSMBehaviour {

    private static String nomFichier;
    private String recherche = new String();
    private File FileTweet;
    private boolean done = false;
    public StateBehaviour() {

        this.registerFirstState(new OneShotBehaviour() {
            int result;
            @Override
            public void action() {

                if(!done ){
                    result = receptionMsg()?1:0;
                    if (done){
                        envoieMsgBegin();
                    }
                }

            }

            @Override
            public int onEnd() {

                BBPetterson.tour = (((AgentTraitement)myAgent).id+1)%BBPetterson.demandes.length;
                BBPetterson.demandes[((AgentTraitement)myAgent).id] = 1;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return result;
            }
        },"receptionMessage");

        this.registerState(new OneShotBehaviour(myAgent) {
            @Override
            public void action() {
                BBPetterson.afficheDemande();
            }

            @Override
            public int onEnd() {
                return ((BBPetterson.tour == ((AgentTraitement)myAgent).id || noDemande()) == true?1:0);
            }
        },"demande");

        this.registerState(new OneShotBehaviour(myAgent) {
            boolean isEnd = false;
            @Override
            public void onStart() {

            }

            @Override
            public void action() {
                isEnd = getandremoveline();
            }

            @Override
            public int onEnd() {
                if(isEnd){
                    done = false;
                    envoieMsgEnfGraph();
                    envoieMsgEndCloud();
                }
                envoieMsgGraph();
                envoieMsgCloud();
                BBPetterson.demandes[((AgentTraitement)myAgent).id] = 0;
                return isEnd?0:1;
            }
        },"lectureFichier");

        this.registerState(new OneShotBehaviour(myAgent) {
            @Override
            public void action() {

            }

            @Override
            public int onEnd() {
                done = false;
                return 1;
            }
        },"end");

        this.registerTransition("receptionMessage","receptionMessage",0);
        this.registerTransition("receptionMessage","demande",1);
        this.registerTransition("demande","demande",0);
        this.registerTransition("demande","lectureFichier",1);
        this.registerTransition("lectureFichier","end",0);
        this.registerTransition("lectureFichier","receptionMessage",1);


    }//end of constructor

    private void envoieMsgCloud() {
    }

    private void envoieMsgGraph() {

    }

    private boolean getandremoveline() {
        recherche= "";
        FileTweet = new File(".\\data\\"+nomFichier+".txt");
        Scanner scanner = null;
        try{
            scanner = new Scanner(FileTweet);
            if(scanner.hasNextLine()){
                recherche = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null){
                scanner.close();
            }
        }
        if(recherche.length()!= 0){
            removeLine(recherche);
            return false;
        }
        return true;
    }

    private void removeLine(String recherche) {
        try{
            File tmp = new File("temporaire");
            PrintWriter out = new PrintWriter(new FileWriter(tmp));

            Files.lines(FileTweet.toPath()).filter(line-> !line.contains(recherche)).forEach(out::println);
            out.flush();
            out.close();
            tmp.renameTo(FileTweet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean noDemande() {
        for(int i = 0 ; i < BBPetterson.demandes.length; i++){
            if( i != ((AgentTraitement)myAgent).id && BBPetterson.demandes[i] == 1){
                return true;
            }
        }
        return false;
    }

    private boolean receptionMsg() {
        ACLMessage msg = myAgent.receive();
        if (msg != null){
            ContentManager cm = myAgent.getContentManager();

            try {
                Predicate pre = (Predicate) cm.extractContent(msg);
                FileTwitter fl = (FileTwitter)pre;
                nomFichier=fl.getName();
                System.out.println(nomFichier);
                done = true;
            } catch (Codec.CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }

        } else {
            return false;
        }
        return true;
    }
}
