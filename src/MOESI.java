public class MOESI extends MESI {
    MOESIBus moesiBus;

    MOESI(int cacheSize, int associativity, int blockSize, MOESIBus bus, Logger logger) {
        super(cacheSize, associativity, blockSize, null, logger);
        this.moesiBus = bus;
    }

    void write(long address) {
        if (!contains(address)) {
            load(address);
            logger.incrementMiss();
        } else {
            logger.incrementIdleTime(1);
        }
        CacheLine cacheLine = getCacheLine(address);
        
        if (cacheLine.getState() == 'E') {
            cacheLine.setState('M');
        } else if (cacheLine.getState() == 'O') {
            if (moesiBus.otherCacheContainsCache(address, this)) {
                moesiBus.update(address, blockSize);
                logger.incrementIdleTime(2 * (blockSize / 4));
            } else {
                cacheLine.setState('M');
            }
        } else if (cacheLine.getState() == 'S') {
            if (moesiBus.otherCacheContainsCache(address, this)) {
                moesiBus.invalidate(address, this);
            }
            cacheLine.setState('M');
        }
        cacheLine.setDirty();
        countPrivatePublicAccess(address);
    }

    void invalidate(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) return;

        CacheLine m =  cache.getCacheLine(tag);
        if (m.getState() == 'O' || m.getState() == 'M') {
            logger.incrementIdleTime(100);
            bus.incrementDataTraffic(blockSize);
            m.isDirty = false;
        }
        m.setState('I');
    }

    void share(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) return;

        CacheLine m = cache.getCacheLine(tag);
        if (m.getState() != 'O') m.setState('S');
        logger.incrementIdleTime(2 * (blockSize / 4));
        bus.incrementDataTraffic(blockSize);
    }
}
