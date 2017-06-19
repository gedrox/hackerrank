package thegreaterxor;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class GreaterXor {

    static long[] POW2 = new long[34];
    static {
        POW2[0] = 1;
        for (int i = 1; i < 34; i++) {
            POW2[i] = POW2[i - 1] * 2;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();

        StringBuilder sb = new StringBuilder();

        for(int a0 = 0; a0 < q; a0++){
            long x = in.nextLong();

            int[] bits = new int[34];
            int i = 0;
            for (; i < 34 && x > 0; i++) {
                bits[i] = (int) (x % 2);
                x = x / 2;
            }
            i--;

            if (x != 0) throw new RuntimeException();

            long res = 0;

            for (; i >= 0; i--) {
                if (bits[i] == 0) {
                    res += POW2[i];
                }
            }

            sb.append(res).append('\n');
        }


        System.out.println(sb.toString());
    }

    @Test
    public void generate() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(100000).append('\n');
        for (int i = 0; i < 100000; i++) {
            sb.append(10000000000L).append('\n');
        }
        Files.write(Paths.get("in.txt"), sb.toString().getBytes());
    }
}
