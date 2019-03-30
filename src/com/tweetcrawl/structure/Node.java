package com.tweetcrawl.structure;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the node of a QuoteGraph as a Twitter account
 */
public class Node implements Comparable<Object> {

	private String name;
	private TreeSet<Edge> links;

	/**
	 * Constructor of the node
	 * 
	 * @param name the name of the node Twitter account
	 */
	public Node(String name) {
		this.name = name;
		this.links = new TreeSet<>();
	}

	/**
	 * Allows to retrieve the name of the node
	 * 
	 * @return the name of the node
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Allows to retrieve the links of the node as retweets
	 * 
	 * @return the links of the node as retweets as a Set of Edges
	 */
	public Set<Edge> getLinks() {
		return this.links;
	}

	/**
	 * Allows to add a link to the node as a retweet
	 * 
	 * @param link the link to add the node as a retweet
	 * @return true if it has been added, false if it already exists
	 */
	public boolean addLink(Edge link) {
		return this.links.add(link);
	}

	/**
	 * Allows to check if a node contains a link
	 * 
	 * @param link the link to check
	 * @return true if the node contains the link, false if not
	 */
	public boolean containsLink(Edge link) {
		return this.links.contains(link);
	}

	/**
	 * Allows to convert the node in its .dot file format
	 * 
	 * @return the node in its .dot file format
	 */
	public String toDot() {
		return this.name.substring(1) + " [label=\"" + this.name + "\"];\n";
	}

	@Override
	public int compareTo(Object o) {
		Node node = (Node) o;
		if (this.equals(node)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return this.getName().equals(other.getName());
	}

}
