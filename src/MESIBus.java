public class MESIBus extends Bus<MESI> {
    int numInvalidate = 0;
    /*
    set all caches to be invalid
     */
    public void invalidate(long address, MESI writer) {

        for (MESI m : caches) {
            if(m != writer) {
                if(m.invalidate(address)) {
                    numInvalidate++;
                }
            }

        }
    }
}
