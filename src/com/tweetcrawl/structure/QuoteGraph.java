package com.tweetcrawl.structure;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class QuoteGraph {
	
	private String name;
	private TreeSet<Node> treeSet;
	
	private int currentAgentsNumber;
	
	public QuoteGraph(String name) {
		this.name = name;
		this.treeSet = new TreeSet<>();
		this.currentAgentsNumber = 1;
	}
	
	public void addAgent() {
		this.currentAgentsNumber++;
	}
	
	public int removeAgent() {
		return --this.currentAgentsNumber;
	}
	
	public boolean addNode(Node node) {
		return this.treeSet.add(node);
	}
	
	public boolean containsNode(String name) {
		return this.treeSet.contains(new Node(name));
	}
	
	public Node getNode(String name) {
		if (this.containsNode(name)) {
			for (Iterator<Node> I = this.treeSet.iterator(); I.hasNext();) {
				Node node = I.next();
				if (node.getName().equals(name)) {
					return node;
				}
			}
		}
		return null;
	}
	
	public boolean containsLink(String repeater, String original) {
		if (this.containsNode(repeater) && this.containsNode(original)) {
			Node from = this.getNode(repeater);
			Node to = this.getNode(original);
			return from.containsLink(new Edge(from, to));
		}
		return false;
	}
	
	public boolean addLink(Edge link) {
		String repeater = link.repeater().getName();
		String original = link.original().getName();
		if (this.containsNode(repeater) && !this.containsLink(repeater, original)) {
			link.repeater().addLink(link);
			return true;
		}
		return false;
	}
	
	public void toDot() throws IOException {
		String filename = "tweets_" + this.name + ".dot";
		try (FileOutputStream fich = new FileOutputStream(new File("./visualisation/" + filename));
				DataOutputStream out = new DataOutputStream(fich);) {
			out.writeBytes("digraph " + this.name + " {\n");
			StringBuilder nodes = new StringBuilder();
			StringBuilder links = new StringBuilder();
			// parcours de l'ensemble de Node
			for (Iterator<Node> I = treeSet.iterator(); I.hasNext();) {
				Node node = I.next();
				nodes.append(" " + node.toDot());
				TreeSet<Edge> succ = (TreeSet<Edge>) node.getLinks();
				// parcours de l'ensemble des successeurs de N
				for (Iterator<Edge> J = succ.iterator(); J.hasNext();) {
					Edge link = J.next();
					links.append(" " + link.toDot());
				}
			}
			out.writeBytes(nodes.toString());
			out.writeBytes(links.toString());
			out.writeBytes("}");
		} catch (Exception e) {
			throw new IOException("Exception encountered while writing file " + filename + ".");
		}
	}

}
