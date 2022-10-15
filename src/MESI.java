public class MESI extends Cache{
    int associativity;
    int blockSize;
    MESILRUCache[] sets;
    int cacheSize;

    int numBlocks;
    int numSets;
    int offset;
    int set;
    int tag;
    Bus bus;
    Logger logger;

    MESI(int cacheSize, int associativity, int blockSize, Bus bus, Logger logger) {
        this.cacheSize = cacheSize;
        this.associativity = associativity;
        this.blockSize = blockSize;
        numBlocks = cacheSize / blockSize;
        numSets = numBlocks / associativity;
        offset = log2(blockSize);
        set = log2(numSets);
        tag = offset + set;
        sets = new MESILRUCache[numSets];
        for (int i = 0; i < numSets;i++) {
            sets[i] = new MESILRUCache(associativity, logger);
        }
        this.bus = bus;
        this.logger = logger;

    }

    static int log2(int x) {
        return (int)(Math.log(x) / Math.log(2));
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

    boolean invalidate(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return false;
        }

        CacheLine m =  cache.getCacheLine(tag);
        if (m.getState() != 'I') {
            m.setState('I');
            return true;
        }
        return false;
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
