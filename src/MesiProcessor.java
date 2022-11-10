import java.io.FileNotFoundException;
import java.io.IOException;

public class MesiProcessor extends Processor {
    MESI mesiCache;

    MesiProcessor(int cacheSize, int associativity, int blockSize, String inputFile, MESIBus bus) throws FileNotFoundException {
        super.logger = new Logger(bus, blockSize);
        mesiCache = new MESI(cacheSize, associativity, blockSize, bus, super.logger);
        super.reader = new InstructionReader(inputFile, super.logger);
    }

    void executeInstruction() throws IOException {
        if (reader.fetchNextIntruction()) {
            mesiCache.executeInstruction(reader.getNextInstruction());
        }
    }

    void printInfo() {
        logger.printInfo();
    }
}
