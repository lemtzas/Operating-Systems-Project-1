import Tasks.PageRetrieverTask;
import Tasks.SharedData;
import Tasks.Task;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * The Entry Point
 */
public class Main {
    private static final String ROOT = "http://faculty.washington.edu/gmobus/";
    private static final int DEFAULT_MAX = 5;
    private static final String[] DEFAULT_KEYWORDS =
            {"intelligence","artificial","agent","university","research","science","robot"};
    private static final String OUTPUT_FILE = "log.txt";

    private Main() {}

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(new TeeOutputStream(System.out, new FileOutputStream(OUTPUT_FILE))));
        singleThreaded(DEFAULT_KEYWORDS, DEFAULT_MAX);
    }

    public static void singleThreaded(final String[] keywords, final int pageLimit) {
        //the shared data structure
        SharedData sharedData = new SharedData(keywords, pageLimit);

        //add the initial tasks
        Task[] prt = {new PageRetrieverTask(ROOT,sharedData)};
        sharedData.taskQueue.addTasks(Arrays.asList(prt));

        //begin one thread of execution
        ExecutionThread executionThread = new ExecutionThread(sharedData,true);
        executionThread.run();
    }

    public static void multiThreaded(final String[] keywords, final int pageLimit, final int threads) {
        //the shared data structure
        SharedData sharedData = new SharedData(keywords, pageLimit);

        //start most of the threads
        for(int i = 0; i < threads-1; i++)
            (new Thread(new ExecutionThread(sharedData,false))).start();

        //begin one thread of execution for output
        ExecutionThread executionThread = new ExecutionThread(sharedData,true);
        executionThread.run();
    }
}
