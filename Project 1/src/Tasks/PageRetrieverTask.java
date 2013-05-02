package Tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Retrieves a page and generates the task to process it
 */
public class PageRetrieverTask extends Task {
    private static final ConcurrentSkipListSet<String> checkedRobots = new ConcurrentSkipListSet<String>();
    private static final ConcurrentSkipListSet<String> ignoreURLs = new ConcurrentSkipListSet<String>();
    static {
        ignoreURLs.add("http://questioneverything.typepad.com/");
    }

    /**The priority of these tasks. Higher is Better.**/
    public static final int PRIORITY = -1;

    private String URL;

    public PageRetrieverTask(String URL, SharedData sharedData) {
        super(sharedData,PRIORITY);
        this.URL = URL;
    }

    /**
     * Checks the robots page for the
     * @return true if we are allowed to access this web page.
     */
    private boolean robotsCheck() {
        if(!checkedRobots.contains(getRobotsURL()))
            checkRobots();

        return !ignoreURL();
    }

    private boolean ignoreURL() {
        return ignoreURLs.contains(this.URL.toLowerCase());
    }

    private void checkRobots() {
        try {
            String root  = getRobotsURL();
            URL url;
            if(root != null)
                url = new URL(root);
            else
                return;


            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                //parse the line
                if(inputLine.toLowerCase().startsWith("disallow: ")) {
                    String path = inputLine.substring("disallow: ".length());
                    System.out.println("    ignore: " + root + path);
                    ignoreURLs.add(root + path);
                }
            }
            in.close();
        } catch(Exception e) {
            return;
        }
    }

    @Override
    public void run() {
        boolean allowed = robotsCheck();
        System.out.println("PageRetrieverTask (" + URL + ") " + allowed);
        if(!allowed) return; //cancel
        //get the page
        String text = getPageText();
        System.out.println(text.length());
        if(text != null) { //make sure we got the page
            this.addGeneratedTask(new PageParserTask(URL,text,getSharedData()));
        }
    }

    private String getPageText() {
        try {
            URL url = new URL(this.URL);
            URLConnection connection = url.openConnection();
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

    private String getRobotsURL() {
        try {
            URL url = new URL(this.URL);
            String root = url.getProtocol() + "://" + url.getHost();
            return root;
        } catch(Exception e) {
            return null;
        }
    }
}
