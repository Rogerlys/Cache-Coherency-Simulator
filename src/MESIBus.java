public class MESIBus extends Bus<MESI> {
    int numInvalidate;

    public MESIBus() {
        super();
        numInvalidate = 0;
    }

    public void invalidate(long address, MESI writer) {
        numInvalidate++;
        for (MESI m : caches) {
            if (m != writer) {
                m.invalidate(address);
            }
        }
    }

    public void printStats() {
        super.printStats();
        System.out.printf("Number of invalidations on the bus: %d%n", numInvalidate);
    }
}
