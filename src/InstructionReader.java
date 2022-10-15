import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InstructionReader {
    String filePath;
    Logger logger;
    BufferedReader br;
    boolean hasNext;
    Instruction nextInstruction;
    InstructionReader(String filePath, Logger logger) throws FileNotFoundException {
        this.filePath = filePath;
        this.logger = logger;
        FileReader fr = new FileReader(filePath);
        br = new BufferedReader(fr);
        hasNext = true;
    }

    boolean fetchNextIntruction() throws IOException {
        String line = br.readLine();
        if (line == null) {
            hasNext = false;
            return false;
        }
        nextInstruction = new Instruction(line);
        if (nextInstruction.value == 0) {
            logger.incrementLoad();
        } else if(nextInstruction.value == 1) {
            logger.incrementStore();
        }
        return true;
    }

    Instruction getNextInstruction() {
        return nextInstruction;
    }
}
