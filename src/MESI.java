public class MESI extends Protocol {
    MESIBus bus;
    MESILRUCache[] sets;

    MESI(int cacheSize, int associativity, int blockSize, MESIBus bus, Logger logger) {
        super(cacheSize, associativity, blockSize, logger);
        this.bus = bus;
        sets = new MESILRUCache[numSets];
        for (int i = 0; i < numSets;i++) {
            sets[i] = new MESILRUCache(associativity, logger);
        }
        this.bus = bus;
        this.logger = logger;

    }

    void write(long address) {
        boolean miss = false;
        if (!contains(address)) {
            load(address);
            miss = true;
        } else {

            logger.incrementIdleTime(1);
        }
        CacheLine cacheLine = getCacheLine(address);
        if (cacheLine.getState() != 'M' || cacheLine.getState() != 'E') {
            bus.invalidate(address, this);
            miss = true;

        }

        if (miss) {
            logger.incrementMiss();

        }
        cacheLine.setDirty();
        cacheLine.setState('M');

    }

    void load(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cacheSet = sets[setIndex];
        int t = getTag(address);
        cacheSet.put(t);

        if (bus.otherCacheContainsCache(address, this)) {
            // cache to cache sharing
            bus.share(address);
            logger.incrementIdleTime(2 * (blockSize / 4));
            bus.incrementDataTraffic(blockSize);
            MESI sharer = bus.getCacheSharer(address, this);
            CacheLine cacheLine = getCacheLine(address);
            cacheLine.setState('S');
        } else {
            // load from memory
            exclusive(address);
            logger.incrementIdleTime(100);
            CacheLine cacheLine = getCacheLine(address);
            cacheLine.setState('E');
        }
    }

    CacheLine getCacheLine(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!contains(address)) {
            return null;
        }
        return cache.getCacheLine(tag);
    }

    boolean invalidate(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return false;
        }

        CacheLine m =  cache.getCacheLine(tag);
        m.setState('I');
        return true;
    }

    void share(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return;
        }

        CacheLine m = cache.getCacheLine(tag);
        m.setState('S');
    }

    void exclusive(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return;
        }

        CacheLine m = cache.getCacheLine(tag);
        m.setState('E');
    }

    public boolean contains(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return false;
        }
        return cache.getCacheLine(tag).getState() != 'I';
    }
}
