public class MESIBus extends Bus<MESI> {
    int numInvalidate = 0;
    /*
    set all caches to be invalid
     */
    public void invalidate(long address, MESI writer) {
        numInvalidate++;
        for (MESI m : caches) {
            if(m != writer) {
                m.invalidate(address);
            }

        }
    }
}
