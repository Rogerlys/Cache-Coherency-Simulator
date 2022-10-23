import java.io.FileNotFoundException;
import java.io.IOException;

public class MOESIProcessor {
    MOESI moesiCache;
    Logger logger;
    InstructionReader reader;

    MOESIProcessor(int cacheSize, int associativity, int blockSize, String inputFile, MOESIBus bus) throws FileNotFoundException {
        logger = new Logger(bus, blockSize);
        moesiCache = new MOESI(cacheSize, associativity, blockSize, bus, logger);
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
            moesiCache.executeInstruction(reader.getNextInstruction());
        }
    }

    void printInfo() {
        logger.printInfo();
    }

    boolean hasNext() {
        return reader.hasNext;
    }

}