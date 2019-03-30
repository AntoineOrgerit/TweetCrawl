package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import jade.util.Logger;

/**
 * Ontology used when informing about a treatement to the QuoteGraphGenerator
 * agent
 */
public class QuotesActionOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "QuotesAction-ontology";

	public static final String QUOTESACTION = "QuotesAction";
	public static final String QUOTESACTION_TERM = "term";
	public static final String QUOTESACTION_ACTION = "action";

	private static Ontology instance = new QuotesActionOntology();

	/**
	 * Allows to get an instance of the ontology
	 * 
	 * @return An instance of the ontology as an <i>Ontology</i> object
	 */
	public static Ontology getInstance() {
		return instance;
	}

	/**
	 * Private constructor for the ontology.
	 */
	private QuotesActionOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			this.add(new PredicateSchema(QUOTESACTION), QuotesAction.class);
			PredicateSchema ps = (PredicateSchema) this.getSchema(QUOTESACTION);
			ps.add(QUOTESACTION_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
			ps.add(QUOTESACTION_ACTION, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException oe) {
			logger.severe("Exception during generation of Quote-transmission-ontology ontology : " + oe);
		}
	}

}
