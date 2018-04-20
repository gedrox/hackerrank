package tests;

import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Arrays;
import java.util.Random;

public class BinSearchH2o {
    
    @Test
    public void test() {
        long x = 1L << 62;
        System.out.println(x);

        LocalDate d = LocalDate.of(1900, 1, 1);
        System.out.println(d.plus(255, IsoFields.QUARTER_YEARS));
        System.out.println(d.plus(256, IsoFields.QUARTER_YEARS));
    }
    
    public static void main(String[] args) {
//        double[] x = {1,1,1,1,1,1,1,1,1,1};
//        int res = Arrays.binarySearch(x, 1);
//        System.out.println(res);
        
        int L = 4;
        
        char[] search = "Merry".toCharArray();
        char[] answer = new char[L];
        
        long seed = 0;
        nextSeed:
        while (true) {
            seed++;
            
            Random r = new Random(seed);
            for (int i = 0; i < L; i++) {
                int ch = 32 + r.nextInt(91);
                answer[i] = (char) ch;
                if (ch != search[i]) {
                    continue nextSeed;
                }
            }

            System.out.println(seed);
            break;
        }

        //28019227 28St
        //87617041 one!
        System.out.println(new String(answer));
    }
}
