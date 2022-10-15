import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static boolean continueExecuetion(ArrayList<MesiProcessor> processors) {
        for (MesiProcessor mp : processors) {
            if (mp.hasNext()) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) throws IOException {
	// write your code here
        if (args.length < 6) {
            System.out.println("not enough arguments");
        }

        Protocol protocol = Protocol.MESI;
        if (args[1] == "Dragon") {
            protocol = Protocol.DRAGON;
        } else if (args[1] == "Mesi") {
            protocol = Protocol.MESI;
        }


        String dataType = args[2];
        int cacheSize = Integer.parseInt(args[3]);
        int associativity = Integer.parseInt(args[4]);
        int blockSize = Integer.parseInt(args[5]);
        //MESI MESI = new MESI();
        Bus bus = new Bus();
        ArrayList<MesiProcessor> processors = new ArrayList<>();
        for (int i = 0; i < 4;i++) {
            //"bodytrack_four/bodytrack_0.data"
            String filePath = String.format("%s_four/%s_%d.data", dataType, dataType, i);
            MesiProcessor processor = new MesiProcessor(cacheSize, associativity, blockSize,  filePath, bus);
            processors.add(processor);
        }

        for (MesiProcessor mp : processors) {
            bus.addCache(mp.mesiCache);
        }
        long clockCycle = 0;
/*
        for (MesiProcessor mp : processors) {
            mp.executeInstructions();
        }
*/
        while (continueExecuetion(processors)) {
            for (MesiProcessor mp : processors) {
                mp.executeOneCycle(clockCycle);
            }
            clockCycle++;
        }
        long totalClock = 0;
        for (MesiProcessor mp : processors) {
            mp.printInfo();
            totalClock += mp.logger.getTotalTime();
        }
        System.out.println(totalClock - 143315137);
        System.out.println(bus.numInvalidate);

    }


}
