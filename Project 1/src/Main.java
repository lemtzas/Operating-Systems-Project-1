import OnePool.ExecutionThread;
import OnePool.TaskQueue;
import OnePool.UnifiedTaskQueue;
import Tasks.*;
import TwoPool.IOThread;
import TwoPool.PageParserTaskThread;
import TwoPool.PageRetrieverTaskThread;
import TwoPool.SharedQueues;
import org.apache.commons.cli.*;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * The Entry Point
 */
public class Main {
    private static final String DEFAULT_ROOT = "http://faculty.washington.edu/gmobus/";
    private static final int DEFAULT_MAX = 5;
    private static final String[] DEFAULT_KEYWORDS =
            {"intelligence","artificial","agent","university","research","science","robot"};
    private static final String OUTPUT_FILE = "log.txt";

    private Main() {}

    /**
     * Handle args and determine what to run.
     * @param args command line arguments
     */
    public static void main(String[] args) {

        //interpret command line
        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("s","single-threaded-one-pool",false,"single-threaded singular pool");
        options.addOption("m","multi-threaded-one-pool",true,"multi-threaded singular pool");
        options.addOption("o","output-file",true,"multi-threaded singular pool");
        Option o = OptionBuilder.hasArgs(2)
                .withDescription("multi-threaded-dual-pool(retrieverThreads,parserThreads)")
                .create("d");
        options.addOption(o);
        options.addOption("u","url",true,"Specify the start URL");
        o = OptionBuilder.hasArg()
                .withLongOpt("max")
                .create();
        options.addOption(o);
        o = OptionBuilder.hasArgs()
                .withLongOpt("keywords")
                .create();
        options.addOption(o);

        try {
            CommandLine line = parser.parse(options,args);

            String[] keywords = DEFAULT_KEYWORDS;
            if(line.hasOption("keywords")) //override the default keywords
                keywords = line.getOptionValues("keywords");

            String url = DEFAULT_ROOT;
            if(line.hasOption("url"))
                url = line.getOptionValue("url");

            int max = DEFAULT_MAX;
            try {
                if(line.hasOption("max"))
                    max = Integer.parseInt(line.getOptionValue("max"));
            } catch(Exception e){
                System.out.println("Invalid argument for max");
            }

            String outputFile = OUTPUT_FILE;
            if(line.hasOption("output-file"))
                outputFile = line.getOptionValue("output-file");
            //output to file and console
            System.setOut(new PrintStream(new TeeOutputStream(System.out, new FileOutputStream(outputFile))));



            if(line.hasOption("s")) { //single-threaded option
                singleThreadedOnePool(keywords,max,url);
            } else if(line.hasOption("m")) { //multi-threaded option
                try{
                    int threads = Integer.parseInt(line.getOptionValue("m", "5"));
                    multiThreadedOnePool(keywords, max, url, threads);
                } catch(Exception e) {
                    System.out.println("invalid argument multi-threaded.");
                }
            } else if(line.hasOption("d")) { //dual-pooled, multi-threaded option
                try{
                    String[] counts = line.getOptionValues("d");
                    int x = 5;
                    int y = 5;
                    if(counts.length >= 1)
                        y = x = Integer.parseInt(counts[0]);
                    if(counts.length >= 2)
                        y = Integer.parseInt(counts[1]);
                    multiPool(keywords, max, url, x, y);
                } catch(Exception e) {
                    System.out.println("invalid arguments.");
                }
            } else {
                System.out.println("invalid cli args");
            }
        } catch( ParseException e ) {

        }
    }

    /** Run an instance of the single-threaded crawler**/
    public static void singleThreadedOnePool(final String[] keywords, final int pageLimit, String url) {
        final TaskQueue taskQueue = new UnifiedTaskQueue();

        //the shared data structure
        SharedData sharedData = new SharedData(keywords, pageLimit);

        //add the initial tasks
        Task[] prt = {new PageRetrieverTask(url,sharedData)};
        taskQueue.addTasks(Arrays.asList(prt));

        //begin one thread of execution
        ExecutionThread executionThread = new ExecutionThread(sharedData,taskQueue,true);
        executionThread.run();
    }

    /**Run an instance of the multi-threaded crawler (with one thread pool)**/
    public static void multiThreadedOnePool(final String[] keywords, final int pageLimit, String url, final int threads) {
        final TaskQueue taskQueue = new UnifiedTaskQueue();

        //the shared data structure
        SharedData sharedData = new SharedData(keywords, pageLimit);

        //add the initial tasks
        Task[] prt = {new PageRetrieverTask(url,sharedData)};
        taskQueue.addTasks(Arrays.asList(prt));

        //start most of the threads
        for(int i = 0; i < threads-1; i++)
            (new Thread(new ExecutionThread(sharedData,taskQueue,false))).start();

        //begin one thread of execution for output
        ExecutionThread executionThread = new ExecutionThread(sharedData,taskQueue,true);
        executionThread.run();
    }

    /**Run an instance of the multi-threaded crawler (with two thread pools)**/
    public static void multiPool(final String[] keywords, final int pageLimit, String url, final int retrievers, final int parsers) {

        SharedQueues sharedQueues = new SharedQueues();

        //the shared data structure
        SharedData sharedData = new SharedData(keywords, pageLimit);

        //add the initial tasks
        Task[] prt = {new PageRetrieverTask(url,sharedData)};
        sharedQueues.addTasks(Arrays.asList(prt));

        //start retriever threads
        for(int i = 0; i < retrievers; i++)
            (new Thread(new PageRetrieverTaskThread(sharedData,sharedQueues))).start();

        //start parser threads
        for(int i = 0; i < parsers; i++)
            (new Thread(new PageParserTaskThread(sharedData,sharedQueues))).start();

        //start io thread
        new IOThread(sharedData,sharedQueues).run();
    }
}
