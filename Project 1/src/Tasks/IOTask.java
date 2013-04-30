package Tasks;

/**
 * A task that performs IO.
 *
 * This is handled differently by the scheduler. Using a Task to perform IO may result in unexpected behavior.
 */
public abstract class IOTask extends Task {
    public IOTask(SharedData sharedData, int priority) {
        super(sharedData, priority);
    }

    public IOTask(SharedData sharedData) {
        super(sharedData);
    }
}
