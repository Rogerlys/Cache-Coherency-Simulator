public class DragonBus extends Bus<Dragon> {
    int numUpdate;

    public DragonBus() {
        super();
        numUpdate = 0;
    }

    public void update(long address, int blockSize) {
        numUpdate++;
        incrementDataTraffic(blockSize);
        for (Dragon d : caches) {
            d.update(address);
        }
    }

    public void printStats() {
        super.printStats();
        System.out.printf("Number of updates on the bus: %d%n", numUpdate);
    }
}
