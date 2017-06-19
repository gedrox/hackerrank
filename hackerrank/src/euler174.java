import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class euler174 {

    public static final int[] PRIMES = getPrimes(500);

    private static int[] getPrimes(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean p[] = new boolean[(max - 1) / 2];
        for (int i = 0; i < p.length; i++) {
            if (!p[i]) {
                primes.add(2 * i + 3);
                int next = 3 * i + 3;
                while (next < p.length) {
                    p[next] = true;
                    next += 2 * i + 3;
                }
            }
        }

        int[] pr = new int[primes.size()];
        for (int i = 0; i < primes.size(); i++) {
            pr[i] = primes.get(i);
        }

        return pr;
    }
    
    public static void main(String[] args) {
        ArrayList<Integer> answers = new ArrayList<>();
        for (int i = 2; i <= 1000000/4; i++) {
            
            int mult = 1;
            int j = i;
            for (int prime : PRIMES) {
                if (prime * prime > j) break;
                if (j % prime == 0) {
                    int times = 0;
                    while (j % prime == 0) {
                        j /= prime;
                        times++;
                    }
                    mult *= times + 1;
                    if (mult > 10) {
                        break;
                    }
                }
            }
            
            if (j > 1) {
                mult *= 2;
            }
            
            if (mult/2 <= 10) {
                answers.add(4 * i);
            }
        }

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int k = sc.nextInt();
            int answ = Collections.binarySearch(answers, k);
            if (answ >= 0) {
                sb.append(answ+1).append('\n');
            } else {
                sb.append(-answ-1).append('\n');
            }
        }
        System.out.print(sb.toString());
    }
}
