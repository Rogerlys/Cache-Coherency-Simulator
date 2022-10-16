import java.util.*;

public class Bus<T extends Protocol> {
    ArrayList<T> caches;

    Bus() {
        this.caches = new ArrayList<>();
    }

    void addCache(T t) {
        caches.add(t);
    }

    /*
    set all caches to be shared
     */
    public void share(long address) {
        for (T t : caches) {
            t.share(address);
        }
    }

    /*
    check if other caches have the item if so no need to go to main memory
     */
    public <P extends Protocol> boolean otherCacheContainsCache(long address, P p) {
        for (T t : caches) {
            if (t != p) {
                if (t.contains(address)) {
                    return true;
                }
            }
        }
        return false;
    }
}
