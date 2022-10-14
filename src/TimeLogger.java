public class TimeLogger {
    long idleTime;
    long computeTime;
    TimeLogger() {
        this.idleTime = 0;
        this.computeTime = 0;
    }

    void incrementIdleTime(long i) {
        idleTime += i;
    }

    void incrementComputeTime(long i) {
        computeTime += i;
    }
}
