import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class TestSmth {

    public static void main(String[] args) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("zeroes"));
        out.write("200000".getBytes());
        out.write('\n');
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append('0');
        }
        String s = sb.toString();
        for (int i = 0; i < 200000; i++) {
            out.write(s.getBytes());
            out.write('\n');
        }
        out.close();

        int[] ar = new int[10];
        Integer[] res = new Integer[10];
        Arrays.sort(res, Comparator.comparing(i -> ar[i]));
    }
}
