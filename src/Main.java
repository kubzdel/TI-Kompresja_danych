import java.io.FileNotFoundException;
import java.util.BitSet;

public class Main {

    public static void main(String args[])
    {

        Compression compression = new Compression();
        try {
            compression.process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
