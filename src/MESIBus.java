public class MESIBus extends Bus<MESI> {
    int numInvalidate = 0;
    /*
    set all caches to be invalid
     */
    public void invalidate(long address) {
        numInvalidate++;
        for (MESI m : caches) {
            m.invalidate(address);
        }
    }
}
