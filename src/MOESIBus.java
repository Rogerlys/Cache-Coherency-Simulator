public class MOESIBus extends Bus<MOESI> {
    int numInvalidate;
    int numUpdate;

    public MOESIBus() {
        super();
        this.numInvalidate = 0;
        this.numUpdate = 0;
    }

    @Override
    public void share(long address) {
        MESI containsO = getCacheContainingOState(address);
        if(containsO != null) {
            containsO.share(address);
            return;
        }

        for (MESI m : caches) {
            m.share(address);
        }
    }

    private MESI getCacheContainingOState(long address) {
        for (MESI m : caches) {

            MESILRUCache cache = m.sets[m.getSetIndex(address)];
            int tag = m.getTag(address);
            if (cache.containsO(tag))
                return m;
        }
        return null;
    }

    public void invalidate(long address, MOESI writer) {
        numInvalidate++;
        for (MOESI m : caches) {
            if (m != writer) {
                m.invalidate(address);
            }
        }
    }

    public void update(long address, int blockSize) {
        numUpdate++;
        incrementDataTraffic(blockSize);
    }

    public void printStats() {
        super.printStats();
        System.out.printf("Number of invalidations on the bus: %d%n", numInvalidate);
        System.out.printf("Number of updates on the bus: %d%n", numUpdate);
    }
}