import java.io.FileNotFoundException;
import java.io.IOException;

public class MOESIProcessor extends Processor {
    MOESI moesiCache;

    MOESIProcessor(int cacheSize, int associativity, int blockSize, String inputFile, MOESIBus bus) throws FileNotFoundException {
        super.logger = new Logger(bus, blockSize);
        moesiCache = new MOESI(cacheSize, associativity, blockSize, bus, super.logger);
        super.reader = new InstructionReader(inputFile, super.logger);
    }

    void executeInstruction() throws IOException {
        if (reader.fetchNextIntruction()) {
            moesiCache.executeInstruction(reader.getNextInstruction());
        }
    }
}