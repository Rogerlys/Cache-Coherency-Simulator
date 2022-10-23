public abstract class Protocol {
    int associativity;
    int blockSize;
    int cacheSize;
    int numBlocks;
    int numSets;
    int offset;
    int set;
    int tag;
    Logger logger;

    Protocol(int cacheSize, int associativity, int blockSize, Logger logger) {
        this.cacheSize = cacheSize;
        this.associativity = associativity;
        this.blockSize = blockSize;
        this.logger = logger;
        numBlocks = cacheSize / blockSize;
        numSets = numBlocks / associativity;
        offset = log2(blockSize);
        set = log2(numSets);
        tag = offset + set;
    }

    abstract void write(long address);
    abstract void load(long address);
    abstract void share(long address);
    abstract boolean contains(long address);
    abstract CacheLine getCacheLine(long address);

    int log2(int x) {
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

    void read(long address) {
        if (contains(address)) {

            logger.incrementIdleTime(1);
        } else {
            logger.incrementMiss();
            load(address);
        }
        countPrivatePublicAccess(address);
    }

    void countPrivatePublicAccess(long address) {
        CacheLine cacheLine = getCacheLine(address);
        if (cacheLine.getState() == 'E' || cacheLine.getState() == 'M') {
            logger.incrementPrivateDataAccess();
        } else {
            logger.incrementPublicDataAccess();
        }
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
}
