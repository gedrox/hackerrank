import java.math.BigInteger;
import java.util.Scanner;

public class euler28 {
    static int MOD = 1000000007;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();

        int T = sc.nextInt();
        for (int i = 0; i < T; i ++) {
            long N = sc.nextLong();
            long n = (N - 1) / 2;
            BigInteger b = BigInteger.valueOf(n);
            n %= MOD;

            long p1 = b.multiply(b.add(BigInteger.ONE)).multiply(b.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE)).divide(BigInteger.valueOf(3)).mod(BigInteger.valueOf(MOD)).longValue();

            long a = (1 + ((8 * p1) % MOD) + (2 * ((n * (n + 1)) % MOD) % MOD) + 4 * n) % MOD;
            sb.append(a).append('\n');
        }
        System.out.print(sb.toString());
    }
}
