public class DragonBus extends Bus<Dragon> {
    int numUpdate = 0;

    public void update(long address) {
        numUpdate++;
        for (Dragon d : caches) {
            d.update(address);
        }
    }
}
