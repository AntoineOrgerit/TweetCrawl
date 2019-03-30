package com.tweetcrawl.structure;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Represents a graph of quotes as retweets
 */
public class QuoteGraph {

	private String name;
	private TreeSet<Node> treeSet;

	private int currentAgentsNumber;

	/**
	 * Constructor of the graph
	 * 
	 * @param name the name of the graph as the term of tweets searched
	 */
	public QuoteGraph(String name) {
		this.name = name;
		this.treeSet = new TreeSet<>();
		this.currentAgentsNumber = 1;
	}

	/**
	 * Allows to increase the number of agent working on the graph
	 */
	public void addAgent() {
		this.currentAgentsNumber++;
	}

	/**
	 * Allows to decrease the number of agent working on the graph
	 * 
	 * @return the current number of agent working on the graph
	 */
	public int removeAgent() {
		return --this.currentAgentsNumber;
	}

	/**
	 * Allows to add a node to the graph
	 * 
	 * @param node the node to add to the graph
	 * @return true if the node has been added, false if it already exists
	 */
	public boolean addNode(Node node) {
		return this.treeSet.add(node);
	}

	/**
	 * Allows to check if a node exists in the graph
	 * 
	 * @param name the name of the node to check
	 * @return true if it exists in the graph, false if not
	 */
	public boolean containsNode(String name) {
		return this.treeSet.contains(new Node(name));
	}

	/**
	 * Allows to get a node in the graph
	 * 
	 * @param name the name of the graph to look for
	 * @return the node if it exists in the graph, null if not
	 */
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

	/**
	 * Allows to check if the graph contains a link between to nodes
	 * 
	 * @param repeater the repater of the retweet link
	 * @param original the original of the retweet link
	 * @return true if the graph contains the link, false if not
	 */
	public boolean containsLink(String repeater, String original) {
		if (this.containsNode(repeater) && this.containsNode(original)) {
			Node from = this.getNode(repeater);
			Node to = this.getNode(original);
			return from.containsLink(new Edge(from, to));
		}
		return false;
	}

	/**
	 * Allows to add a link to the graph
	 * 
	 * @param link the link to add to the graph
	 */
	public void addLink(Edge link) {
		link.repeater().addLink(link);
	}

	/**
	 * Allows to generate a .dot file of the graph in the subdirectory visualisation
	 * 
	 * @throws IOException if an exception is encountered while generating the file
	 */
	public void toDot() throws IOException {
		String filename = "tweets_" + this.name + ".dot";
		try (FileOutputStream fich = new FileOutputStream(new File("./visualisation/" + filename));
				DataOutputStream out = new DataOutputStream(fich);) {
			out.writeBytes("digraph \"" + this.name + "\" {\n");
			StringBuilder nodes = new StringBuilder();
			StringBuilder links = new StringBuilder();
			// parcours de l'ensemble de Node
			for (Iterator<Node> I = treeSet.iterator(); I.hasNext();) {
				Node node = I.next();
				nodes.append("\t" + node.toDot());
				TreeSet<Edge> succ = (TreeSet<Edge>) node.getLinks();
				// parcours de l'ensemble des successeurs de N
				for (Iterator<Edge> J = succ.iterator(); J.hasNext();) {
					Edge link = J.next();
					links.append("\t" + link.toDot());
				}
			}
			out.writeBytes(nodes.toString());
			out.writeBytes(links.toString());
			out.writeBytes(this.getLegend());
			out.writeBytes("}");
		} catch (Exception e) {
			throw new IOException("Exception encountered while writing file " + filename + ": " + e);
		}
	}

	/**
	 * Allows to generate the legend of the graph in its .dot format
	 * 
	 * @return the legend of the graph in its .dot format
	 */
	private String getLegend() {
		return "\tsubgraph cluster1 {\n" + "\t\tlabel=\"Légende\";\n" + "\t\tshape=rectangle;\n" + "\t\tcolor=black;\n"
				+ "\t\ta [style=invis];\n" + "\t\tb [style=invis];\n" + "\t\ta -> b[label=\"  a retweeté  \"] ;\n"
				+ "\t}\n";
	}

}
