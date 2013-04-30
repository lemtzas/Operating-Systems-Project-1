package Tasks;

import java.util.PriorityQueue;

/**
 * The data shared between threads
 */
public class SharedData {
    public final TaskQueue taskQueue = new TaskQueue();

    /**
     * @return a snapshot of the current stats
     */
    public DataSnapshot getSnapshot() {
        return new DataSnapshot();
    }


    private int processedPageCount = 0;
    private int processedTaskCount = 0;
    public final int getProcessedPageCount() {
        //TODO: Make this thread safe
        return processedPageCount;
    }
    public final void incrementProcessedPageCount(){
        //TODO: Make this thread safe
        processedPageCount++;
    }
    public final int getProcessedTaskCount() {
        //TODO: Make this thread safe
        return processedTaskCount;
    }
    public final void incrementProcessedTaskCount(){
        //TODO: Make this thread safe
        processedTaskCount++;
    }

    private boolean done = false;
    public boolean isDone() {
        //TODO: Make this thread safe
        return done;
    }
    public void setDone() {
        //TODO: Make this thread safe
        done = true;
    }
}
