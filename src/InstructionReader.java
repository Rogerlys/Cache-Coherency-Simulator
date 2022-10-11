import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InstructionReader {
    String filePath;
    BufferedReader br;
    boolean hasNext;
    Instruction nextInstruction;
    int numStore;
    int numLoad;
    InstructionReader(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        FileReader fr = new FileReader(filePath);
        br = new BufferedReader(fr);
        hasNext = true;
        numLoad = 0;
        numStore = 0;
    }

    boolean fetchNextIntruction() throws IOException {
        String line = br.readLine();
        if (line == null) {
            hasNext = false;
            return false;
        }
        nextInstruction = new Instruction(line);
        if (nextInstruction.value == 0) {
            numLoad++;
        } else if(nextInstruction.value == 1) {
            numStore++;
        }
        return true;
    }

    Instruction getNextInstruction() {
        return nextInstruction;
    }
}
