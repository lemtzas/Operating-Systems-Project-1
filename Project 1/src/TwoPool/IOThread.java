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
public class IOThread implements Runnable {

    private final SharedData sharedData;
    private final SharedQueues sharedQueues;

    /**
     * Instantiates an Execution Thread
     * @param sharedData Data in the shared space
     * @param sharedQueues
     */
    public IOThread(SharedData sharedData, SharedQueues sharedQueues) {
        this.sharedData = sharedData;
        this.sharedQueues = sharedQueues;
    }

    @Override
    public void run() {
        Task task = null;
        while(true) {
            task = sharedQueues.getIOTask(sharedData.isDone());
            if(task == null) return; //time to stop, there are none left

            //run the task
            task.run();
            //update the task queue
            Set<Task> newTasks = task.getGeneratedTasks();
            sharedQueues.addTasks(newTasks);
            sharedData.incrementProcessedTaskCount();
        }
    }
}
