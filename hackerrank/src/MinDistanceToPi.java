import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MinDistanceToPi {

    public static final BigInteger PI_ORIG = new BigInteger( "1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170");
//    public static final BigInteger PI_ORIG = new BigInteger( "84375");
    public static final BigInteger DI_ORIG = new BigInteger("10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
//    public static final BigInteger DI_ORIG = new BigInteger("100000");

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long min = in.nextLong();
        long max = in.nextLong();

        System.out.println(solve(min, max));
    }

    private static String solve(long min, long max) {
        Long[][] res = contFrac();
        Long[] ups = res[0];
        Long[] downs = res[1];

        for (int X = downs.length - 1; X >= 0; X--) {
            long a = (min - 1) / downs[X];
            long b = max / downs[X];
            if (a != b) {
                return String.format("%d/%d", ups[X] * (a + 1) + 3 * (downs[X] * (a + 1)), downs[X] * (a + 1));
            }
        }

        return String.format("%d/%d", 3 * min, min);
    }

    static Long[][] contFrac() {
        BigInteger PI = PI_ORIG;
        BigInteger DI = DI_ORIG;

        int n = 30;
        long i[] = new long[n];

        for (int j = 0; j < n; j++) {
            BigInteger div = PI.divide(DI);
            i[j] = div.longValue();
            PI = PI.subtract(DI.multiply(div));

            BigInteger tmp = PI;
            PI = DI;
            DI = tmp;
            
            if (DI.equals(BigInteger.ZERO)) break;
        }

        ArrayList<Long> ups = new ArrayList<>();
        ArrayList<Long> downs = new ArrayList<>();

        long prevUp = 0;
        long prevDown = 1;
        
        for (int j = 1; j < n; j++) {
            
            for (long last = (i[j]+1)/2; last <= i[j]; last++) {

                long up = 0;
                long down = 1;
                for (int k = j; k >= 0; k--) {
                    up = (k == j ? last : i[k]) * down + up;
                    long gcd = gcd(up, down);
                    up /= gcd;
                    down /= gcd;

                    long tmp = up;
                    up = down;
                    down = tmp;
                }

                long tmp = up;
                up = down;
                down = tmp;
                
                if (i[j] % 2 == 0 && last == i[j]/2) {
                    BigInteger errPrev = PI_ORIG.multiply(BigInteger.valueOf(prevDown)).subtract(DI_ORIG.multiply(BigInteger.valueOf(prevUp))).abs().multiply(BigInteger.valueOf(down));
                    BigInteger err = PI_ORIG.multiply(BigInteger.valueOf(down)).subtract(DI_ORIG.multiply(BigInteger.valueOf(up))).abs().multiply(BigInteger.valueOf(prevDown));
                    
                    if (err.equals(BigInteger.ZERO)) {
                        throw new RuntimeException();
                    }
                    
                    if (err.equals(errPrev)) {
                        throw new RuntimeException();
                    }
                    
                    if (err.compareTo(errPrev) >= 0) {
                        continue;
                    }
                }

                ups.add(up);
                downs.add(down);
                
                prevUp = up;
                prevDown = down;
            }
        }
        
//        Integer[] ind = new Integer[ups.size()];
//        for (int i1 = 0; i1 < ups.size(); i1++) {
//            ind[i1] = i1;
//        }
//        
//        Arrays.sort(ind, Comparator.comparingLong(downs::get));
//        
//        long[] upsPr = new long[ups.size()];
//        long[] downPr = new long[downs.size()];

//        for (int i1 = 0; i1 < ind.length; i1++) {
//            upsPr[i1] = ups.get(ind[i1]);
//            downPr[i1] = downs.get(ind[i1]);
//        }

//        System.err.println(Arrays.toString(ups));
//        System.err.println(Arrays.toString(downs));

        return new Long[][]{ups.toArray(new Long[0]), downs.toArray(new Long[0])};
    }

    private static long gcd(long up, long down) {
        if (up == 0 && down == 0) return 1;
        if (down == 0) return up;
        while (up != 0) {
            down = down % up;
            long tmp = down;
            down = up;
            up = tmp;
        }
        return down;
    }

    @Test
    public void test() {

        Long[][] longs = contFrac();

        for (int i = 0; i < longs[0].length; i++) {
            System.out.printf("%d\t%d%n", longs[0][i], longs[1][i]);
        }

        for (int i = 1; i < longs[1].length; i++) {
            if ((long) longs[1][i] == longs[1][i-1]) throw new RuntimeException(i + "");
        }
        
//        System.out.println(Arrays.toString(longs[0]));
//        System.out.println(Arrays.toString(longs[1]));

        System.out.println(solve(1, 1));
        System.out.println(solve(1, 2));
        System.out.println(solve(1, 3));
        System.out.println(solve(1, 4));
        System.out.println(solve(1, 5));
        System.out.println(solve(1, 6));
        System.out.println(solve(1, 7));
        System.out.println(solve(1, 8));
        System.out.println(solve(1, 9));
        System.out.println(solve(1, 10));
        System.out.println(solve(1, 1000000000000000L));
        System.out.println(solve(1000000000000000L, 1000000000000000L));
        System.out.println(solve(44485467702854L, 136308121570116L));
        System.out.println(solve(136308121570118L, 1000000000000000L));
    }
}
