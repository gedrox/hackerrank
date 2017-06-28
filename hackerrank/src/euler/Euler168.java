package euler;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Euler168 {

    public static void main(String[] args) {
        int m = new Scanner(System.in).nextInt();
        long answer = 0;
        BigInteger ten = BigInteger.TEN;
        
        BigInteger[] oneToNine = new BigInteger[9];
        for (int i = 0; i < 9; i++) {
            oneToNine[i] = BigInteger.valueOf(i + 1);
        }
        
        for (int x = 1; x < m; x++) {
            for (BigInteger l : oneToNine) {
                for (BigInteger A : oneToNine) {
                    BigInteger up = A.multiply(ten.subtract(l));
                    BigInteger down = BigInteger.TEN.multiply(l).subtract(BigInteger.ONE);
                    if (up.mod(down).equals(BigInteger.ZERO)) {
                        BigInteger B = BigInteger.TEN.multiply(up.divide(down)).add(A);
                        if (B.compareTo(ten) < 0) continue;
                        
                        int rem = B.mod(BigInteger.valueOf(100000)).intValue();
                        answer += rem;
                        answer %= 100000;
                    }
                }
            }
            ten = ten.multiply(BigInteger.TEN);
        }

        System.out.println(answer);
    }
}