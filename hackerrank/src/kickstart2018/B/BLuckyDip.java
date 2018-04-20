package kickstart2018.B;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class BLuckyDip {
    public static void main(String[] args) throws IOException {
        String fn = new Scanner(System.in).next();
        Scanner f = new Scanner(new File(fn));
        String out = fn + ".out";
        StringBuilder sb = new StringBuilder();
        int q = f.nextInt();
        for (int q_i = 0; q_i < q; q_i++) {
            int n = f.nextInt();
            int k = f.nextInt();

            int v[] = new int[n];
            for (int i = 0; i < n; i++) {
                v[i] = f.nextInt();
            }

            double answer = solveNew(n, k, v);

            if (k <= 1 && answer != solveOld(n, k, v)) {
                throw new RuntimeException(q_i + 1 + "");
            }

            sb.append("Case #").append(q_i + 1).append(": ").append(answer).append('\n');
        }

        System.out.println(sb.toString());
        Files.write(Paths.get(out), sb.toString().getBytes());
    }

    private static double getMatCer(int[] v) {
        long sum = 0;
        for (int i : v) {
            sum += i;
        }
        return 1d * sum / v.length;
    }

    private static double solveNew(int n, int k, int[] v) {
        Arrays.sort(v);
        var sum = new long[n];
        for (int i = n - 1; i >= 0; i--) {
            sum[i] = (i < n - 1 ? sum[i + 1] : 0) + v[i];
        }
        double answer = getMatCer(v);
        for (int k_i = 0; k_i < k; k_i++) {
            var pos = Arrays.binarySearch(v, (int) Math.floor(answer));
            if (pos < 0) pos = - pos - 2;
            answer = (answer * (pos + 1) + (pos + 1 < sum.length ? sum[pos + 1] : 0)) / n;
        }
        
        return answer;
    }
    
    @Test
    public void test() {
        System.out.println(solveNew(5, 3, new int[]{16, 11, 7, 4, 1}));
    }

    private static double solveOld(int n, int k, int[] v) {
        double matCer = getMatCer(v);
        double answer;
        if (k == 0) {
            answer = matCer;
        } else {
            answer = 0;
            for (int i = 0; i < n; i++) {
                if (v[i] < matCer) {
                    answer += matCer;
                } else {
                    answer += v[i];
                }
            }
            answer /= n;
        }
        return answer;
    }
}
