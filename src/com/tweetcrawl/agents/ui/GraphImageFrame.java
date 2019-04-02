package com.tweetcrawl.agents.ui;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

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
		ImageIcon imageIcon = new ImageIcon("./img/twitter-logo-vector-png-clipart-1.png");
			ImageIcon ii = new ImageIcon("./visualisation/tweets_" + term + ".png");
			this.getContentPane().add(new JScrollPane(new JLabel(ii)));
			this.setTitle("Retweets linked to the term " + term);
			this.setIconImage(imageIcon.getImage());
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);

	}

}
