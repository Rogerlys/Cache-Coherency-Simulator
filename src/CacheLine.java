public class CacheLine {
    CacheLine prev;
    CacheLine next;
    int key;
    boolean isValid;
    boolean isDirty;
    char state;
    CacheLine(int key) {
        this.key  = key;
        isDirty = false;
        isValid = true;
        state = 'S';

    }

    public void setState(char state) {
        this.state = state;
    }

    public char getState() {
        return state;
    }


}