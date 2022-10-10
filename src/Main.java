import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        if (args.length < 6) {
            System.out.println("not enough arguments");
        }

        Protocol protocol = Protocol.MESI;
        if (args[1] == "Dragon") {
            protocol = Protocol.DRAGON;
        } else if (args[1] == "Mesi") {
            protocol = Protocol.MESI;
        }


        String dataType = args[2];
        int cacheSize = Integer.parseInt(args[3]);
        int associativity = Integer.parseInt(args[4]);
        int blockSize = Integer.parseInt(args[5]);
        MESI mesi = new MESI(cacheSize, associativity, blockSize, "bodytrack_four/bodytrack_0.data");
        String s = mesi.readInstructions();
        System.out.println(s);


    }
}
