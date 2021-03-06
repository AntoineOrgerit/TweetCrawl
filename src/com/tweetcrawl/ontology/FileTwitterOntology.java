package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

import com.tweetcrawl.agents.utils.TweetCrawlerLogger;

/**
 * Ontology used when informing about a new available file to a
 * {@code Processor} agent.
 */
public class FileTwitterOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "FileName_ontology";

	public static final String FILE = "File";
	public static final String TERM = "term";

	private static Ontology instance = new FileTwitterOntology();

	/**
	 * Allows to get the instance of the ontology.
	 * 
	 * @return {@code Ontology} as instance of the {@code FileTwitterOntology}
	 *         ontology
	 */
	public static Ontology getInstance() {
		return instance;
	}

	/**
	 * Private constructor of the ontology.
	 */
	private FileTwitterOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			this.add(new PredicateSchema(FILE), FileTwitter.class);
			PredicateSchema ps = (PredicateSchema) this.getSchema(FILE);
			ps.add(TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING));
		} catch (OntologyException oe) {
			logger.severe("Exception during generation of the ontology FileTwitterOntology: " + oe);
		}
	}

}
