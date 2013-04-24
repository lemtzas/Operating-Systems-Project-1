import Tasks.PageRetrieverTask;
import Tasks.SharedData;
import Tasks.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * The Entry Point
 */
public class Main {
    private Main() {}

    public static void main(String[] args) throws Exception {
        SharedData sharedData = new SharedData();

        //add the initial tasks
        Task[] prt = {new PageRetrieverTask("http://www.yahoo.com/",sharedData)};
        sharedData.taskQueue.addTasks((List<Task>)Arrays.asList(prt));


        //begin one thread of execution
        ExecutionThread executionThread = new ExecutionThread(sharedData);
        executionThread.run();
    }
}
