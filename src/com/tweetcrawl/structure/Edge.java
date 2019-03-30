package com.tweetcrawl.structure;

/**
 * Represents the edge of a QuoteGraph as a quote relation
 */
public class Edge implements Comparable<Object> {

	private Node repeater;
	private Node original;

	/**
	 * Constructor of the edge
	 * 
	 * @param repeater the repeater of the quote represented
	 * @param original the original of the quote represented
	 */
	public Edge(Node repeater, Node original) {
		this.repeater = repeater;
		this.original = original;
	}

	/**
	 * Allows to retrieve the repeater of the quote relation
	 * 
	 * @return the repeater of the quote relation
	 */
	public Node repeater() {
		return this.repeater;
	}

	/**
	 * Allows to retrieve the original of the quote relation
	 * 
	 * @return the original of the quote relation
	 */
	public Node original() {
		return this.original;
	}

	/**
	 * Allows to convert the edge in its .dot file format
	 * 
	 * @return the edge in its .dot file format
	 */
	public String toDot() {
		return this.repeater.getName().substring(1) + " -> " + this.original.getName().substring(1) + ";\n";
	}

	@Override
	public int compareTo(Object o) {
		Edge link = (Edge) o;
		if (this.equals(link)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.repeater == null) ? 0 : this.repeater.hashCode());
		result = prime * result + ((this.original == null) ? 0 : this.original.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return this.repeater().equals(other.repeater()) && this.original().equals(other.original());
	}

}
