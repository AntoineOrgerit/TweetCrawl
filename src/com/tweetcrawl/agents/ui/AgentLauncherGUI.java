package com.tweetcrawl.agents.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tweetcrawl.agents.AgentLauncher;

import jade.gui.GuiEvent;

public class AgentLauncherGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private AgentLauncher agent;

	public static final int EXIT = 0;
	public static final int INPUT = 1;

	private JLabel label;
	private JTextField textField;
	private JButton button;

	public AgentLauncherGUI(AgentLauncher agent) {
		this.agent = agent;
		this.label = new JLabel("Rechercher sur Twitter :");
		this.textField = new JTextField(10);
		this.label.setLabelFor(this.textField);
		this.button = new JButton("Rechercher");
		this.button.addActionListener(this);
		GridBagLayout layout = new GridBagLayout();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 7, 7));
		panel.add(this.label);
		panel.add(this.textField);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(7, 7, 7, 7);
		contentPane.add(panel, c);
		c.gridx = 0;
		c.gridy = 1;
		contentPane.add(this.button, c);
		this.setContentPane(contentPane);
		this.setIconImage((new ImageIcon("./img/twitter-logo-vector-png-clipart-1.png")).getImage());
		this.setTitle("Twitter Crawler");
		this.pack();
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GuiEvent ge = new GuiEvent(this, AgentLauncherGUI.EXIT);
				agent.postGuiEvent(ge);
			}
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.button) {
			System.out.println("here");
			String input = this.textField.getText();
			GuiEvent ge = new GuiEvent(this, AgentLauncherGUI.INPUT);
			ge.addParameter(input);
			this.agent.postGuiEvent(ge);
			this.textField.setText("");
		}
	}

}
