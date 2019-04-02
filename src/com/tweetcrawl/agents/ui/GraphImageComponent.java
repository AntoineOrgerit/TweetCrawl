package com.tweetcrawl.agents.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * JComponent containing the image of a graph
 */
public class GraphImageComponent extends JPanel {

	private static final long serialVersionUID = 1L;

	private transient Image image;
	Dimension size;

	/**
	 * Constructor of the component
	 * 
	 * @param term the term associated with the component and the image
	 * @throws IOException if the image couldn't be retrieved
	 */
	public GraphImageComponent(String term) throws IOException {
		File file = new File("./visualisation/tweets_" + term + ".png");
		this.image = ImageIO.read(file);
		this.size = new Dimension(((BufferedImage) this.image).getWidth(), ((BufferedImage) this.image).getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return this.size;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (this.image != null) {
			g.drawImage(this.image, 0, 0, this);
		}
	}

}
