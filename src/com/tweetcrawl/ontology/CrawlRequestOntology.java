package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.util.Logger;

/**
 * Représente l'ontologie utilisée lors de la demande de crawl au TweetCrawler.
 */
public class CrawlRequestOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getMyLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "Crawl-request-ontology";

	public static final String CRAWL = "Crawl";
	public static final String CRAWL_TERM = "term";

	private static Ontology instance = new CrawlRequestOntology();

	/**
	 * Permets d'obtenir une instance de l'ontologie.
	 * 
	 * @return Une instance de l'ontologie sous la forme d'une Ontology.
	 */
	public static Ontology getInstance() {
		return instance;
	}

	/**
	 * Constructeur privé de l'ontologie.
	 */
	private CrawlRequestOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			// liaison du prédicat utilisé
			this.add(new AgentActionSchema(CRAWL), Crawl.class);
			AgentActionSchema ps = (AgentActionSchema) this.getSchema(CRAWL);
			ps.add(CRAWL_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException oe) {
			logger.severe("Exception durant la génération de l'ontologie Crawl-request-ontology : " + oe);
		}
	}

}
