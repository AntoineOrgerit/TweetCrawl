package com.tweetcrawl.ontology;

import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

/**
 * Ontology used when informing about a new cloud to a
 * {@code CloudGenerator} agent.
 */
public class CloudOntology extends Ontology {

    private static final long serialVersionUID = 1L;
    private TweetCrawlerLogger logger = new TweetCrawlerLogger(this.getClass().getName());

    public static final String ONTOLOGY_NAME = "Cloud-transmission-ontology";

    public static final String CLOUD = "Cloud";
    public static final String CLOUD_TERM = "term";
    public static final String CLOUD_DATE = "date";
    public static final String CLOUD_WORD = "word";

    private static Ontology instance = new CloudOntology();

    /**
     * Allows to get the instance of the ontology.
     *
     * @return {@code Ontology} as instance of the {@code CloudOntology} ontology
     */
    public static Ontology getInstance(){
        return instance;
    }

    /**
     * Private constructor of the ontology.
     */
    private CloudOntology(){
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            this.add(new PredicateSchema(CLOUD), Quote.class);
            PredicateSchema cs = (PredicateSchema) this.getSchema(CLOUD);
            cs.add(CLOUD_TERM, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            cs.add(CLOUD_DATE, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            cs.add(CLOUD_WORD, (PrimitiveSchema) this.getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
        } catch (OntologyException oe) {
            logger.severe("Exception during generation of the ontology CloudOntology: " + oe);
        }
    }

}
