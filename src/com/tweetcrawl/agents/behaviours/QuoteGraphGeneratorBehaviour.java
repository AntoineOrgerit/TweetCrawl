package com.tweetcrawl.agents.behaviours;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.tweetcrawl.agents.ui.GraphImageFrame;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.ProcessorAction;
import com.tweetcrawl.ontology.Quote;
import com.tweetcrawl.structure.Edge;
import com.tweetcrawl.structure.Node;
import com.tweetcrawl.structure.QuoteGraph;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Behaviour of the {@code QuoteGraphGenerator} agent.
 */
public class QuoteGraphGeneratorBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger;

	private transient Map<String, QuoteGraph> graphsContruction;

	/**
	 * Constructor of the behaviour.
	 * 
	 * @param agent  the {@code QuoteGraphGenerator} agent using the behaviour
	 * @param logger the {@code TweetCrawlerLogger} used to display errors
	 *               encountered
	 */
	public QuoteGraphGeneratorBehaviour(Agent agent, TweetCrawlerLogger logger) {
		super(agent);
		this.logger = logger;
		this.graphsContruction = new HashMap<>();
	}

	@Override
	public void action() {
		ContentElement element = this.getContentOfMessage();
		if (element != null) {
			if (element instanceof ProcessorAction) {
				this.treatQuotesAction((ProcessorAction) element);
			} else {
				if (element instanceof Quote) {
					this.addQuoteToGraph((Quote) element);
				}
			}
		} else {
			this.block();
		}
	}

	/**
	 * Allows to retrieve the content of a message.
	 * 
	 * @return the {@code ContentElement} of a message
	 */
	private ContentElement getContentOfMessage() {
		ACLMessage msg = myAgent.receive();
		ContentElement element = null;
		if (msg != null) {
			try {
				ContentManager cm = myAgent.getContentManager();
				element = cm.extractContent(msg);
			} catch (CodecException | OntologyException e) {
				logger.severe("Exception during term reception on QuoteGraphGenerator: " + e);
			}
		}
		return element;
	}

	/**
	 * Allows to treat {@code ProcessorAction} messages content.
	 * 
	 * @param processorAction the {@code ProcessorAction} to treat
	 */
	private void treatQuotesAction(ProcessorAction processorAction) {
		if (processorAction.getAction().equals("begin")) {
			this.createQuoteGraphIfNotExists(processorAction.getTerm());
		} else {
			if (processorAction.getAction().equals("end")) {
				this.removeAgentFromQuoteGraph(processorAction.getTerm());
			}
		}
	}

	/**
	 * Allows to create a {@code QuoteGraph} graph if it doesn't exists and to
	 * update the number of agents working on a graph.
	 * 
	 * @param term the term associated to the graph
	 */
	private void createQuoteGraphIfNotExists(String term) {
		QuoteGraph graph;
		if (this.graphsContruction.containsKey(term)) {
			graph = this.graphsContruction.get(term);
			graph.addAgent();
			this.graphsContruction.put(term, graph);
		} else {
			graph = new QuoteGraph(term);
			this.graphsContruction.put(term, graph);
		}
	}

	/**
	 * Allows to decrease the number of agents working on a {@code QuoteGraph}
	 * graph. It will also convert the graph to a .dot file and generate a view of
	 * the corresponding .png file if no more agents are working on the graph.
	 * 
	 * @param term the term associated to the graph
	 */
	private void removeAgentFromQuoteGraph(String term) {
		QuoteGraph graph = this.graphsContruction.get(term);
		int totalAgent = graph.removeAgent();
		if (totalAgent == 0) {
			try {
				graph.toDot();
				if (convertDotToPng(term)) {
					new GraphImageFrame(term);
				} else {
					logger.warning(
							"Unable to display the .dot file. You can find the .dot file of the graph under the visualisation subdirectory.");
				}
			} catch (IOException e) {
				logger.severe("Exception during .dot file generation on QuoteGraphGenerator: " + e);
			}
			this.graphsContruction.remove(term);
		}
	}

	/**
	 * Allows to convert the .dot file to a .png file.
	 * 
	 * @param term the term associated with the files
	 * @return {@code true} if the conversion is a success, {@code false} otherwise
	 */
	private boolean convertDotToPng(String term) {
		try {
			ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", "./visualisation/tweets_" + term + ".dot",
					"-o./visualisation/tweets_" + term + ".png");
			Process p = pb.start();
			return p.waitFor() == 0;
		} catch (IOException | InterruptedException e) {
			logger.severe("Exception during .dot file conversion to .png file on QuoteGraphGenerator : " + e);
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * Allows to add a quote to an existing {@code QuoteGraph} graph.
	 * 
	 * @param quote the quote to add to its corresponding graph
	 */
	private void addQuoteToGraph(Quote quote) {
		QuoteGraph graph = this.graphsContruction.get(quote.getTerm());
		Node repeater;
		Node original;
		if (graph.containsNode(quote.getRepeater())) {
			repeater = graph.getNode(quote.getRepeater());
		} else {
			repeater = new Node(quote.getRepeater());
			graph.addNode(repeater);
		}
		if (graph.containsNode(quote.getOriginal())) {
			original = graph.getNode(quote.getOriginal());
		} else {
			original = new Node(quote.getOriginal());
			graph.addNode(original);
		}
		Edge link = new Edge(repeater, original);
		graph.addLink(link);
		this.graphsContruction.put(quote.getTerm(), graph);
	}

}
