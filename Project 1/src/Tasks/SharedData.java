package Tasks;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The data shared between threads.
 */
public class SharedData {
    public final String[] keywords;


    public final DataSnapshot dataSnapshot = new DataSnapshot();
    public final Lock dataSnapshotLock = new ReentrantLock();
    public final long startTime = System.currentTimeMillis();
    private boolean done = false; //set to true when we are done

    public SharedData(String[] keywords, int pageLimit) {
        this.keywords = keywords;
        dataSnapshot.pageLimit = pageLimit;
        for(String s : keywords) {
            dataSnapshot.wordCounts.put(s, 0);
        }
    }

    /**
     * @return a snapshot of the current stats (a copy)
     */
    public DataSnapshot getSnapshot() {
        dataSnapshotLock.lock();
        DataSnapshot copy = new DataSnapshot(dataSnapshot);
        dataSnapshotLock.unlock();
        return copy;
    }

    /**Is processing finished. (display should still continue)**/
    public boolean isDone() {
        return done;
    }
    /**Set processing to be finished (display should still continue until all items are printed)**/
    public void setDone() {
        done = true;
    }
    /**Update the word counts with the given counts for a particular page.**/
    public void updateCounts(HashMap<String, Integer> counts) {
        dataSnapshotLock.lock();
        for(Map.Entry<String,Integer> e : counts.entrySet()) {
            dataSnapshot.wordCounts.put(
                    e.getKey(),
                    dataSnapshot.wordCounts.get(e.getKey()) + e.getValue());
        }
        dataSnapshotLock.unlock();
    }

    /**Update the total runtime**/
    public void updateRuntime() {
        dataSnapshotLock.lock();
        dataSnapshot.totalRunTime = System.currentTimeMillis() - startTime;
        dataSnapshotLock.unlock();
    }

    /**Add time period to number tracking total time spent parsing**/
    public void addParseTime(final long milliseconds) {
        dataSnapshotLock.lock();
        dataSnapshot.totalParseTime += milliseconds;
        dataSnapshotLock.unlock();
    }
    /**Indicate that we have finished parsing a page**/
    public void parsedPage() {
        dataSnapshotLock.lock();
        dataSnapshot.pageCount += 1;
        dataSnapshotLock.unlock();
    }
    /**Check that we are not over the maximum number of pages to be parsed.**/
    public boolean checkOvermax() {
        dataSnapshotLock.lock();
        if(dataSnapshot.pageCount > dataSnapshot.pageLimit) {
            this.done = true;
            System.out.println(dataSnapshot.pageCount + " > " + dataSnapshot.pageLimit + " -> done");
        }
        dataSnapshotLock.unlock();
        return this.done;
    }
    /**Add number of reported links for a page to the running total.**/
    public void addLinks(int linkCount) {
        dataSnapshotLock.lock();
        dataSnapshot.urlCount+= linkCount;
        dataSnapshotLock.unlock();
    }

    /**Add number of counted words for a page to the running total.**/
    public void addWords(int wordCount) {
        dataSnapshotLock.lock();
        dataSnapshot.wordCount += wordCount;
        dataSnapshotLock.unlock();
    }
}
