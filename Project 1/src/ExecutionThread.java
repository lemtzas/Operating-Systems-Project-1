import Tasks.SharedData;
import Tasks.Task;

import java.util.Set;

/**
 * One of the threads in the thread pool
 */
public class ExecutionThread implements Runnable {
    private final SharedData sharedData;
    public ExecutionThread(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        Task task = null;
        while(!sharedData.isDone()) {
            task = sharedData.taskQueue.getTask();
            if(task != null) {
                //run the task
                task.run();
                //update the task queue
                Set<Task> newTasks = task.getGeneratedTasks();
                sharedData.taskQueue.addTasks(newTasks);
                sharedData.incrementProcessedTaskCount();
            }
            //are we done yet?
            //TODO: Make sure this checks for active tasks
            if(sharedData.taskQueue.getTaskCount() == 0) {
                sharedData.setDone();
            }
        }
    }
}
