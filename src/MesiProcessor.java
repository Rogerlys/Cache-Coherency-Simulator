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
    MesiProcessor(int cacheSize, int associativity, int blockSize, String inputFile) throws FileNotFoundException {
        mesiCache = new MESI(cacheSize, associativity, blockSize);
        reader = new InstructionReader(inputFile);

    }

    void readInstruction() throws IOException {
        Instruction next = reader.getNextInstruction();
        reader.fetchNextIntruction();
    }

    //todo change this to tick one clock cycle
    void executeInstructions() throws IOException {
        while(reader.fetchNextIntruction()) {
            mesiCache.executeInstruction(reader.getNextInstruction());
        }
        System.out.println(String.format("load:%d store:%d", reader.numLoad, reader.numStore));
        System.out.println(String.format("miss rate %d/%d", mesiCache.numMiss, mesiCache.totalInstruction));
    }

}
