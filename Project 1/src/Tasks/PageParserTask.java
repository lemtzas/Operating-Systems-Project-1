package Tasks;


import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        System.out.println("start PageParserTask (" + URL + ")");
        Document doc = Jsoup.parse(pageText, URL);

        //follow valid links
        Elements links = doc.select("a[href]");
        for(Element link : links) {
            String linkText = link.attr("abs:href");
            System.out.println("  site (" + urlValidator.isValid(linkText) + "): " + linkText);
            if(urlValidator.isValid(linkText)) {
                this.addGeneratedTask(new PageRetrieverTask(linkText,getSharedData()));
                System.out.println("  site: " + linkText);
            }
        }


        String text = doc.title() + doc.head().html() + doc.body().html();

        this.addGeneratedTask(new DataGathererTask(getSharedData(), text, doc));
        System.out.println("end PageParserTask");
    }
}