package com.tweetcrawl.agents.ui;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.tweetcrawl.agents.utils.TweetCrawlerLogger;

/**
 * JFrame containing a GraphImageComponent
 */
public class GraphImageFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the frame
	 * 
	 * @param term   the term associated with the frame
	 * @param logger logger used to display errors
	 */
	public GraphImageFrame(String term, TweetCrawlerLogger logger) {
		GraphImageComponent component;
		ImageIcon imageIcon = new ImageIcon("./img/twitter-logo-vector-png-clipart-1.png");
		try {
			component = new GraphImageComponent(term);
			this.add(component);
			this.setTitle("Retweets linked to the term " + term);
			this.setIconImage(imageIcon.getImage());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		} catch (IOException e) {
			logger.severe("Exception during .png display on QuoteGraphGenerator: " + e);
		}

	}

}
