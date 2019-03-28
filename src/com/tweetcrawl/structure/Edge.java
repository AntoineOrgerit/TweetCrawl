package com.tweetcrawl.structure;

public class Edge {
	
	private Node repeater;
	private Node original;
	
	public Edge(Node repeater, Node original) {
		this.repeater = repeater;
		this.original = original;
	}
	
	public Node repeater() {
		return this.repeater;
	}

	public Node original() {
		return this.original;
	}
	
	public String toDot() {
		return this.repeater.getName() + "--" + this.original.getName();
	}
	
}
