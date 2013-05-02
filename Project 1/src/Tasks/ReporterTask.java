package Tasks;

/**
 * Prints the output
 */
public class ReporterTask extends IOTask {
    private static final int PRIORITY = -2;

    private final DataSnapshot snapshot;

    public ReporterTask(SharedData sharedData, DataSnapshot snapshot) {
        super(sharedData, PRIORITY);
        this.snapshot = snapshot;
    }

    @Override
    public void run() {
        //System.out.println("start ReporterTask");
        System.out.println("------------------------------");
        System.out.println(snapshot);
        System.out.println("------------------------------");
        //System.out.println("end ReporterTask");
    }
}
