package TwoPool;

import Tasks.SharedData;
import Tasks.Task;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/2/13
 * Time: 4:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class PageRetrieverTaskThread implements Runnable {

    private final SharedData sharedData;
    private final SharedQueues sharedQueues;

    /**
     * Instantiates an Execution Thread
     * @param sharedData Data in the shared space
     * @param sharedQueues
     */
    public PageRetrieverTaskThread(SharedData sharedData, SharedQueues sharedQueues) {
        this.sharedData = sharedData;
        this.sharedQueues = sharedQueues;
    }

    @Override
    public void run() {
        Task task = null;
        while(!sharedData.isDone()) {
            task = sharedQueues.getRetrieverTask();
            if(task != null) {
                //run the task
                task.run();
                //update the task queue
                Set<Task> newTasks = task.getGeneratedTasks();
                sharedQueues.addTasks(newTasks);
                sharedData.incrementProcessedTaskCount();
            }
            //are we done yet?
            //TODO: Make sure this checks for active tasks
//            if(sharedData.taskQueue.getTaskCount() == 0) {
//                sharedData.setDone();
//            }
        }
    }
}
