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
    InstructionReader reader;
    MesiProcessor(int cacheSize, int associativity, int blockSize, String inputFile, Bus bus) throws FileNotFoundException {
        mesiCache = new MESI(cacheSize, associativity, blockSize, bus);
        reader = new InstructionReader(inputFile);

    }


    //todo change this to tick one clock cycle
    void executeInstructions() throws IOException {
        while(reader.fetchNextIntruction()) {
            mesiCache.executeInstruction(reader.getNextInstruction());
        }
        System.out.println(String.format("load:%d store:%d", reader.numLoad, reader.numStore));
        System.out.println(String.format("miss rate %d/%d CPU time %d idle Time %d", mesiCache.numMiss, mesiCache.totalInstruction, mesiCache.cpuTime, mesiCache.idleTime));
    }

}
