import java.util.*;
class LRUCache {
    HashMap<Integer, Node> hmap;
    Node head;
    Node end;
    int capacity;
    public LRUCache(int capacity) {
        hmap = new HashMap();
        this.capacity = capacity;
    }

    public int get(int key) {
        if (!hmap.containsKey(key)) {
            return -1;
        }
        if (hmap.get(key) != end) {
            moveToEnd(key);


        }
        // print();
        return hmap.get(key).value;
    }

    public void put(int key, int value) {
        if (hmap.containsKey(key)) {
            moveToEnd(key);
            hmap.get(key).value = value;
            return;
        } else {
            Node n = new Node(key, value);


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
        if(hmap.size() > capacity) {
            hmap.remove(head.key);
            head.next.prev = null;
            head = head.next;
        }
        //print();
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

    void print() {
        Node h = head;
        System.out.print("head");
        while (h != null) {

            System.out.print(h.key + " ");
            h = h.next;
        }
        System.out.println();
        Node e = end;
        System.out.print("end");
        while (e != null) {

            System.out.print(e.key + " ");
            e = e.prev;
        }
        System.out.println();

    }

    public void setWrite(int i) {
        hmap.get(i).isWrite= true;
    }

    public boolean isWrite(int i) {
        return hmap.get(i).isWrite;
    }

    public void setRead(int i) {
        hmap.get(i).isWrite= false;
    }

}
