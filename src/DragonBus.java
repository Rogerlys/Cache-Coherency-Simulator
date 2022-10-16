public class DragonBus extends Bus<Dragon> {
    public void update(long address) {
        for (Dragon d : caches) {
            d.update(address);
        }
    }
}
