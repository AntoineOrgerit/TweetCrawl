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
 * Repr�sente le comportement de l'agent TweetCrawler.
 */
public class TweetCrawlerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Logger logger;

	/**
	 * Constructeur du comportement.
	 * 
	 * @param agent  - l'agent TweetCrawler auquel li� le comportement
	 * @param logger - le logger � utiliser pour les messages d'erreurs
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
	 * Permets d'extraire le terme � rechercher � partir des messages re�us.
	 * 
	 * @return Le terme � rechercher sous la forme d'un String.
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
				logger.severe("Exception durant la r�ception du terme sur TweetCrawlerAgent : " + e);
			}
		}
		return term;
	}

	/**
	 * Permets de g�n�rer la requ�te vers l'API de Twitter.
	 * 
	 * @param term - le terme � rechercher par le biais de la requ�te sous la forme
	 *             d'un String
	 * @return La requ�te � ex�cuter sous la forme d'une Query.
	 */
	private Query generateQuery(String term) {
		Query query = new Query(term);
		query.setSince(this.getDateFromXDays(10));
		query.setCount(100);
		return query;
	}

	/**
	 * Permets d'obtenir la date d'il y a X jours.
	 * 
	 * @param days - le nombre X de jours pr�c�dant la date actuelle sous la forme
	 *             d'un entier
	 * @return La date d'il y a X jours sous la forme d'un String formatt�
	 *         YYYY-MM-dd.
	 */
	private String getDateFromXDays(int days) {
		LocalDate date = LocalDate.now().minusDays(days);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		return date.format(formatter);
	}

	/**
	 * Permets d'ex�cuter la requ�te vers l'API de Twitter.
	 * 
	 * @param query - la requ�te � ex�cuter sour la forme d'une Query
	 * @return Le r�sultat de la requ�te sous la forme d'un QueryResult.
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
			logger.severe("Exception durant l'obtention des tweets sur TweetCrawlerAgent : " + e);
		}
		return result;
	}

	/**
	 * Permets de stocker les r�sultats de la recherche par terme dans un fichier
	 * correspondant.
	 * 
	 * @param tweets - les tweets r�sultant de la requ�te sous la forme d'un
	 *               QueryResult
	 * @param term   - le terme ayant �t� recherch� sous la forme d'un String,
	 *               utilis� pour le nom de fichier
	 */
	private void storeTweets(QueryResult tweets, String term) {
		File fout = new File("./data/tweets_" + term + ".txt");
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout))))) {
			for (Status tweet : tweets.getTweets()) {
				pw.println("{datetime:'" + tweet.getCreatedAt() + "', from:'" + tweet.getUser().getScreenName()
						+ "', content:'" + tweet.getText().replaceAll("[\\t\\n\\r]+", " ") + "'}");
			}
		} catch (IOException e) {
			logger.severe("Exception durant la sauvegarde des tweets sur TweetCrawlerAgent : " + e);
		}
	}

}
