public class Node {
    Node prev;
    Node next;
    int key;
    boolean isValid;
    boolean isDirty;
    char state;
    Node(int key) {
        this.key  = key;
        isDirty = false;
        isValid = true;
        state = 'I';

    }

    public void setState(char state) {
        this.state = state;
    }

    public boolean canWrite() {
        return state == 'M' || state == 'E';
    }
}