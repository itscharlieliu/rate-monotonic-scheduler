import java.util.Timer;

public class RateMonotonicScheduler {

    static final RateMonotonicScheduler sync = new RateMonotonicScheduler();
    static final int NUM_TIMES_RUN = 10;
    static final int TIME_UNIT_LENGTH = 10;
    static final int MAJOR_FRAME_PERIOD = 16;

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(scheduler, 0, TIME_UNIT_LENGTH);

        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted at RateMonotonicScheduler");
            }
        }

        timer.cancel();

        System.out.println(
                "\nScheduler periods completed (RateMonotonicScheduler): "
                + scheduler.schedulerPeriodsCompleted
                + "\n"
        );
    }
}
