public class MESIBus extends Bus<MESI> {
    int numInvalidate;

    public MESIBus() {
        super();
        numInvalidate = 0;
    }

    public boolean invalidate(long address, MESI writer) {
        numInvalidate++;
        boolean waitForFlush = false;
        for (MESI m : caches) {
            if (m != writer) {
                m.invalidate(address);
                waitForFlush = true;
            }
        }
        return waitForFlush;
    }

    public void printStats() {
        super.printStats();
        System.out.printf("Number of invalidations on the bus: %d%n", numInvalidate);
    }
}
