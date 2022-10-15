import java.lang.reflect.Array;
import java.util.*;
public class Bus {
    //make this a abstract class so this can be reused for MOESI and dragon
    public ArrayList<MESI> caches;
    Bus() {
        this.caches = new ArrayList<>();
    }
    int numUpdate = 0;
    int numInvalidate = 0;

    void addCache(MESI mesi) {
        caches.add(mesi);
    }
    /*
    set all caches to be invalid
     */
    public void invalidate(long address) {
        for (MESI m : caches) {
            m.invalidate(address);
        }
    }
    /*
    set all caches to be shared
     */
    public void share(long address) {
        for (MESI m : caches) {
            m.share(address);
        }
    }

    /*
    check if other caches have the item if so no need to go to main memory
     */
    public boolean otherCacheContainsCache(long address, MESI mesi) {
        for (MESI m : caches) {
            if (m != mesi) {
                if (m.contains(address)) {
                    return true;
                }
            }
        }
        return false;
    }
}
