import java.util.concurrent.Semaphore;

public class ScheduledThread extends Thread {
    int numOverruns = 0;
    int counter = 0;
    int timesRun = 0;

    final int period;

    private int numTimesDoWork;
    private Semaphore sem;
    private boolean stopped = false;


    ScheduledThread(Semaphore sem, int numTimesDoWork, int period) {
        this.numTimesDoWork = numTimesDoWork;
        this.sem = sem;
        this.period = period;
    }

    void stopThread() {
        stopped = true;
    }

    private void doWork() {
        double[][] matrix = new double[10][10];
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[j][i] *= 1d;
            }
        }
    }

    @Override
    public void run() {

        while(!stopped) {
            try {
                sem.acquire();
                if (stopped) {
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println("Unable to acquire semaphore in thread: " + this.getName());
            }
            for (int i = 0; i < numTimesDoWork; ++i) {
                doWork();
            }
            this.timesRun++;
        }
    }
}
