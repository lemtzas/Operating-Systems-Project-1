package Tasks;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a task for use on the task priority queue.
 *
 * Use IOTask for Tasks that need to perform I/O serially. Not doing so may result in unexpected behavior.
 */
public abstract class Task implements Runnable, Comparable<Task> {
    /**
     * The tasks generated. Should be null until the process is run.
     */
    private Set<Task> generatedTasks = null;

    private final SharedData sharedData;
    private final int priority;

    /**
     * Constructs a task with priority level 0
     * @param sharedData
     */
    public Task(SharedData sharedData) {
        this(sharedData,0);
    }

    /**
     * Constructs a task with specified priority level
     * @param sharedData
     * @param priority
     */
    public Task(SharedData sharedData, int priority) {
        this.sharedData = sharedData;
        this.priority = priority;
    }

    /**
     * @return The shared data for all the threads.
     */
    protected final SharedData getSharedData() {
        return sharedData;
    }

    /**
     * Adds a new task to be executed once this one completes (in priority order, of course).
     */
    protected final void addGeneratedTask(final Task task) {
        if(generatedTasks == null)
            generatedTasks = new HashSet<Task>();
        generatedTasks.add(task);
    }

    /**
     * @return The tasks generated by this code's run() phase.
     */
    public final Set<Task> getGeneratedTasks() {
        if(generatedTasks != null)
            return generatedTasks;
        else
            return new HashSet<Task>();
    }

    @Override
    public final int compareTo(Task other) {
        return ((Integer)priority).compareTo(other.priority);
    }
}
