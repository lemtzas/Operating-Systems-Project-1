import Tasks.SharedData;
import Tasks.Task;

import java.util.Set;

/**
 * One of the threads in the thread pool
 */
public class ExecutionThread implements Runnable {
    private final SharedData sharedData;
    private final boolean IOThread;

    /**
     * Instantiates an Execution Thread
     * @param sharedData Data in the shared space
     * @param IOThread Should this thread perform I/O?
     */
    public ExecutionThread(SharedData sharedData, boolean IOThread) {
        this.sharedData = sharedData;
        this.IOThread = IOThread;
    }

    @Override
    public void run() {
        Task task = null;
        while(!sharedData.isDone()) {
            task = sharedData.taskQueue.getTask(IOThread);
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
//            if(sharedData.taskQueue.getTaskCount() == 0) {
//                sharedData.setDone();
//            }
        }
    }
}
