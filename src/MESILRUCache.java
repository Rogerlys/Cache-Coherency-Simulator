import java.util.*;

class MESILRUCache {
    HashMap<Integer, CacheLine> hmap;
    CacheLine head;
    CacheLine end;
    int capacity;
    Logger logger;
    public MESILRUCache(int capacity, Logger logger) {
        hmap = new HashMap();
        this.capacity = capacity;
        this.logger = logger;
    }

    public boolean contains(int key) {
        if (!hmap.containsKey(key) || hmap.get(key).getState() == 'I') {
            return false;
        }
        if (hmap.get(key) != end) {
            moveToEnd(key);
        }
        return true;
    }

    public void put(int key) {
        if (hmap.containsKey(key)) {
            moveToEnd(key);
            hmap.get(key).setState('S');
            return;
        } else {
            CacheLine n = new CacheLine(key);
            if (head == null) {
                head = n;
            }
            if (end != null) {
                end.next = n;
                n.prev = end;
            }
            end = n;
            hmap.put(key, n);
        }

        if (hmap.size() > capacity) {
            if (head.getDirty()) {
                // write back to cache
                logger.incrementIdleTime(100);
            }
            hmap.remove(head.key);
            head.next.prev = null;
            head = head.next;
        }

    }

    void moveToEnd(int key) {
        CacheLine n = hmap.get(key);
        if(n == end) {

        }
        else if (n == head) {
            head = n.next;
            head.prev = null;
            n.next = null;
            end.next = n;
            n.prev = end;
            end = n;
        }  else {
            if (n.prev != null) {
                n.prev.next = n.next;
            }
            if (n.next != null) {
                n.next.prev = n.prev;
            }
            n.next = null;
            end.next = n;
            n.prev = end;
            end = n;
        }

    }

    public CacheLine getCacheLine(int tag) {
        return hmap.get(tag);
    }
}
