public class MESIBus extends Bus<MESI> {
    /*
    set all caches to be invalid
     */
    public void invalidate(long address) {
        for (MESI m : caches) {
            m.invalidate(address);
        }
    }
}
