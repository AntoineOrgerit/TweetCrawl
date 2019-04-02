package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.util.Logger;

/**
 * Ontology used when requesting a crawl action to a {@code TweetCrawler} agent.
 */
public class CrawlOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "Crawl-request-ontology";

	public static final String CRAWL = "Crawl";
	public static final String CRAWL_TERM = "term";

	private static Ontology instance = new CrawlOntology();

	/**
	 * Allows to get the instance of the ontology.
	 * 
	 * @return {@code Ontology} as instance of the {@code CrawlOntology} ontology
	 */
	public static Ontology getInstance() {
		return instance;
	}

	/**
	 * Private constructor of the ontology.
	 */
	private CrawlOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			this.add(new AgentActionSchema(CRAWL), Crawl.class);
			AgentActionSchema ps = (AgentActionSchema) this.getSchema(CRAWL);
			ps.add(CRAWL_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException oe) {
			logger.severe("Exception during generation of the ontology Crawl-request-ontology : " + oe);
		}
	}

}
