package com.tweetcrawl.structure;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class CloudMap {

    private Map<ZonedDateTime, Map<String, Integer>> datedTags;
    private int currentAgentsNumber;
    private String term;

    public CloudMap(String term) {
        this.term = term;
        datedTags = new HashMap<>();
        this.currentAgentsNumber = 1;
    }


    public Map<String, Integer> getMapFromDate(ZonedDateTime date) {

        ZonedDateTime day = date.truncatedTo(ChronoUnit.DAYS);

        if (!datedTags.containsKey(day))
            datedTags.put(day, new HashMap<>());

        return datedTags.get(day);

    }

    public void addTagToMap(String tag, ZonedDateTime date) {

        Map<String, Integer> selectedMap = getMapFromDate(date);
        int nbOccurences = 1;

        if (selectedMap.containsKey(tag))
            nbOccurences = selectedMap.get(tag) + 1;

        selectedMap.put(tag, nbOccurences);

    }

    private void trimOlderTags() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        Vector<ZonedDateTime> toDelete = new Vector<>();


        for (Map.Entry<ZonedDateTime, Map<String, Integer>> entry : datedTags.entrySet()) {
            if (entry.getKey().until(today, ChronoUnit.DAYS) > 10)
                toDelete.add(entry.getKey());
        }

        for (ZonedDateTime date : toDelete)
            datedTags.remove(date);
    }

    public void addAgent() {
        this.currentAgentsNumber++;
    }

    public int removeAgent() {
        return --this.currentAgentsNumber;
    }

    //METHODE TOP10
    //PENSER A APPELER trimOlderTags()
    //https://www.javacodegeeks.com/2017/09/java-8-sorting-hashmap-values-ascending-descending-order.html
    public Map<String, Integer>[] getTopTags() {

        trimOlderTags();

        Map<String, Integer>[] topList = new LinkedHashMap[10];
        ZonedDateTime[] tenLastDays = new ZonedDateTime[10];

        //Calculate 10 last days
        for (int i = 0; i < 10; i++)
            tenLastDays[i] = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minus(9 - i, ChronoUnit.DAYS);

        Map<String, Integer> sortedTags;
        int topCount;

        //Iterating through the dates
        for (int i = 0; i < 10; i++) {
            //if the i-th day is in the map
            if (datedTags.containsKey(tenLastDays[i])) {

                //Sorting tags
                sortedTags = datedTags.get(tenLastDays[i])
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));


                topCount = 0;

                //Putting ten most used words in corresponding map
                for (Map.Entry<String, Integer> entry : sortedTags.entrySet()) {
                    //This way was safer to use : it iterates until there is no more tags left, and puts up to 10 tags in map
                    if (topCount < 10)
                        topList[i].put(entry.getKey(), entry.getValue());

                    topCount++;
                }
            }
        }

        return topList;

    }

    //  /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\
    // HELLO :) J'ai voulu faire ça pour délester la méthode au dessus mais il gueule tu peux essayer de trouver pourquoi ?
    // à priori il y avait un problème de mappage, il n arrivait pas a associer les types de la map x)
    //  /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\
    private LinkedHashMap sortHashMapElems (String term) {
        Map<String,Integer> map = this.getInnerMap(term);
        LinkedHashMap sorted = map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return sorted;
    }


    //METHODE AFFICHAGE
    //APPELEE A LA FIN DE LA MISE A JOUR
    //ON APPELLE TOP10 METHODE
    public void draw () {}

    public Map<ZonedDateTime, Map<String, Integer>> getDatedTags() {
        return datedTags;
    }

    public Map<String,Integer> getInnerMap(String term){
        return this.datedTags.get(term);
    }
}
