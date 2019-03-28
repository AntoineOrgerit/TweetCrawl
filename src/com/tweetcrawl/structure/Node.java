package com.tweetcrawl.structure;

import java.util.Set;
import java.util.TreeSet;
public class Node implements Comparable<Object> {
	
	private String name;
	private TreeSet<Edge> links;
	
	public Node(String name) {
		this.name = name;
		this.links = new TreeSet<>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Set<Edge> getLinks() {
		return this.links;
	}
	
	public boolean addLink(Edge link) {
		return this.links.add(link);
	}
	
	public boolean containsLink(Edge link) {
		return this.links.contains(link);
	}
	
	public String toDot() {
		return this.name + "\n";
	}

	@Override
	public int compareTo(Object o) {
		Node node = (Node) o;
		if(this.equals(node)) {
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
