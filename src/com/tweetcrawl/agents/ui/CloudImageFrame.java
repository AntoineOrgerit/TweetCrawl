package com.tweetcrawl.agents.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloudImageFrame extends Frame {

    private static final long serialVersionUID = 1L;
    private Map<String, Integer>[] topTags;

    /**
     * Constructor of the frame.
     *
     * @param term the term associated with the graph that has to be displayed
     */
    public CloudImageFrame(String term, Map<String, Integer>[] topTags) {
        this.topTags = topTags;
        this.setTitle("Most discussed terms since 10 days about " + term);
        this.add(new CustomPaintComponent());
        int frameWidth = 1800;
        int frameHeight = 300;
        this.setSize(frameWidth, frameHeight);
        this.setVisible(true);
    }

    private class CustomPaintComponent extends Component {

        @Override
        public void paint(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;

            drawZones(g2d);
            drawDays(g2d);

            drawTags(g2d, topTags);

        }

        private void drawZones(Graphics2D g2d) {

            g2d.setColor(Color.lightGray);

            for (int i = 0; i < 5; i++)
                g2d.fillRect(i * 360, 0, 180, 300);
        }

        private void drawDays(Graphics2D g2d) {
            for (int i = 0; i < 9; i++)
                drawCenteredText(g2d, i * 180, 20, 180, "J - " + (9 - i));

            drawCenteredText(g2d, 1620, 20, 180, "TODAY");

        }

        private void drawTags(Graphics2D g2d, Map<String, Integer>[] tagMaps) {
            for (int i = 0; i < 10; i++) {
                if (!tagMaps[i].isEmpty()) {
                    System.out.println(i + " -> " + tagMaps[i]);
                    drawDailyTags(g2d, i, tagMaps[i]);
                }
            }
        }

        private void drawDailyTags(Graphics2D g2d, int day, Map<String, Integer> map) {

            Font font;
            g2d.setColor(Color.BLACK);

            int size, x, y = 60;
            String word;


            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                x = 20 + day * 180;
                word = entry.getKey();
                size = 12 + (int) Math.sqrt(entry.getValue() * 2);

                System.out.println("\t" + word + " (" + entry.getValue() + ")");

                font = new Font("Arial", Font.PLAIN, size);
                g2d.setFont(font);

                g2d.drawString(word, x, y);

                y += size + 5;
            }

        }

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
