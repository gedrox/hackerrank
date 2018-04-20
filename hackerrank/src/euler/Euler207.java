package euler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;

import static java.math.BigInteger.ONE;

public class Euler207 {
    
    static BigInteger TWO = BigInteger.valueOf(2);

    public static void main(String[] args) throws IOException {
        int q = (int) readLong();

        BigInteger pow2 = ONE;
        BigInteger pow2p1 = TWO;
        
        Div[] divs = new Div[65];
        
        StringBuilder sb = new StringBuilder();
        
        for (int m_cand = 1; m_cand <= 65; m_cand++) {
            pow2 = pow2.multiply(TWO);
            pow2p1 = pow2p1.multiply(TWO);
            
            divs[m_cand - 1] = new Div(BI(m_cand), pow2p1.subtract(TWO));
        }
        
        for (int q_i = 0; q_i < q; q_i++) {
            BigInteger a_BI = BI(readLong());
            BigInteger b_BI = BI(readLong());

            int pos = Arrays.binarySearch(divs, new Div(a_BI, b_BI));
            if (pos >= 0) {
                pos++;
            } else {
                pos = - pos - 1;
            }
            
            int m = pos + 1;
            
            BigInteger divider = BI(m).multiply(b_BI).divide(a_BI).add(ONE);
            sb.append(divider.multiply(divider.add(ONE))).append('\n');
        }

        System.out.print(sb);
    }
    
    private static BigInteger BI(long a) {
        return BigInteger.valueOf(a);
    }
    
    static class Div implements Comparable<Div> {
        final BigInteger up, down;

        Div(BigInteger up, BigInteger down) {
            this.up = up;
            this.down = down;
        }

        @Override
        public int compareTo(Div o) {
            return -up.multiply(o.down).compareTo(o.up.multiply(down));
        }
    }

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static long readLong() throws IOException {
        long res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }
}
