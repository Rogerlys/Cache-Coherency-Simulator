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
    int numMiss = 0;
    int totalInstruction = 0;
    Bus bus;
    MESI(int cacheSize, int associativity, int blockSize, Bus bus) {
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
            sets[i] = new MESILRUCache(associativity);
        }
        this.bus = bus;
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
            totalInstruction++;

        } else if (i .value == 1){
            write(i.address);
            totalInstruction++;

        }
    }

    void read(long address) {
        if (contains(address)) {

            return;
        }
        numMiss++;
        load(address);
        if (bus.otherCacheContainsCache(address, this)) {
            // cache to cache sharing
        }

    }

    void write(long address) {
        boolean miss = false;
        if (!contains(address)) {
            load(address);
            miss = true;
        }
        CacheLine cacheLine = getCacheLine(address);
        if (cacheLine.getState() != 'M' || cacheLine.getState() != 'E') {
            bus.invalidate(address);
            miss = true;
        }
        if (miss) {
            numMiss++;
        }
        cacheLine.setState('M');
    }
    int load(long address) {
        if (bus.otherCacheContainsCache(address, this)) {
            // cache to cache sharing
        } else {
            // load from memory
        }
        int setIndex = getSetIndex(address);
        MESILRUCache cacheSet = sets[setIndex];
        tag = getTag(address);
        cacheSet.put(tag);
        return 0;
    }

    int store(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cacheSet = sets[setIndex];
        tag = getTag(address);

        return 0;
    }

    CacheLine getCacheLine(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cache = sets[setIndex];
        int tag = getTag(address);
        if (!cache.contains(tag)) {
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

        CacheLine m =  cache.getCacheLine(tag);
        m.setState('S');
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
