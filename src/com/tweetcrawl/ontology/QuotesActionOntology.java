package com.tweetcrawl.ontology;

import com.tweetcrawl.agents.utils.TweetCrawlerLogger;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

/**
 * Ontology used when informing about a current state of treatment of a
 * {@code Processor} agent.
 */
public class QuotesActionOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "QuotesAction-ontology";

	public static final String QUOTESACTION = "QuotesAction";
	public static final String QUOTESACTION_TERM = "term";
	public static final String QUOTESACTION_ACTION = "action";

	private static Ontology instance = new QuotesActionOntology();

	/**
	 * Allows to get the instance of the ontology.
	 * 
	 * @return {@code Ontology} as instance of the {@code QuotesActionOntology}
	 *         ontology
	 */
	public static Ontology getInstance() {
		return instance;
	}

	/**
	 * Private constructor of the ontology.
	 */
	private QuotesActionOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			this.add(new PredicateSchema(QUOTESACTION), QuotesAction.class);
			PredicateSchema ps = (PredicateSchema) this.getSchema(QUOTESACTION);
			ps.add(QUOTESACTION_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
			ps.add(QUOTESACTION_ACTION, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException oe) {
			logger.severe("Exception during generation of the ontology QuotesActionOntology: " + oe);
		}
	}

}
