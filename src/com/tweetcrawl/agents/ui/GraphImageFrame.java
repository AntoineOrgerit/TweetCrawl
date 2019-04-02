package com.tweetcrawl.agents.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Frame used to display generated graphs from the {@code QuoteGraphGenerator}
 * agent.
 */
public class GraphImageFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the frame.
	 * 
	 * @param term the term associated with the graph that has to be displayed
	 */
	public GraphImageFrame(String term) {
		ImageIcon icon = new ImageIcon("./img/twitter-logo-vector-png-clipart-1.png");
		ImageIcon content = new ImageIcon("./visualisation/tweets_" + term + ".png");
		this.getContentPane().add(new JScrollPane(new JLabel(content)));
		this.setTitle("Retweets linked to the term " + term);
		this.setIconImage(icon.getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
