package OnePool;

import Tasks.SharedData;
import Tasks.Task;

import java.util.Set;

/**
 * One of the threads in the thread pool
 */
public class ExecutionThread implements Runnable {
    private final SharedData sharedData;
    private final boolean IOThread;
    private final TaskQueue taskQueue;

    /**
     * Instantiates an Execution Thread
     * @param sharedData Data in the shared space
     * @param IOThread Should this thread perform I/O?
     */
    public ExecutionThread(SharedData sharedData, TaskQueue taskQueue, boolean IOThread) {
        this.sharedData = sharedData;
        this.IOThread = IOThread;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        Task task = null;
        while(true) {
            task = taskQueue.getTask(IOThread,sharedData.isDone());
            if(task == null) {
                System.out.println("DONE");
                return;
            }
            //run the task
            task.run();
            //update the task queue
            Set<Task> newTasks = task.getGeneratedTasks();
            taskQueue.addTasks(newTasks);
            sharedData.incrementProcessedTaskCount();
        }
    }
}
