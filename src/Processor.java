import java.io.IOException;

public abstract class Processor {
    Logger logger;
    InstructionReader reader;

    abstract void executeInstruction() throws IOException;

    void executeOneCycle(long clockCycle) throws IOException {
        if (logger.getTotalTime() > clockCycle) {
            return;
        }
        if (reader.hasNext) {
            executeInstruction();
        }
    }

    boolean hasNext() {
        return reader.hasNext;
    }

    void printInfo() {
        logger.printInfo();
    }
}
