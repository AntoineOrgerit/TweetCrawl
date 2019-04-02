package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.util.Logger;

/**
 * Ontology used when informing about a new quote to a
 * {@code QuoteGraphGenerator} agent.
 */
public class QuoteOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "Quote-transmission-ontology";

	public static final String QUOTE = "Quote";
	public static final String QUOTE_TERM = "term";
	public static final String QUOTE_REPEATER = "repeater";
	public static final String QUOTE_ORIGINAL = "original";

	private static Ontology instance = new QuoteOntology();

	/**
	 * Allows to get the instance of the ontology.
	 * 
	 * @return {@code Ontology} as instance of the {@code QuoteOntology} ontology
	 */
	public static Ontology getInstance() {
		return instance;
	}

	/**
	 * Private constructor of the ontology.
	 */
	private QuoteOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			this.add(new PredicateSchema(QUOTE), Quote.class);
			PredicateSchema cs = (PredicateSchema) this.getSchema(QUOTE);
			cs.add(QUOTE_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
			cs.add(QUOTE_REPEATER, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
			cs.add(QUOTE_ORIGINAL, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException oe) {
			logger.severe("Exception during generation of Quote-transmission-ontology ontology : " + oe);
		}
	}

}
