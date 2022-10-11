public class MESI {
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
    MESI(int cacheSize, int associativity, int blockSize) {
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
            load(i.address);
            totalInstruction++;

        } else if (i .value == 1){
            store(i.address);
            totalInstruction++;

        }
    }
    int load(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cacheSet = sets[setIndex];
        tag = getTag(address);
        if (!cacheSet.readCache(tag)) {
            numMiss++;
        }
        return 0;
    }

    int store(long address) {
        int setIndex = getSetIndex(address);
        MESILRUCache cacheSet = sets[setIndex];
        tag = getTag(address);
        if (!cacheSet.writeCahce(tag)) {
            numMiss++;
        }
        return 0;
    }

}
