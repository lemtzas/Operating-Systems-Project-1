package Tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Retrieves a page and generates the task to process it
 */
public class PageRetrieverTask extends Task {
    /**The priority of these tasks. Higher is Better.**/
    public static final int PRIORITY = -1;

    private String URL;

    public PageRetrieverTask(String URL, SharedData sharedData) {
        super(sharedData,PRIORITY);
        this.URL = URL;
    }

    @Override
    public void run() {
        System.out.println("start PageRetrieverTask");
        //get the page
        String text = getPageText();
        System.out.println(text.length());
        if(text != null) { //make sure we got the page
            this.addGeneratedTask(new PageParserTask(text,getSharedData()));
        }
        System.out.println("end PageRetrieverTask");
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
}
