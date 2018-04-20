package kickstart2018.A;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ANoOddDigits {
    public static void main(String[] args) throws IOException {
        String fn = new Scanner(System.in).next();
        Scanner f = new Scanner(new File(fn));
        String out = fn + ".out";
        StringBuilder sb = new StringBuilder();
        int q = f.nextInt();
        for (int q_i = 0; q_i < q; q_i++) {
            long n = f.nextLong();
            
            int[] digits = new int[18];
            long nc = n;
            int max = 0;
            for (int i = 0; i < 17; i++) {
                digits[i] = (int) (nc % 10);
                nc = nc / 10;
                if (digits[i] != 0) {
                    max = i;
                }
            }
            
            long countPlus = 0, countMinus = 0;
            // +
            for (int i = max; i >= 0; i--) {
                if (digits[i] % 2 != 0) {
                    countPlus = 1;
                    if (digits[i] == 9) {
                        countPlus = Long.MAX_VALUE;
                        break;
                    }
                    for (int j = i - 1; j >= 0; j--) {
                        countPlus *= 10;
                        countPlus -= digits[j];
                    }
                    break;
                }
            }
            
            // -
            for (int i = max; i >= 0; i--) {
                if (digits[i] % 2 != 0) {
                    countMinus = 1;
                    for (int j = i - 1; j >= 0; j--) {
                        countMinus *= 10;
                        countMinus += digits[j] - 8;
                    }
                    break;
                }
            }

            long answer = Math.min(countPlus, countMinus);
            
//            long br = brute(n);
//            if (answer != br) {
//                throw new RuntimeException(n + "");
//            }

            sb.append("Case #").append(q_i + 1).append(": ").append(answer).append('\n');
        }

        System.out.println(sb.toString());
        Files.write(Paths.get(out), sb.toString().getBytes());
    }
    
    static long brute(long n) {
        long pl = 0;
        while (!String.valueOf(n + pl).matches("[02468]+")) {
            pl++;
        }

        long min = 0;
        while (!String.valueOf(n - min).matches("[02468]+")) {
            min++;
        }
        
        return Math.min(pl, min);
    }
    
    @Test
    public void test() {
        System.out.println(brute(99));
    }
}
