package TwoPool;

import Tasks.SharedData;
import Tasks.Task;

import java.util.Set;

/**
 * A thread that executes only IOTasks from the TwoPool model.
 */
public class IOThread implements Runnable {

    private final SharedData sharedData;
    /**The queues where pending tasks are stored**/
    private final SharedQueues sharedQueues;

    /**
     * Instantiates an Execution Thread
     * @param sharedData Data in the shared space
     * @param sharedQueues The location of the shared task queues
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
            if(task == null) return; //time to stop, there are none left and we are done

            //run the task
            task.run();
            //update the task queue
            Set<Task> newTasks = task.getGeneratedTasks();
            sharedQueues.addTasks(newTasks);
        }
    }
}
