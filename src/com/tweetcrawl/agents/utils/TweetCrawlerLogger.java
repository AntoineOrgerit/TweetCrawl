package com.tweetcrawl.agents.utils;

import java.io.Serializable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import jade.util.Logger;

public class TweetCrawlerLogger implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger logger;

	public TweetCrawlerLogger(String name) {
		this.logger = Logger.getMyLogger(name);
	}

	public void info(String message) {
		this.logger.info(message);
	}

	public JDialog info(String message, String title, boolean canBeClosed) {
		this.logger.info(message);
		JDialog dialog = new JDialog();
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
				null, new Object[] {}, null);
		dialog.setTitle(title);
		dialog.setModal(false);
		dialog.setContentPane(optionPane);
		if (canBeClosed) {
			dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		} else {
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		}
		dialog.pack();
		dialog.setVisible(true);
		return dialog;
	}

	public void severe(String message) {
		this.logger.severe(message);
		JDialog dialog = new JDialog();
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
				new Object[] {}, null);
		dialog.setTitle("Error");
		dialog.setModal(false);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}

	public void warning(String message) {
		this.logger.severe(message);
		JDialog dialog = new JDialog();
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
				new Object[] {}, null);
		dialog.setTitle("Warning");
		dialog.setModal(false);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}

}
