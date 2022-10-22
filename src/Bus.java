import java.util.*;

public class Bus<T extends Protocol> {
    ArrayList<T> caches;
    int trafficData;

    Bus() {
        this.caches = new ArrayList<>();
        this.trafficData = 0;
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

    public <P extends Protocol> T getCacheSharer(long address, P p) {
        for (T t : caches) {
            if (t != p) {
                if (t.contains(address)) {
                   return t;
                }
            }
        }
        return null;
    }

    void incrementDataTraffic(int i) {
        trafficData += i;
    }
}
