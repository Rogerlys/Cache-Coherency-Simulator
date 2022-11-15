import java.util.HashMap;

public abstract class LRUCache {
    HashMap<Integer, CacheLine> hmap;
    CacheLine head;
    CacheLine end;
    int capacity;
    Logger logger;

    public LRUCache(int capacity, Logger logger) {
        hmap = new HashMap<>();
        this.capacity = capacity;
        this.logger = logger;
    }

    public abstract void removeHead();

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
            int invalidTag = findInvalid();
            if (invalidTag != -1) {
                moveToHead(invalidTag);
            }
            removeHead();

        }
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

    public boolean containsO(int key) {
        if (!hmap.containsKey(key) || hmap.get(key).getState() != 'O') {
            return false;
        }

        return true;
    }

    void moveToEnd(int key) {
        CacheLine n = hmap.get(key);
        if(n == end) {

        } else if (n == head) {
            head = n.next;
            head.prev = null;
            n.next = null;
            end.next = n;
            n.prev = end;
            end = n;
        } else {
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

    void moveToHead(int key) {
        CacheLine n = hmap.get(key);
        if(n == end) {
            end = n.prev;
            end.next = null;
            n.prev = null;
            head.prev = head;
            n.next = head;
            head = end;
        } else if (n == head) {

        } else {
            if (n.prev != null) {
                n.prev.next = n.next;
            }
            if (n.next != null) {
                n.next.prev = n.prev;
            }
            n.prev = null;
            head.prev = n;
            n.next = head;
            head = n;
        }
    }

    int findInvalid() {
        CacheLine h = head;
        while (h != null) {
            if (h.getState() == 'I') {
                return h.key;
            }
            h = h.next;
        }
        return -1;
    }
}
