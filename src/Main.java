import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static boolean continueExecution(ArrayList<MesiProcessor> processors) {
        for (MesiProcessor mp : processors) {
            if (mp.hasNext()) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) throws IOException {
        if (args.length < 6) {
            System.out.println("not enough arguments");
        }

        String dataType = args[2];
        int cacheSize = Integer.parseInt(args[3]);
        int associativity = Integer.parseInt(args[4]);
        int blockSize = Integer.parseInt(args[5]);

        ProtocolName protocolName = ProtocolName.MESI;
        if (args[1].equals("DRAGON")) {
            protocolName = ProtocolName.DRAGON;
            ArrayList<DragonProcessor> processors = new ArrayList<>();
            DragonBus bus = new DragonBus();
            for (int i = 0; i < 4; i++) {
                //"bodytrack_four/bodytrack_0.data"
                String filePath = String.format("%s_four/%s_%d.data", dataType, dataType, i);
                DragonProcessor processor = new DragonProcessor(cacheSize, associativity, blockSize,  filePath, bus);
                processors.add(processor);
            }

            for (DragonProcessor dp : processors) {
                bus.addCache(dp.dragonCache);
            }
            for (DragonProcessor mp : processors) {
                mp.executeInstructions();
            }
        } else if (args[1].equals("MESI")) {
            protocolName = ProtocolName.MESI;
            ArrayList<MesiProcessor> processors = new ArrayList<>();
            MESIBus bus = new MESIBus();
            for (int i = 0; i < 4; i++) {
                //"bodytrack_four/bodytrack_0.data"
                String filePath = String.format("%s_four/%s_%d.data", dataType, dataType, i);
                MesiProcessor processor = new MesiProcessor(cacheSize, associativity, blockSize,  filePath, bus);
                processors.add(processor);
            }

            for (MesiProcessor mp : processors) {
                bus.addCache(mp.mesiCache);
            }
            /*for (MesiProcessor mp : processors) {
                mp.executeInstructions();
            }*/
            long clockCycle = 0;

            while (continueExecution(processors)) {
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
}
