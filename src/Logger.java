public class Logger {
    private static int coreCount;
    int coreNum;
    long idleTime;
    long computeTime;
    long numLoad;
    long numStore;
    long numMiss;
    long totalInstruction;
    long numPrivate;
    long numPublic;

    Logger() {
        this.coreNum = coreCount++;
        this.idleTime = 0;
        this.computeTime = 0;
        this.numLoad = 0;
        this.numStore = 0;
        this.numMiss = 0;
        this.totalInstruction = 0;
        this.numPrivate = 0;
        this.numPublic = 0;
    }

    void incrementIdleTime(long i) {
        idleTime += i;
    }

    void incrementComputeTime(long i) {
        computeTime += i;
    }

    void incrementLoad() {
        numLoad++;
    }

    void incrementStore() {
        numStore++;
    }

    void incrementMiss() {
        numMiss++;
    }

    void incrementInstructionCount() {
        totalInstruction++;
    }

    void incrementPrivate() {
        numPrivate++;
    }

    void incrementPublic() {
        numPublic++;
    }

    long getTotalTime() {
        return idleTime + computeTime;
    }

    void printInfo() {
        System.out.printf("Core: %d%n", coreNum);
        System.out.printf("- Execution cycles for core: %d%n", idleTime + computeTime);
        System.out.printf("- Number of compute cycles: %d%n", computeTime);
        System.out.printf("- Number of load instructions: %d%n", numLoad);
        System.out.printf("- Number of store instructions: %d%n", numStore);
        System.out.printf("- Number of idle cycles: %d%n", idleTime);
        System.out.printf("- Data cache miss rate: %d/%d %f%n", numMiss, totalInstruction, (double) numMiss / totalInstruction);
        System.out.printf("- Bus data traffic: N/A%n");
        System.out.printf("- Number of invalidations/updates on the bus: N/A%n");
        System.out.printf("- Number of private data access: N/A%n");
        System.out.printf("- Number of shared data access: N/A%n");
        System.out.println();
    }
}
