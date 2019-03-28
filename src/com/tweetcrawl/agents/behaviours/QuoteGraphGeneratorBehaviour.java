package com.tweetcrawl.agents.behaviours;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.tweetcrawl.ontology.Quote;
import com.tweetcrawl.ontology.QuotesAction;
import com.tweetcrawl.structure.Node;
import com.tweetcrawl.structure.QuoteGraph;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class QuoteGraphGeneratorBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Logger logger;
	
	private Map<String, QuoteGraph> graphsContruction;
	
	public QuoteGraphGeneratorBehaviour(Agent agent, Logger logger) {
		super(agent);
		this.logger = logger;
		this.graphsContruction = new HashMap<>();
	}
	
	@Override
	public void action() {
		ContentElement element = this.getContentOfMessage();
		if(element != null) {
			if(element instanceof QuotesAction) {
				this.treatQuotesAction((QuotesAction) element);
			} else {
				if(element instanceof Quote) {
					this.addQuoteToGraph((Quote) element);
				}
			}
		}
	}
	
	private ContentElement getContentOfMessage() {
		ACLMessage msg = myAgent.receive();
		ContentElement element = null;
		if (msg != null) {
			try {
				ContentManager cm = myAgent.getContentManager();
				element = cm.extractContent(msg);
			} catch (CodecException | OntologyException e) {
				logger.severe("Exception during term reception on QuoteGraphGenerator : " + e);
			}
		}
		return element;
	}
	
	private void treatQuotesAction(QuotesAction quotesAction) {
		if(quotesAction.getAction().equals("start")) {
			this.createQuoteGraphIfNotExists(quotesAction.getTerm());
		} else {
			if(quotesAction.getAction().equals("end")) {
				this.removeAgentFromQuoteGraph(quotesAction.getTerm());
			}
		}
	}
	
	private void createQuoteGraphIfNotExists(String term) {
		QuoteGraph graph;
		if(this.graphsContruction.containsKey(term)) {
			graph = this.graphsContruction.get(term);
			graph.addAgent();
			this.graphsContruction.put(term, graph);
		} else {
			graph = new QuoteGraph(term);
			this.graphsContruction.put(term, graph);
		}
	}
	
	private void removeAgentFromQuoteGraph(String term) {
		QuoteGraph graph = this.graphsContruction.get(term);
		int totalAgent = graph.removeAgent();
		if(totalAgent == 0) {
			try {
				graph.toDot();
			} catch (IOException e) {
				logger.severe("Exception during .dot file generation on QuoteGraphGenerator : " + e);
			}
			this.graphsContruction.remove(term);
		}
	}
	
	private void addQuoteToGraph(Quote quote) {
		QuoteGraph graph = this.graphsContruction.get(quote.getTerm());
		Node repeater = new Node(quote.getRepeater());
		// to continue
	}
	
}
