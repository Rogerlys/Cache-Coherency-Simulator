public class Logger {
    public static int coreCount;
    int coreNum;
    long idleTime;
    long computeTime;
    long numLoad;
    long numStore;
    long numMiss;
    long totalInstruction;
    long privateDataAccess;
    long publicDataAccess;
    Bus bus;
    int blockSize;

    Logger(Bus bus, int blockSize) {
        this.coreNum = coreCount++;
        this.idleTime = 0;
        this.computeTime = 0;
        this.numLoad = 0;
        this.numStore = 0;
        this.numMiss = 0;
        this.totalInstruction = 0;
        this.privateDataAccess = 0;
        this.publicDataAccess = 0;
        this.bus = bus;
        this.blockSize = blockSize;
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

    long getTotalTime() {
        return idleTime + computeTime;
    }

    void incrementPublicDataAccess() {
        publicDataAccess++;
    }

    void incrementPrivateDataAccess() {
        privateDataAccess++;
    }

    void incrementDataTraffic() {
        bus.incrementDataTraffic(blockSize);
    }

    void printInfo() {
        System.out.printf("Core: %d%n", coreNum);
        System.out.printf("%d%n", idleTime + computeTime);
        System.out.printf("%d%n", computeTime);
        System.out.printf("%d%n", numLoad);
        System.out.printf("%d%n", numStore);
        System.out.printf("%d%n", idleTime);
        System.out.printf("%d/%d%n%f%n", numMiss, totalInstruction, (double) numMiss / totalInstruction);
        System.out.printf("%d%n", privateDataAccess);
        System.out.printf("%d", publicDataAccess);
        System.out.print("\n\n");
    }
}
