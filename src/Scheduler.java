import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class Scheduler extends TimerTask {
    int schedulerPeriodsCompleted;

    private static final int THREAD_1_PERIOD = 1;
    private static final int THREAD_2_PERIOD = 2;
    private static final int THREAD_3_PERIOD = 4;
    private static final int THREAD_4_PERIOD = 16;

    private Semaphore sem1 = new Semaphore(0, true);
    private Semaphore sem2 = new Semaphore(0, true);
    private Semaphore sem3 = new Semaphore(0, true);
    private Semaphore sem4 = new Semaphore(0, true);

    private ScheduledThread thread1 = new ScheduledThread(sem1, 1, THREAD_1_PERIOD);
    private ScheduledThread thread2 = new ScheduledThread(sem2, 2, THREAD_2_PERIOD);
    private ScheduledThread thread3 = new ScheduledThread(sem3, 4, THREAD_3_PERIOD);
    private ScheduledThread thread4 = new ScheduledThread(sem4, 16, THREAD_4_PERIOD);

    Scheduler(){
        schedulerPeriodsCompleted = 0;

        thread1.setPriority(4);
        thread2.setPriority(3);
        thread3.setPriority(2);
        thread4.setPriority(1);

        thread1.setName("Thread1");
        thread2.setName("Thread2");
        thread3.setName("Thread3");
        thread4.setName("Thread4");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    // TODO scheduler will keep track of overrun, thread with keep track of how many times it runs

    private void scheduleThread(ScheduledThread thread, Semaphore sem) {
        if (thread.counter == 0) {
            if (!sem.hasQueuedThreads()){ // Queue is empty, meaning no thread has called acquire() yet
                thread.numOverruns++;
                System.out.println("Overrrun");
            }
            sem.release();
            thread.counter = thread.period;
        }
        thread.counter--;
    }

    @Override
    public void run() {

        // stopping the scheduler
        if (schedulerPeriodsCompleted >= RateMonotonicScheduler.NUM_TIMES_RUN * RateMonotonicScheduler.MAJOR_FRAME_PERIOD) {
            thread1.stopThread();
            thread2.stopThread();
            thread3.stopThread();
            thread4.stopThread();

            sem1.release();
            sem2.release();
            sem3.release();
            sem4.release();

            try {
                thread1.join();
                thread2.join();
                thread3.join();
                thread4.join();
            } catch (InterruptedException e) {
                System.out.println("Unable to join all threads: Stopped unexpectedly");
            }

            synchronized (RateMonotonicScheduler.sync) {
                RateMonotonicScheduler.sync.notify();
            }

            System.out.println(thread1.getName() + " - Times ran: " + thread1.timesRun);
            System.out.println(thread2.getName() + " - Times ran: " + thread2.timesRun);
            System.out.println(thread3.getName() + " - Times ran: " + thread3.timesRun);
            System.out.println(thread4.getName() + " - Times ran: " + thread4.timesRun);

            System.out.println();

            System.out.println(thread1.getName() + " - Times overran: " + thread1.numOverruns);
            System.out.println(thread2.getName() + " - Times overran: " + thread2.numOverruns);
            System.out.println(thread3.getName() + " - Times overran: " + thread3.numOverruns);
            System.out.println(thread4.getName() + " - Times overran: " + thread4.numOverruns);


            return;
        }

        System.out.println("\nScheduler periods completed: " + ++schedulerPeriodsCompleted);

        scheduleThread(thread1, sem1);
        scheduleThread(thread2, sem2);
        scheduleThread(thread3, sem3);
        scheduleThread(thread4, sem4);
    }
}


