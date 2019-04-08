package com.tweetcrawl.structure;

import com.tweetcrawl.agents.ui.CloudImageFrame;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.stream.Collectors.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Represents a word cloud collection by days.
 */
public class CloudMap {

	private Map<ZonedDateTime, Map<String, Integer>> datedTags;
	private int currentAgentsNumber;
	private String term;

	/**
	 * Constructor of the {@code CloupMap}.
	 * 
	 * @param term the term associated to the word cloud collection
	 */
	public CloudMap(String term) {
		this.term = term;
		datedTags = new HashMap<>();
		this.currentAgentsNumber = 1;
	}

	/**
	 * Allows to retrieve the word cloud for a specific date.
	 * 
	 * @param date the date of the word cloud to retrieve
	 * @return a {@code Map} containing the words of the word cloud
	 */
	public Map<String, Integer> getMapFromDate(ZonedDateTime date) {
		ZonedDateTime day = date.truncatedTo(ChronoUnit.DAYS);
		if (!datedTags.containsKey(day)) {
			datedTags.put(day, new HashMap<>());
		}
		return datedTags.get(day);
	}

	/**
	 * Allows to retrieve the word clouds of every dates.
	 * 
	 * @return a {@code Map} of all word clouds
	 */
	public Map<ZonedDateTime, Map<String, Integer>> getDatedTags() {
		return datedTags;
	}

	/**
	 * Allows to add a tag to a word cloud.
	 * 
	 * @param tag  the tag to add to the word cloud
	 * @param date the date of the world cloud to add the tag
	 */
	public void addTagToMap(String tag, ZonedDateTime date) {
		Map<String, Integer> selectedMap = getMapFromDate(date);
		int nbOccurences = 1;
		if (selectedMap.containsKey(tag)) {
			nbOccurences = selectedMap.get(tag) + 1;
		}
		selectedMap.put(tag, nbOccurences);
	}

	/**
	 * Allows to remove tags with dates older than 10 days before the current date.
	 */
	private void trimOlderTags() {
		ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
		ArrayList<ZonedDateTime> toDelete = new ArrayList<>();
		for (Map.Entry<ZonedDateTime, Map<String, Integer>> entry : datedTags.entrySet()) {
			if (entry.getKey().until(today, ChronoUnit.DAYS) > 10) {
				toDelete.add(entry.getKey());
			}
		}
		for (ZonedDateTime date : toDelete) {
			datedTags.remove(date);
		}
	}

	/**
	 * Allows to increment the number of agent currently working on the
	 * {@code CloudMap}.
	 */
	public void addAgent() {
		this.currentAgentsNumber++;
	}

	/**
	 * Allows to decrease the number of agent currently working on the
	 * {@code CloudMap}.
	 * 
	 * @return the current number of agent currently working on the word cloud
	 */
	public int removeAgent() {
		return --this.currentAgentsNumber;
	}

	/**
	 * Allows to output the {@code CloudMap}.
	 */
	public void draw() {
		CloudImageFrame frame = new CloudImageFrame(this.term, getTopTags());
		frame.addWindowListener(new WindowAdapter(){
			@Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
	}

	/**
	 * Allows to retrieve the ten top tags in the word clouds.
	 * 
	 * @return a {@code Map} containing the word clouds with the top ten tags
	 */
	private Map<String, Integer>[] getTopTags() {
		this.trimOlderTags();
		Map<String, Integer>[] topList = new LinkedHashMap[10];
		ZonedDateTime[] tenLastDays = new ZonedDateTime[10];
		// Calculate 10 last days
		for (int i = 0; i < 10; i++) {
			tenLastDays[i] = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minus((long) 9 - i, ChronoUnit.DAYS);
		}
		Map<String, Integer> sortedTags;
		int topCount;
		// Iterating through the dates
		for (int i = 0; i < 10; i++) {
			// Initialization of the day
			topList[i] = new LinkedHashMap<>();
			// if the i-th day is in the map
			if (datedTags.containsKey(tenLastDays[i])) {
				// Sorting tags
				sortedTags = datedTags.get(tenLastDays[i]).entrySet().stream()
						.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
						.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
				topCount = 0;
				// Putting ten most used words in corresponding map
				for (Map.Entry<String, Integer> entry : sortedTags.entrySet()) {
					// This way was safer to use : it iterates until there is no more tags left, and
					// puts up to 10 tags in map
					if (topCount < 10) {
						topList[i].put(entry.getKey(), entry.getValue());
					}
					topCount++;
				}
			}
		}
		return topList;
	}
}
