public class Instruction {
    int value;
    long address;
    Instruction(String s) {
        String[] arr = s.split(" ");
        this.value = Integer.parseInt(arr[0]);
        this.address = Integer.parseInt(arr[1].substring(2), 16);
    }
}
