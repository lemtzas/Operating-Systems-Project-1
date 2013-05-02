package OnePool;

import Tasks.Task;
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
public interface TaskQueue {
    public Task getTask(boolean IOThread, boolean done);
    public void addTasks(Collection<Task> tasks);
}
