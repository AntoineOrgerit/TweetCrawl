package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.util.Logger;

/**
 * Ontology used when requesting a crawl action to the TweetCrawler
 */
public class CrawlRequestOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "Crawl-request-ontology";

	public static final String CRAWL = "Crawl";
	public static final String CRAWL_TERM = "term";

	private static Ontology instance = new CrawlRequestOntology();

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
	private CrawlRequestOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			// Binding of the used predicate
			this.add(new AgentActionSchema(CRAWL), Crawl.class);
			AgentActionSchema ps = (AgentActionSchema) this.getSchema(CRAWL);
			ps.add(CRAWL_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException oe) {
			logger.severe("Exception during generation of the ontology Crawl-request-ontology : " + oe);
		}
	}

}
