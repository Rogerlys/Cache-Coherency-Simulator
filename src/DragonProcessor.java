import java.io.FileNotFoundException;
import java.io.IOException;

public class DragonProcessor {
    Dragon dragonCache;
    String inputFile;
    int clock;
    int numBlocks;
    int numSets;
    int offset;
    int set;
    int tag;
    Logger logger;
    InstructionReader reader;

    DragonProcessor(int cacheSize, int associativity, int blockSize, String inputFile, DragonBus bus) throws FileNotFoundException {
        logger = new Logger(bus, blockSize);
        dragonCache = new Dragon(cacheSize, associativity, blockSize, bus, logger);
        reader = new InstructionReader(inputFile, logger);
    }

    void executeOneCycle(long clockCycle) throws IOException {
        if (logger.getTotalTime() > clockCycle) {
            return;
        }
        if (reader.hasNext) {
            executeInstruction();
        }
    }

    void executeInstruction() throws IOException {
        if (reader.fetchNextIntruction()) {
            dragonCache.executeInstruction(reader.getNextInstruction());
        }
    }

    void printInfo() {
        logger.printInfo();
    }

    boolean hasNext() {
        return reader.hasNext;
    }
}
