package com.tweetcrawl.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

import java.util.logging.Logger;

/**
 * Ontology used when informing about a new available file to a
 * {@code Processor} agent.
 */
public class FileTwitterOntology extends Ontology {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass().getName());

	public static final String ONTOLOGY_NAME = "FileName_ontology";

	public static final String FILE = "File";
	public static final String NAME = "name";

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
			ps.add(NAME, (PrimitiveSchema) this.getSchema(BasicOntology.STRING));
		} catch (OntologyException oe) {
			log.severe("Exception during generation of the ontology !");
		}
	}

}
