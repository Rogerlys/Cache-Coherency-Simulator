import java.util.*;

class MESILRUCache {
    HashMap<Integer, Node> hmap;
    Node head;
    Node end;
    int capacity;
    public MESILRUCache(int capacity) {
        hmap = new HashMap();
        this.capacity = capacity;
    }

    public boolean contains(int key) {
        if (!hmap.containsKey(key)) {
            return false;
        }
        if (hmap.get(key) != end) {
            moveToEnd(key);
        }
        // print();
        return true;
    }

    public void put(int key) {
        if (hmap.containsKey(key)) {
            moveToEnd(key);

            return;
        } else {
            Node n = new Node(key);


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

        // todo when we evict the block need to check if need to write back
        if(hmap.size() > capacity) {
            hmap.remove(head.key);
            head.next.prev = null;
            head = head.next;
        }

    }



    void moveToEnd(int key) {
        Node n = hmap.get(key);
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

    boolean readCache(int tag) {
        if (contains(tag)) {
            Node block = hmap.get(tag);
            block.setState('S');
            return true;
        } else {
            put(tag);
        }
        return false;
    }

    boolean writeCahce(int tag) {
        if (contains(tag)) {
            Node block = hmap.get(tag);
            if (block.canWrite()) {
                return true;
            }
            block.setState('M');

        } else {
            put(tag);
            Node block = hmap.get(tag);
            block.setState('M');
            return false;
        }
        return false;
    }
}
