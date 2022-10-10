import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MESI {
    int associativity;
    int blockSize;
    LRUCache[] sets;
    int cacheSize;
    String inputFile;
    int clock;
    int numBlocks;
    int numSets;
    int offset;
    int set;
    int tag;
    MESI(int cacheSize, int associativity, int blockSize, String inputFile) {
        this.cacheSize = cacheSize;
        this.associativity = associativity;
        this.blockSize = blockSize;
        this.inputFile = inputFile;
        numBlocks = cacheSize / blockSize;
        numSets = numBlocks / associativity;
        clock = 0;
        offset = log2(blockSize);
        set = log2(numSets);
        tag = offset + set;
        sets = new LRUCache[numSets];
        for (int i = 0; i < numSets;i++) {
            sets[i] = new LRUCache(associativity);
        }
    }

    static int log2(int x) {
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

    String readInstructions() throws IOException {
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int hit = 0;
        int total = 0;
        int miss = 0;
        int load = 0;
        int store = 0;
        while( (line = br.readLine()) != null) {
            Instruction instruction = new Instruction(line);
            long address = instruction.address;
            int tag = getTag(address);
            int set = getSetIndex(address);

            if (instruction.value != 2) {
                LRUCache currSet = sets[set];
                if (currSet.get(tag) != -1) {
                    if (instruction.value == 1) {
                        miss++;
                    } else {
                        hit++;

                    }
                } else {
                    currSet.put(tag, 0);
                    miss++;
                }
                total++;
            }
            if (instruction.value == 0) {
                load++;
            }
            if (instruction.value == 1){
                store++;
            }

        }
        return String.format("hit:%d total:%d miss:%d load:%d, store:%d", hit, total, miss, load, store);
    }

}
