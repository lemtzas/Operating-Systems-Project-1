package Tasks;

import org.jsoup.nodes.Document;

/**
 * Interprets the data generated by the PageRetrieverTask
 */
public class DataGathererTask extends Task {
    public static final int PRIORITY = -1;
    private final String HTML;
    public DataGathererTask(SharedData sharedData, String parsedHTML, Document doc) {
        super(sharedData,PRIORITY);
        this.HTML = parsedHTML;
    }

    @Override
    public void run() {
        System.out.println("start DataGathererTask");
//		final Pattern p = Pattern.compile(HTML, Pattern.CASE_INSENSITIVE);
//		for(int i=0; i<2 ; i++){			// 2 is a placeholder for the number of words to be scanned for
//			Matcher m = p.Matcher("");  	// "" placeholder for the words being scanned
//			while(m.find()) {
//				//access and increment DataSnapshot
//			}
//		}
		
        //TODO: Update Stats.
        //TODO: Add stats to output Queue
        //TODO: Tell GUI there are stats to display
        this.addGeneratedTask(new ReporterTask(getSharedData(),getSharedData().getSnapshot()));
        System.out.println("end DataGathererTask");
    }
}
