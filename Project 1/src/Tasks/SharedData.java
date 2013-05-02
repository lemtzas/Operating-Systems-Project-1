package Tasks;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The data shared between threads
 */
public class SharedData {
    public final TaskQueue taskQueue = new TaskQueue();
    public final String[] keywords;


    public final DataSnapshot dataSnapshot = new DataSnapshot();
    public final Lock dataSnapshotLock = new ReentrantLock();
    public final long startTime = System.currentTimeMillis();

    public SharedData(String[] keywords, int pageLimit) {
        this.keywords = keywords;
        dataSnapshot.pageLimit = pageLimit;
        for(String s : keywords) {
            dataSnapshot.testMap.put(s,0);
        }
    }

    /**
     * @return a snapshot of the current stats
     */
    public DataSnapshot getSnapshot() {
        dataSnapshotLock.lock();
        DataSnapshot copy = new DataSnapshot(dataSnapshot);
        dataSnapshotLock.unlock();
        return copy;
    }

	// DataSnapshot now holds page count as well, may want to only hold it there
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

    public void updateCounts(HashMap<String, Integer> counts) {
        dataSnapshotLock.lock();
        for(Map.Entry<String,Integer> e : counts.entrySet()) {
            dataSnapshot.testMap.put(
                    e.getKey(),
                    dataSnapshot.testMap.get(e.getKey()) + e.getValue());
        }
        dataSnapshotLock.unlock();
    }

    public void updateRuntime() {
        dataSnapshotLock.lock();
        dataSnapshot.totalRunTime = System.currentTimeMillis() - startTime;
        dataSnapshotLock.unlock();
    }

    public void addParseTime(final long milliseconds) {
        dataSnapshotLock.lock();
        dataSnapshot.totalParseTime += milliseconds;
        dataSnapshotLock.unlock();
    }

    public void parsedPage() {
        dataSnapshotLock.lock();
        dataSnapshot.pageCount++;
        dataSnapshotLock.unlock();
    }

    public void checkOvermax() {
        dataSnapshotLock.lock();
        if(dataSnapshot.pageCount > dataSnapshot.pageLimit)
            this.done = true;
        dataSnapshotLock.unlock();
    }
}
