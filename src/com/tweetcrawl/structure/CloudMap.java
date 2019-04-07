package com.tweetcrawl.structure;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import java.awt.geom.AffineTransform;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class CloudMap {

    private Map<ZonedDateTime, Map<String, Integer>> datedTags;
    private int currentAgentsNumber;
    private String term;

    public CloudMap(String term) {
        this.term = term;
        datedTags = new HashMap<>();
        this.currentAgentsNumber = 1;
    }


    public Map<String, Integer> getMapFromDate(ZonedDateTime date) {

        ZonedDateTime day = date.truncatedTo(ChronoUnit.DAYS);

        if (!datedTags.containsKey(day))
            datedTags.put(day, new HashMap<>());

        return datedTags.get(day);

    }

    public Map<ZonedDateTime, Map<String, Integer>> getDatedTags() {
        return datedTags;
    }

    public Map<String,Integer> getInnerMap(String term){
        return this.datedTags.get(term);
    }

    public void addTagToMap(String tag, ZonedDateTime date) {

        Map<String, Integer> selectedMap = getMapFromDate(date);
        int nbOccurences = 1;

        if (selectedMap.containsKey(tag))
            nbOccurences = selectedMap.get(tag) + 1;

        selectedMap.put(tag, nbOccurences);

    }

    private void trimOlderTags() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        Vector<ZonedDateTime> toDelete = new Vector<>();


        for (Map.Entry<ZonedDateTime, Map<String, Integer>> entry : datedTags.entrySet()) {
            if (entry.getKey().until(today, ChronoUnit.DAYS) > 10)
                toDelete.add(entry.getKey());
        }

        for (ZonedDateTime date : toDelete)
            datedTags.remove(date);
    }

    public void addAgent() {
        this.currentAgentsNumber++;
    }

    public int removeAgent() {
        return --this.currentAgentsNumber;
    }

    public Map<String, Integer>[] getTopTags() {

        trimOlderTags();

        Map<String, Integer>[] topList = new LinkedHashMap[10];
        ZonedDateTime[] tenLastDays = new ZonedDateTime[10];

        //Calculate 10 last days
        for (int i = 0; i < 10; i++)
            tenLastDays[i] = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minus(9 - i, ChronoUnit.DAYS);

        Map<String, Integer> sortedTags;
        int topCount;

        //Iterating through the dates
        for (int i = 0; i < 10; i++) {
            //Initialization of the day
            topList[i] = new LinkedHashMap<>();

            //if the i-th day is in the map
            if (datedTags.containsKey(tenLastDays[i])) {

                //Sorting tags
                /*sortedTags = datedTags.get(tenLastDays[i])
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));*/

                sortedTags = sortHashMapElems(this.term);  // Pas sur que ce soit comme ça qu'il faut faire... on verra :shrug:

                topCount = 0;

                //Putting ten most used words in corresponding map
                for (Map.Entry<String, Integer> entry : sortedTags.entrySet()) {
                    //This way was safer to use : it iterates until there is no more tags left, and puts up to 10 tags in map
                    if (topCount < 10)
                        topList[i].put(entry.getKey(), entry.getValue());

                    topCount++;
                }
            }
        }

        return topList;

    }

    private LinkedHashMap sortHashMapElems (String term) {
        Map<String,Integer> map = this.getInnerMap(term);
        LinkedHashMap sorted = map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return sorted;
    }






    //METHODE AFFICHAGE
    //APPELEE A LA FIN DE LA MISE A JOUR
    //ON APPELLE TOP10 METHODE
    public void draw ( String term ) {

        // Create a frame
        Frame frame = new Frame();
        frame.setTitle("Most discussed terms since 10 days about " + term);

        // Add a component with a custom paint method
        frame.add(new CustomPaintComponent());

        // Add a listener to allow to close window
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        // Display the frame
        int frameWidth = 1800;
        int frameHeight = 300;
        frame.setSize(frameWidth, frameHeight);

        frame.setVisible(true);
    }

    //pour le test
    public static void main (String[] args) {
        new CloudMap("#OTACOS").draw("#OTACOS");
    }

    static class CustomPaintComponent extends Component {

        private static Map<String, Integer>[] mapTest;

        public void paint(Graphics g) {

            //test
            mapTest = new LinkedHashMap[10];
            for(int i = 0; i < 10; i++) {
                mapTest[i] = new LinkedHashMap<>();

                mapTest[i].put("KOIFJSQDKLJ", 21);
                mapTest[i].put("sfsdfsd", 18);
                mapTest[i].put("FDSF", 16);
                mapTest[i].put("GSDGDSG", 15);
                mapTest[i].put("Gfdgdfg", 11);
                mapTest[i].put("GgGgG", 8);
                mapTest[i].put("sdgsg", 7);
                mapTest[i].put("errrr", 7);
                mapTest[i].put("eryu", 6);
                mapTest[i].put("zer", 6);
            }



            Graphics2D g2d = (Graphics2D) g;

            drawZones(g2d);
            drawDays(g2d);

            drawTags(g2d, mapTest);

        }

        private void drawZones (Graphics2D g2d) {

            g2d.setColor(Color.lightGray);

            for (int i = 0; i < 5; i++)
                g2d.fillRect(i * 360, 0, 180, 300);
        }

        private void drawDays (Graphics2D g2d) {
            for (int i = 0; i < 9; i++)
                drawCenteredText(g2d, i * 180, 20, 180, "J - " + (9-i));

            drawCenteredText(g2d, 1620, 20, 180, "TODAY");

        }

        private void drawTags (Graphics2D g2d, Map<String, Integer>[] tagMaps) {
            for (int i = 0; i < 10; i++)
                drawDailyTags(g2d, i, tagMaps[i]);
        }

        private void drawDailyTags (Graphics2D g2d, int day, Map<String, Integer> map) {

            Font font = new Font("Arial", Font.PLAIN, 12);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);

            int size, x, y = 60;
            String word;


            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                x = 20 + day * 180;
                word = entry.getKey();
                size = 12 + (int)Math.sqrt(entry.getValue() * 2);

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
