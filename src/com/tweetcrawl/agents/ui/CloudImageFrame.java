package com.tweetcrawl.agents.ui;

import java.awt.*;
import java.util.Map;

/**
 * Frame used to display generated word clouds from the {@code CloudGenerator}
 * agent.
 */
public class CloudImageFrame extends Frame {

	private static final long serialVersionUID = 1L;
	private transient Map<String, Integer>[] topTags;

	/**
	 * Constructor of the frame.
	 *
	 * @param term    the term associated with the graph that has to be displayed
	 * @param topTags the top tags to used in the word cloud
	 */
	public CloudImageFrame(String term, Map<String, Integer>[] topTags) {
		this.topTags = topTags;
		this.setTitle("Most discussed terms since 10 days about " + term);
		this.add(new CustomPaintComponent());
		int frameWidth = 1800;
		int frameHeight = 300;
		this.setSize(frameWidth, frameHeight);
		this.setResizable(true);
		this.setVisible(true);
	}

	/**
	 * {@code Component} used by the {@code CloudImageFrame} to contain the word
	 * cloud.
	 */
	private class CustomPaintComponent extends Component {

		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			drawZones(g2d);
			drawDays(g2d);
			drawTags(g2d, topTags);
		}

		/**
		 * Allows to draw the different zones displayed in the {@code CloudImageFrame}.
		 * 
		 * @param g2d the {@code Graphics2D} object in which to display the zones
		 */
		private void drawZones(Graphics2D g2d) {
			g2d.setColor(Color.lightGray);
			for (int i = 0; i < 5; i++) {
				g2d.fillRect(i * 360, 0, 180, 300);
			}
		}

		/**
		 * Allows to draw the days labels in the {@code CloudImageFrame}.
		 * 
		 * @param g2d the {@code Graphics2D} object in which to show the days labels
		 */
		private void drawDays(Graphics2D g2d) {
			for (int i = 0; i < 9; i++) {
				drawCenteredText(g2d, i * 180, 20, 180, "J - " + (9 - i));
			}
			drawCenteredText(g2d, 1620, 20, 180, "TODAY");

		}

		/**
		 * Allows to draw the word clouds for each day.
		 * 
		 * @param g2d     the {@code Graphics2D} object in which to display the word
		 *                clouds
		 * @param tagMaps an array of {@code Map} containing the tags to display in the
		 *                word clouds for each day
		 */
		private void drawTags(Graphics2D g2d, Map<String, Integer>[] tagMaps) {
			for (int i = 0; i < 10; i++) {
				if (!tagMaps[i].isEmpty()) {
					drawDailyTags(g2d, i, tagMaps[i]);
				}
			}
		}

		/**
		 * Allows to draw the word cloud of a particular day.
		 * 
		 * @param g2d the {@code Graphics2D} object in which to display the word cloud
		 * @param day the day for which the word cloud is linked to
		 * @param map a {@code Map} containing the tags to display in the word cloud of
		 *            a day
		 */
		private void drawDailyTags(Graphics2D g2d, int day, Map<String, Integer> map) {
			Font font;
			g2d.setColor(Color.BLACK);
			int size;
			int x;
			int y = 60;
			String word;
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				x = 20 + day * 180;
				word = entry.getKey();
				size = 12 + (int) Math.sqrt((double) entry.getValue() * 2);
				font = new Font("Arial", Font.PLAIN, size);
				g2d.setFont(font);
				g2d.drawString(word, x, y);
				y += size + 5;
			}
		}

		/**
		 * Allows to draw a centered text.
		 * 
		 * @param g2d   the {@code Graphics2D} object in which to display the text
		 * @param x     the horizontal position of the text
		 * @param y     the vertical position of the text
		 * @param width the width of the text to display
		 * @param text  the text to display
		 */
		private void drawCenteredText(Graphics2D g2d, int x, int y, int width, String text) {
			Font font = new Font("Arial", Font.BOLD, 20);
			FontMetrics metrics = g2d.getFontMetrics(font);
			int calculatedX = x + (width - metrics.stringWidth(text)) / 2;
			g2d.setColor(Color.BLACK);
			g2d.setFont(font);
			g2d.drawString(text, calculatedX, y);
		}
	}
}
