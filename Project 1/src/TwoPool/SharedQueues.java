package TwoPool;

import Tasks.IOTask;
import Tasks.PageParserTask;
import Tasks.PageRetrieverTask;
import Tasks.Task;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/2/13
 * Time: 4:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class SharedQueues {
    private final PriorityQueue<Task> retrieverTasks    = new PriorityQueue<Task>();
    private final PriorityQueue<Task> parserTasks       = new PriorityQueue<Task>();
    private final PriorityQueue<Task> IOTasks           = new PriorityQueue<Task>();

    private final Lock retrieverLock            = new ReentrantLock();
    private final Lock parserLock               = new ReentrantLock();
    private final Lock IOLock                   = new ReentrantLock();
    private final Condition retrieverNotEmpty   = retrieverLock.newCondition();
    private final Condition parserNotEmpty      = parserLock.newCondition();
    private final Condition IONotEmpty          = IOLock.newCondition();

    /**Blocking call to retriever a Retriever Task**/
    public Task getRetrieverTask() {

        Task result = null;
        try {
            retrieverLock.lock();
            if(retrieverTasks.isEmpty())
                retrieverNotEmpty.await();

            result = retrieverTasks.poll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            retrieverLock.unlock();
        }
        return result;
    }

    /**Blocking call to retriever a Parser Task**/
    public Task getParserTask() {
        Task result = null;
        try {
            parserLock.lock();
            if(parserTasks.isEmpty())
                parserNotEmpty.await();

            result = parserTasks.poll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            parserLock.unlock();
        }
        return result;
    }

    /**Blocking call to retriever a IO Task
     * @param done When this is true, returns null instead of blocking when no entries are left
     **/
    public Task getIOTask(boolean done) {
        Task result = null;
        try {
            IOLock.lock();
            if(IOTasks.isEmpty()) {
                if(!done)
                    IONotEmpty.await();
                else
                    return null; //signal that we are done
            }

            result = IOTasks.poll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            IOLock.unlock();
        }
        return result;
    }


    /**Adds the task to the appropriate queue**/
    public void addTasks(Collection<Task> tasks) {
        for(Task task : tasks) {
            if(task instanceof IOTask) {
                IOLock.lock();
                IOTasks.add(task);
                IONotEmpty.signal();
                IOLock.unlock();
            } else if(task instanceof PageRetrieverTask) {
                retrieverLock.lock();
                retrieverTasks.add(task);
                retrieverNotEmpty.signal();
                retrieverLock.unlock();
            } else if(task instanceof PageParserTask) {
                parserLock.lock();
                parserTasks.add(task);
                parserNotEmpty.signal();
                parserLock.unlock();
            }
        }
    }
}
