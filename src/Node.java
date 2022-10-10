public class Node {
    Node prev;
    Node next;
    int value;
    int key;
    boolean isWrite;
    Node(int key, int value) {
        this.value = value;
        this.key  = key;
        isWrite = false;
    }
}