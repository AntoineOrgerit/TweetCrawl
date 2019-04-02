package com.tweetcrawl.agents.ui;

import java.awt.Container;
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

import com.tweetcrawl.agents.Launcher;

import jade.gui.GuiEvent;

/**
 * GUI of the launcher agent
 */
public class AgentLauncherGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Launcher agent;

	/** types of action performed **/
	public static final int EXIT = 0;
	public static final int INPUT = 1;

	private JTextField textField;
	private JButton button;

	/**
	 * Constructor of the GUI
	 * 
	 * @param agent launcher agent using the GUI
	 */
	public AgentLauncherGUI(Launcher agent) {
		this.agent = agent;
		this.generateFrame();
	}

	/**
	 * Allows to generate the frame
	 */
	private void generateFrame() {
		Container contentPane = this.generateLayout();
		this.generateLabelAndInput(contentPane);
		this.generateButton(contentPane);
		this.setContentPane(contentPane);
		this.setIconImage((new ImageIcon("./img/twitter-logo-vector-png-clipart-1.png")).getImage());
		this.setTitle("Twitter Crawler");
		this.pack();
		this.setLocationRelativeTo(null);
		this.setCloseOperation();
	}

	/**
	 * Allows to generate the layout of the frame
	 * 
	 * @return the <code>Container</code> containing the layout used
	 */
	private Container generateLayout() {
		GridBagLayout layout = new GridBagLayout();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);
		return contentPane;
	}

	/**
	 * Allows to generate the label and the input of the container
	 * 
	 * @param contentPane the <code>Container</code> where to include the label and
	 *                    the input
	 */
	private void generateLabelAndInput(Container contentPane) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Rechercher sur Twitter :");
		this.textField = new JTextField(10);
		label.setLabelFor(this.textField);
		panel.setLayout(new GridLayout(1, 2, 7, 7));
		panel.add(label);
		panel.add(this.textField);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(7, 7, 7, 7);
		contentPane.add(panel, c);
	}

	/**
	 * Allows to generate the button of the container
	 * 
	 * @param contentPane the <code>Container</code> where to include the button
	 */
	private void generateButton(Container contentPane) {
		this.button = new JButton("Rechercher");
		this.button.addActionListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(7, 7, 7, 7);
		c.gridx = 0;
		c.gridy = 1;
		contentPane.add(this.button, c);
	}

	/**
	 * Allows to set a close operation to the frame
	 */
	private void setCloseOperation() {
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
			String input = this.textField.getText();
			GuiEvent ge = new GuiEvent(this, AgentLauncherGUI.INPUT);
			ge.addParameter(input);
			this.agent.postGuiEvent(ge);
			this.textField.setText("");
		}
	}

}
