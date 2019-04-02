package com.tweetcrawl.agents.utils;

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import jade.util.Logger;

/**
 * Logger used by the project to log messages, errors and making popups if
 * necessary to inform the user.
 */
public class TweetCrawlerLogger implements Serializable {

	private static final long serialVersionUID = 1L;
	private ImageIcon image;
	private Logger logger;

	/**
	 * Constructor of the {@code TweetCrawlerLogger}.
	 * 
	 * @param name the name of the logger
	 */
	public TweetCrawlerLogger(String name) {
		this.logger = Logger.getMyLogger(name);
		this.image = new ImageIcon("./img/twitter-logo-vector-png-clipart-1.png");
	}

	/**
	 * Allows to log an informative message in the {@code TweetCrawlerLogger}.
	 * 
	 * @param message the message to log
	 */
	public void info(String message) {
		this.logger.info(message);
	}

	/**
	 * Allows to log an informative message in the {@code TweetCrawlerLogger} and to
	 * display it.
	 * 
	 * @param message     the message to log and display
	 * @param title       the title to give to the display popup
	 * @param canBeClosed {@code true} if the popup can be closed by the user,
	 *                    {@code false} otherwise
	 * @return {@code JDialog} as the popup used to display the message that can be
	 *         used to close programmatically the popup
	 */
	public JDialog info(String message, String title, boolean canBeClosed) {
		this.logger.info(message);
		JDialog dialog = new JDialog();
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
				null, new Object[] {}, null);
		dialog.setTitle(title);
		dialog.setIconImage(this.image.getImage());
		dialog.setModal(false);
		dialog.setContentPane(optionPane);
		if (canBeClosed) {
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		} else {
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		}
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		return dialog;
	}

	/**
	 * Allows to log an error message in the {@code TweetCrawlerLogger} and to
	 * display it.
	 * 
	 * @param message the message to log and display
	 */
	public void severe(String message) {
		this.logger.severe(message);
		JDialog dialog = new JDialog();
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
				new Object[] {}, null);
		dialog.setTitle("Error");
		dialog.setIconImage(this.image.getImage());
		dialog.setModal(false);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	/**
	 * Allows to log a warning message in the {@code TweetCrawlerLogger} and to
	 * display it.
	 * 
	 * @param message the message to log and display
	 */
	public void warning(String message) {
		this.logger.severe(message);
		JDialog dialog = new JDialog();
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
				new Object[] {}, null);
		dialog.setTitle("Warning");
		dialog.setIconImage(this.image.getImage());
		dialog.setModal(false);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

}
