public class Dragon extends Protocol {
    DragonBus bus;
    DragonLRUCache[] sets;

    Dragon(int cacheSize, int associativity, int blockSize, DragonBus bus, Logger logger) {
        super(cacheSize, associativity, blockSize, logger);
        this.bus = bus;
        sets = new DragonLRUCache[numSets];
        for (int i = 0; i < numSets;i++) {
            sets[i] = new DragonLRUCache(associativity, logger);
        }
    }

    // todo write miss calculation
    void write(long address) {
        boolean miss = false;
        if (!contains(address)) {
            load(address);
            miss = true;
        } else {
            logger.incrementIdleTime(1);
        }
        CacheLine cacheLine = getCacheLine(address);
        if (cacheLine.getState() == 'E') {
            cacheLine.setState('M');
        } else if(cacheLine.getState() == 'S') {
            if(bus.otherCacheContainsCache(address, this)) {
                bus.update(address);
                cacheLine.setState('D');
            } else {
                cacheLine.setState('M');
            }
        } else if(cacheLine.getState() == 'D') {
            if(bus.otherCacheContainsCache(address, this)) {
                bus.update(address);
            } else {
                cacheLine.setState('M');
            }
        }

        if (miss) {
            logger.incrementMiss();
        }
    }

    void load(long address) {
        int setIndex = getSetIndex(address);
        DragonLRUCache cacheSet = sets[setIndex];
        tag = getTag(address);
        cacheSet.put(tag);

        if (bus.otherCacheContainsCache(address, this)) {
            // cache to cache sharing
            bus.share(address);
            logger.incrementIdleTime(2 * (blockSize / 4));
        } else {
            // load from memory
            exclusive(address);
            logger.incrementIdleTime(100);
        }
    }

    CacheLine getCacheLine(long address) {
        int setIndex = getSetIndex(address);
        DragonLRUCache cache = sets[setIndex];
        int tag = getTag(address);
        return cache.getCacheLine(tag);
    }

    void share(long address) {
        int setIndex = getSetIndex(address);
        DragonLRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return;
        }

        CacheLine d = cache.getCacheLine(tag);
        if(d.getState() == 'M') {
            d.setState('D');
        } else if(d.getState() == 'E') {
            d.setState('S');
        }
    }

    void exclusive(long address) {
        int setIndex = getSetIndex(address);
        DragonLRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return;
        }

        CacheLine m = cache.getCacheLine(tag);
        m.setState('E');
    }

    void update(long address) {
        int setIndex = getSetIndex(address);
        DragonLRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
            return;
        }

        CacheLine d =  cache.getCacheLine(tag);
        if(d.getState() == 'D') {
            d.setState('S');
        }
    }

    public boolean contains(long address) {
        int setIndex = getSetIndex(address);
        DragonLRUCache cache = sets[setIndex];
        int tag = getTag(address);
        return cache.contains(tag);
    }
}
