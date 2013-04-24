package Tasks;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A multi-threaded task queue
 */
public class TaskQueue {
    private final PriorityQueue<Task> taskQueue = new PriorityQueue<Task>();
    private final Mutex mutex = new Mutex();

    public final Task getTask() {
        try {
            Task task = null;
            mutex.acquire();
            try {
                //TODO: Make this a blocking call if queue is empty
                task = taskQueue.poll();
            } finally {
                mutex.release();
                return task;
            }
        } catch(InterruptedException ie) {
            // ...
        }
        return null;
    }

    public final int getTaskCount() {
        return taskQueue.size();
    }

    public void addTasks(Collection<Task> tasks) {
        try {
            mutex.acquire();
            try {
                //TODO: Task Queue size limit?
                taskQueue.addAll(tasks);
            } finally {
                mutex.release();
            }
        } catch(InterruptedException ie) {
            // ...
        }
    }
}
