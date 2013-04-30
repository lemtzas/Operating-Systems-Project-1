package Tasks;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import sun.org.mozilla.javascript.internal.ast.ConditionalExpression;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A multi-threaded task queue
 */
public class TaskQueue {
    private static final int SIZE_LIMIT = -1;

    private final PriorityQueue<Task> taskQueue = new PriorityQueue<Task>();
    private final PriorityQueue<Task> IOQueue = new PriorityQueue<Task>();
    private final Lock lock = new ReentrantLock();
    private final Condition notFull    = lock.newCondition();
    private final Condition notEmpty   = lock.newCondition();
    private final Condition IOnotEmpty = lock.newCondition();

    public final Task getTask(boolean IOThread) {
        Task task = null;
        lock.lock();
        try {
            //if this is from an IOThread, check for an IO process each time
            if(IOThread && !IOQueue.isEmpty()) {
                task = IOQueue.poll();
            } else {
                if(taskQueue.isEmpty())
                    notEmpty.await();

                task = taskQueue.poll();
                notFull.signal();
            }
        } finally {
            lock.unlock();
            return task;
        }
    }

    public final int getTaskCount() {
        return taskQueue.size();
    }

    public void addTasks(Collection<Task> tasks) {
            lock.lock();
            try {
                if(SIZE_LIMIT != -1 && taskQueue.size() >= SIZE_LIMIT)
                    notFull.await();

                boolean IOTask = false;
                boolean task = false;

                for(Task t: tasks) {
                    if(t instanceof IOTask) {
                        IOQueue.add(t);
                        IOTask = true;
                    } else {
                        taskQueue.add(t);
                        task = true;
                    }
                }
                if(IOTask)IOnotEmpty.signal();
                if(task)notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
    }
}
