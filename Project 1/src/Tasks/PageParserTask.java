package Tasks;


import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sound.midi.Patch;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A stub for the PageParserTask
 */
public class PageParserTask extends Task {
    private static final UrlValidator urlValidator = new UrlValidator(new String[]{"http","https"});

    /**The priority of these tasks. Higher is Better.**/
    public static final int PRIORITY = 0;
    private final String pageText;
    private final String URL;

    public PageParserTask(String URL, String pageText, SharedData sharedData) {
        super(sharedData, PRIORITY);
        this.pageText = pageText;
        this.URL = URL;
    }
    

    @Override
    public void run() {
        System.out.println("PageParserTask (" + URL + ")");

        final long startTime = System.currentTimeMillis();

        Document doc = Jsoup.parse(pageText, URL);


        //count the words
        int wordCount = 0;
        if(doc != null && doc.body() != null) {
            Pattern p = Pattern.compile("\\w+");
            Matcher m = p.matcher(doc.body().text());
            while(m.find())
                wordCount++;
        }


        //follow valid links
        Elements links = doc.select("a[href]");
        int linkCount = 0;
        for(Element link : links) {
            String linkText = link.attr("abs:href");
            //System.out.println("  site (" + urlValidator.isValid(linkText) + "+"+(linkText.endsWith(".htm") || linkText.endsWith(".html") || linkText.endsWith(".txt"))+"): " + linkText);
            if(urlValidator.isValid(linkText) &&
                    //only follow specified files (hackish way)
                    (linkText.endsWith(".htm") || linkText.endsWith(".html") || linkText.endsWith(".txt"))) {
                this.addGeneratedTask(new PageRetrieverTask(linkText,getSharedData()));
                linkCount++;
            }
        }


        //String text = doc.title() + doc.head().html() + doc.body().html();
        String text = pageText;

        //count the words
        HashMap<String,Integer> counts = new HashMap<String,Integer>();
        for(String s : getSharedData().keywords) {
            int i = -1;
            int count = 0;
            while((i = text.indexOf(s,i+1)) >= 0)
                count++;
            counts.put(s,count);
        }


        this.addGeneratedTask(new DataGathererTask(getSharedData(), URL, linkCount));

        //update stats
        getSharedData().parsedPage();
        getSharedData().updateCounts(counts);
        getSharedData().addLinks(linkCount);
        getSharedData().addWords(wordCount);
        getSharedData().addParseTime(System.currentTimeMillis() - startTime);
    }
}