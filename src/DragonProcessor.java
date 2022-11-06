import java.io.FileNotFoundException;
import java.io.IOException;

public class DragonProcessor extends Processor {
    Dragon dragonCache;

    DragonProcessor(int cacheSize, int associativity, int blockSize, String inputFile, DragonBus bus) throws FileNotFoundException {
        super.logger = new Logger(bus, blockSize);
        dragonCache = new Dragon(cacheSize, associativity, blockSize, bus, super.logger);
        super.reader = new InstructionReader(inputFile, super.logger);
    }

    void executeInstruction() throws IOException {
        if (reader.fetchNextIntruction()) {
            dragonCache.executeInstruction(reader.getNextInstruction());
        }
    }
}
