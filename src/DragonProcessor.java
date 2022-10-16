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
        logger = new Logger();
        dragonCache = new Dragon(cacheSize, associativity, blockSize, bus, logger);
        reader = new InstructionReader(inputFile, logger);
    }

    //todo change this to tick one clock cycle
    void executeInstructions() throws IOException {
        while(reader.fetchNextIntruction()) {
            dragonCache.executeInstruction(reader.getNextInstruction());
        }
        logger.printInfo();
    }

}
