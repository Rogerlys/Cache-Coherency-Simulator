import java.io.FileNotFoundException;
import java.io.IOException;

public class MesiProcessor {

    MESI mesiCache;
    String inputFile;
    int clock;
    int numBlocks;
    int numSets;
    int offset;
    int set;
    int tag;
    Logger logger;
    InstructionReader reader;
    MesiProcessor(int cacheSize, int associativity, int blockSize, String inputFile, Bus bus) throws FileNotFoundException {
        logger = new Logger();
        mesiCache = new MESI(cacheSize, associativity, blockSize, bus, logger);
        reader = new InstructionReader(inputFile, logger);
    }

    //todo change this to tick one clock cycle
    void executeInstructions() throws IOException {
        while(reader.fetchNextIntruction()) {
            mesiCache.executeInstruction(reader.getNextInstruction());
        }
        logger.printInfo();
    }

}
