class ScheduledThread_Old extends Thread {

    private final int TIME_UNIT_LENGTH = 10;

    private int numTimesDoWork;

    ScheduledThread_Old(int numTimesDoWork) {
        this.numTimesDoWork = numTimesDoWork;
    }

    @Override
    public void run() {
        super.run();

        System.out.println("Thread running");
        for (int i = 0; i < numTimesDoWork; ++i){
            doWork();
        }

    }

    private void doWork() {
        int[][] matrix = new int[10][10];

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                matrix[j][i] *= 1d;
            }
        }
    }
}
