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
    private final SharedData sharedData;

    public PageParserTask(String URL, String pageText, SharedData sharedData) {
        super(sharedData, PRIORITY);
        this.pageText = pageText;
        this.sharedData = sharedData;
    }
    

    @Override
    public void run() {
        System.out.println("start PageParserTask");
        Document doc = Jsoup.parse(pageText);

        //follow valid links
        Elements links = doc.select("a[href]");
        for(Element link : links) {
            String linkText = link.attr("abs:href");
            if(urlValidator.isValid(linkText)) {
                this.addGeneratedTask(new PageRetrieverTask(linkText,getSharedData()));
            }
        }


        String text = doc.title() + doc.head().html() + doc.body().html();

        this.addGeneratedTask(new DataGathererTask(sharedData, text, doc));
        System.out.println("end PageParserTask");
    }
}