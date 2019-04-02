package com.tweetcrawl.agents.behaviours;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.tweetcrawl.agents.utils.DFServiceManager;
import com.tweetcrawl.agents.utils.TweetCrawlerLogger;
import com.tweetcrawl.ontology.Crawl;
import com.tweetcrawl.ontology.FileTwitter;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Behaviour of the {@code TweetCrawler} agent.
 */
public class TweetCrawlerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private TweetCrawlerLogger logger;
	private Codec codec;
	private Ontology fileOntology;

	/**
	 * Constructor of the behaviour.
	 * 
	 * @param agent  the {@code TweetCrawler} agent using the behaviour
	 * @param logger the {@code TweetCrawlerLogger} used to display errors encountered
	 * @param codec the {@code Codec} used to communicate with the {@code Processor} agents
	 * @param fileOntology the {@code Ontology} used to communicate with the {@code Processor} agents
	 */
	public TweetCrawlerBehaviour(Agent agent, TweetCrawlerLogger logger, Codec codec, Ontology fileOntology) {
		super(agent);
		this.logger = logger;
		this.codec = codec;
		this.fileOntology = fileOntology;
	}

	@Override
	public void action() {
		String term = this.getTermToCrawl();
		if (!term.equals("")) {
			Query query = this.generateQuery(term);
			QueryResult tweets = this.executeQuery(query);
			if (tweets != null) {
				this.storeTweets(tweets, term);
				this.sendMessageToProcessorAgents(term);
			}
		} else {
			this.block();
		}
	}

	/**
	 * Allows to extract the term to crawl on Twitter API from received messages.
	 *
	 * @return the term to crawl
	 */
	private String getTermToCrawl() {
		ACLMessage msg = myAgent.receive();
		String term = "";
		if (msg != null) {
			try {
				ContentManager cm = myAgent.getContentManager();
				Action action = (Action) cm.extractContent(msg);
				Crawl crawl = (Crawl) action.getAction();
				term = crawl.getTerm();
			} catch (CodecException | OntologyException e) {
				logger.severe("Exception while searching for the term on TweetCrawlerAgent : " + e);
			}
		}
		return term;
	}

	/**
	 * Allows to generate the request to send to Twitter API.
	 * 
	 * @param term the term to be searched through the request
	 * @return a {@code Query} containing the request to be executed
	 */
	private Query generateQuery(String term) {
		Query query = new Query(term);
		query.setSince(this.getDateFromXDays(10));
		query.setCount(100);
		return query;
	}

	/**
	 * Allows to get the date from X days ago.
	 * 
	 * @param days the number X of days
	 * @return the date X days ago, formated as {@code YYYY-MM-dd}
	 */
	private String getDateFromXDays(int days) {
		LocalDate date = LocalDate.now().minusDays(days);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		return date.format(formatter);
	}

	/**
	 * Allows to execute a request on Twitter API.
	 *
	 * @param query the {@code Query} to be executed
	 * @return the {@code QueryResult} of the request
	 */
	private QueryResult executeQuery(Query query) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			logger.severe("Exception while requesting tweets on TweetCrawlerAgent : " + e);
		}
		if(result != null && result.getCount() != 0) {
			return result;
		} else {
			return null;
		}
	}

	/**
	 * Allows to store the results of the request in a .txt file.
	 * 
	 * @param tweets {@code QueryResult} containing the result of the request
	 * @param term   the crawled term, used to name the file
	 */
	private void storeTweets(QueryResult tweets, String term) {
		File fout = new File("./data/tweets_" + term + ".txt");
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout), StandardCharsets.UTF_8)))) {
			for (Status tweet : tweets.getTweets()) {
				pw.println("{datetime:'" + tweet.getCreatedAt() + "', from:'@" + tweet.getUser().getScreenName()
						+ "', content:'" + tweet.getText().replaceAll("[\\t\\n\\r]+", " ") + "'}");
			}
		} catch (IOException e) {
			logger.severe("Exception while storing the tweets from TweetCrawlerAgent : " + e);
		}
	}
	
	/**
	 * Allows to send a message to the {@code Processor} agents.
	 * 
	 * @param term the term to send with the message
	 */
	private void sendMessageToProcessorAgents(String term) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		for(DFAgentDescription dfad : DFServiceManager.getAgentsForService(myAgent, "ParseFile-service")) {
			msg.addReceiver(dfad.getName());
		}
		msg.setLanguage(this.codec.getName());
		msg.setOntology(this.fileOntology.getName());
		FileTwitter fileTwitter = new FileTwitter();
		fileTwitter.setTerm(term);
		try {
			myAgent.getContentManager().fillContent(msg, fileTwitter);
			myAgent.send(msg);
		} catch (CodecException | OntologyException e) {
			logger.severe("Exception while sending the term to the Treatment agents : " + e);
		}
	}

}
