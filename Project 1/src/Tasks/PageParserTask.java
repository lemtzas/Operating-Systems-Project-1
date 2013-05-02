package Tasks;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A stub for the PageParserTask
 */
public class PageParserTask extends Task {
    /**The priority of these tasks. Higher is Better.**/
    public static final int PRIORITY = 0;
    private final String pageText;

    public PageParserTask(String pageText, SharedData sharedData) {
        super(sharedData, PRIORITY);
    }
    

    @Override
    public void run() {
        System.out.println("start PageParserTask");
        Document doc = Jsoup.parse(pageText);
        this.pageText = doc.title() + doc.head().html() + doc.body().html();
        
		//add Reporter task (to output shit to the screen) and increment the number of pages parsed?
		
        this.addGeneratedTask(new DataGathererTask(SharedData, pageText, doc));
        System.out.println(pageText.length());
        System.out.println("end PageParserTask");
    }
}