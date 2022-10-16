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

    int getSetIndex(long address) {
        int setMask = (numSets - 1) << offset;
        return (int) (address & setMask) >> offset;
    }
    int getTag(long address) {
        long tagMask = ((0xFFFFFFFF >> set) >> offset) << set << offset;
        return (int) (address & tagMask) >> set >> offset;

    }

    void executeInstruction(Instruction i) {
        if (i.value == 0) {
            read(i.address);
            logger.incrementInstructionCount();

        } else if (i .value == 1){
            write(i.address);
            logger.incrementInstructionCount();

        } else if(i.value == 2) {
           logger.incrementComputeTime(i.address);
        }
    }

    void read(long address) {
        if (contains(address)) {
            logger.incrementIdleTime(1);
            logger.incrementPrivateDataAccess();
            return;
        }
        logger.incrementMiss();
        load(address);
    }

    void write(long address) {
        boolean miss = false;
        if (!contains(address)) {
            load(address);
            miss = true;
        } else {
            logger.incrementPrivateDataAccess();
            logger.incrementIdleTime(1);
        }
        CacheLine cacheLine = getCacheLine(address);
        if (cacheLine.getState() != 'M' || cacheLine.getState() != 'E') {
            bus.invalidate(address);

            miss = true;
        }
        //bus.invalidate(address);
        if (miss) {
            logger.incrementMiss();

        } else {
            logger.incrementPrivateDataAccess();
        }

        cacheLine.setDirty();
        cacheLine.setState('M');
    }

    void load(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cacheSet = sets[setIndex];
        int t = getTag(address);
        cacheSet.put(t);
        logger.incrementPublicDataAccess();
        if (bus.otherCacheContainsCache(address, this)) {
            // cache to cache sharing
            bus.share(address);
            //logger.incrementIdleTime(2 * (blockSize / 4));
            logger.incrementIdleTime(100);
        } else {
            // load from memory
            exclusive(address);
            logger.incrementIdleTime(100);
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

    void invalidate(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return;
        }

        CacheLine m =  cache.getCacheLine(tag);
        m.setState('I');
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
