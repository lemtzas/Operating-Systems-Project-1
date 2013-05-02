package Tasks;

import net.sourceforge.jrobotx.RobotExclusion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Retrieves a page and generates the task to process it
 */
public class PageRetrieverTask extends Task {
    private static final ConcurrentSkipListSet<String> accessedPages = new ConcurrentSkipListSet<String>();
    private static final String QUESTION_EVERYTHING = "questioneverything.typepad.com";

    /**The priority of these tasks. Higher is Better.**/
    public static final int PRIORITY = 1;

    private String URL;
    private static final int TIMEOUT = 1000;

    public PageRetrieverTask(String URL, SharedData sharedData) {
        super(sharedData,PRIORITY);
        this.URL = URL;
    }

    /**
     * Checks the robots page for the
     * @return true if we are allowed to access this web page.
     */
    private boolean robotsCheck() {
//        if(!checkedRobots.contains(getRobotsURL()))
//            checkRobots();

        return !URL.contains(QUESTION_EVERYTHING);// && robotExclusion.allows(new URL(URL),"TCSS422 Bot");
    }



    @Override
    public void run() {
        if(accessedPages.contains(URL)) return; //ignore duplicates
        accessedPages.add(URL);
        boolean allowed = robotsCheck();
        //System.out.println("PageRetrieverTask (" + URL + ") " + allowed);
        if(!allowed) return; //cancel
        //get the page
        String text = getPageText();
        if(text != null) { //make sure we got the page
            this.addGeneratedTask(new PageParserTask(URL,text,getSharedData()));
        }/* else {
            System.out.println("error");
        }*/
    }

    private String getPageText() {
        try {
            URL url = new URL(this.URL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String inputLine;


            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine).append('\n');
            in.close();
            return sb.toString();
        } catch(Exception e) {
            return null;
        }
    }
}
