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
    private static boolean continueMoesiExecution(ArrayList<MOESIProcessor> processors) {
        for (MOESIProcessor mp : processors) {
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

        if (args[1].equals("Dragon")) {
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
                totalClock += dp.logger.getTotalTime();
            }

            System.out.println("Global Stats:");
            System.out.printf("Number of clock cycles: %d\n", totalClock);
            bus.printStats();
            System.out.println();
            //System.out.printf("data traffic %d/18001696, %f",  bus.trafficData, (bus.trafficData/18001696.0));

            for (DragonProcessor dp : processors) {
                dp.printInfo();
            }
        } else if (args[1].equals("MESI")) {
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
                totalClock += mp.logger.getTotalTime();
            }

            System.out.println("Global Stats:");
            System.out.printf("Number of clock cycles: %d\n", totalClock);
            bus.printStats();
            System.out.println();
            //System.out.printf("data traffic %d/18001696, %f",  bus.trafficData, (bus.trafficData/18001696.0));

            for (MesiProcessor mp : processors) {
                mp.printInfo();
            }
        }else if (args[1].equals("MOESI")) {
            ArrayList<MOESIProcessor> processors = new ArrayList<>();
            MOESIBus bus = new MOESIBus();
            for (int i = 0; i < 4; i++) {
                //"bodytrack_four/bodytrack_0.data"
                String filePath = String.format("%s_four/%s_%d.data", dataType, dataType, i);
                MOESIProcessor processor = new MOESIProcessor(cacheSize, associativity, blockSize, filePath, bus);
                processors.add(processor);
            }

            for (MOESIProcessor mp : processors) {
                bus.addCache(mp.moesiCache);
            }

            long clockCycle = 0;

            while (continueMoesiExecution(processors)) {
                for (MOESIProcessor mp : processors) {
                    mp.executeOneCycle(clockCycle);
                }
                clockCycle++;
            }
            long totalClock = 0;
            for (MOESIProcessor mp : processors) {
                totalClock += mp.logger.getTotalTime();
            }

            System.out.println("Global Stats:");
            System.out.printf("Number of clock cycles: %d\n", totalClock);
            bus.printStats();
            System.out.println();
            //System.out.printf("data traffic %d/18001696, %f",  bus.trafficData, (bus.trafficData/18001696.0));

            for (MOESIProcessor mp : processors) {
                mp.printInfo();
            }
        }
    }
}
