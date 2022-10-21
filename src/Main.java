import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static boolean continueMesiExecution(ArrayList<MesiProcessor> processors) {
        for (MesiProcessor mp : processors) {
            if (mp.hasNext()) {
                return true;
            }
        }
        return false;
    }

    private static boolean continueDragonExecution(ArrayList<DragonProcessor> processors) {
        for (DragonProcessor dp : processors) {
            if (dp.hasNext()) {
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
        if (args[1].equals("Dragon")) {
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

            long clockCycle = 0;

            while (continueDragonExecution(processors)) {
                for (DragonProcessor dp : processors) {
                    dp.executeOneCycle(clockCycle);
                }
                clockCycle++;
            }
            long totalClock = 0;
            for (DragonProcessor dp : processors) {
                dp.printInfo();
                totalClock += dp.logger.getTotalTime();
            }
            System.out.println(totalClock);
            System.out.println(bus.numUpdate);
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

            long clockCycle = 0;

            while (continueMesiExecution(processors)) {
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
            System.out.println(totalClock);
            System.out.println(bus.numInvalidate);
        }
    }
}
