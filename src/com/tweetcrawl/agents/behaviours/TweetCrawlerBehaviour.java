package com.tweetcrawl.agents.behaviours;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.tweetcrawl.ontology.Crawl;

import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Behaviour of the TweetCrawler agent
 */
public class TweetCrawlerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Logger logger;

	/**
	 * Constructor of the behaviour
	 * 
	 * @param agent  corresponding TweetCrawler agent
	 * @param logger logger used to display errors
	 */
	public TweetCrawlerBehaviour(Agent agent, Logger logger) {
		super(agent);
		this.logger = logger;
	}

	@Override
	public void action() {
		String term = this.getTermToCrawl();
		if (!term.equals("")) {
			Query query = this.generateQuery(term);
			QueryResult tweets = this.executeQuery(query);
			if (tweets != null) {
				this.storeTweets(tweets, term);
			}
		}
	}

	/**
	 * Allows to extract the term to search from received messages
	 *
<<<<<<< HEAD
	 * @return String containing the term to search
=======
	 * @return 		String containing the term to search
>>>>>>> 377c3e88d36e2d12aa2c89ab0eefc0d08f79bc8c
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
	 * Allows to generate the request to send to Twitter's API
	 * 
<<<<<<< HEAD
	 * @param term String containing the term to be searched through the request
	 * @return Query containing the request to be executed
=======
	 * @param term 		String containing the term to be searched through the request
	 * @return 		Query containing the request to be executed
>>>>>>> 377c3e88d36e2d12aa2c89ab0eefc0d08f79bc8c
	 */
	private Query generateQuery(String term) {
		Query query = new Query(term);
		query.setSince(this.getDateFromXDays(10));
		query.setCount(100);
		return query;
	}

	/**
	 * Allows to get the date X days ago
	 * 
<<<<<<< HEAD
	 * @param days Number X of days
	 * @return String containing the date X days ago, formated as YYYY-MM-dd
=======
	 * @param days 		Number X of days
	 * @return 		String containing the date X days ago, formated as YYYY-MM-dd
>>>>>>> 377c3e88d36e2d12aa2c89ab0eefc0d08f79bc8c
	 */
	private String getDateFromXDays(int days) {
		LocalDate date = LocalDate.now().minusDays(days);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		return date.format(formatter);
	}

	/**
	 * Allows to execute a request using Twitter's API
	 *
<<<<<<< HEAD
	 * @param query Query request to be executed
	 * @return QueryResult containing the result of the request
=======
	 * @param query 	Query request to be executed
	 * @return 		QueryResult containing the result of the request
>>>>>>> 377c3e88d36e2d12aa2c89ab0eefc0d08f79bc8c
	 */
	private QueryResult executeQuery(Query query) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("0CeZ8uh64y17IHQywEawEDywX")
				.setOAuthConsumerSecret("cZxzH3itDvcxHNc2IL1YkbXqd58TrBUUnjN10zrIWTj0fLUxMG").setOAuthAccessToken("")
				.setOAuthAccessTokenSecret("");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			logger.severe("Exception while requesting tweets on TweetCrawlerAgent : " + e);
		}
		return result;
	}

	/**
	 * Allows to store the results of the query in a file
	 * 
<<<<<<< HEAD
	 * @param tweets QueryResult containing the result of the request
	 * @param term   String containing the search term, used to name the file
=======
	 * @param tweets 	QueryResult containing the result of the request
	 * @param term   	String containing the search term, used to name the file
>>>>>>> 377c3e88d36e2d12aa2c89ab0eefc0d08f79bc8c
	 */
	private void storeTweets(QueryResult tweets, String term) {
		File fout = new File("./data/tweets_" + term + ".txt");
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout))))) {
			for (Status tweet : tweets.getTweets()) {
				pw.println("{datetime:'" + tweet.getCreatedAt() + "', from:'@" + tweet.getUser().getScreenName()
						+ "', content:'" + tweet.getText().replaceAll("[\\t\\n\\r]+", " ") + "'}");
			}
		} catch (IOException e) {
			logger.severe("Exception while storing the tweets from TweetCrawlerAgent : " + e);
		}
	}

}
