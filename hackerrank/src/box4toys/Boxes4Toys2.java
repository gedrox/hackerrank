package box4toys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class Boxes4Toys2 {

    static int MOD = 1000000007;
    private static int[][][] max;
    private static int[][] x;
    private static int n;
    private static int[][] q;
    private static int q0;
    private static int q1;
    private static long offset;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        BufferedReader br = new BufferedReader(new FileReader("src/box4toys/case5.in"));
        n = Integer.parseInt(br.readLine());

        x = new int[n][];

        for (int i = 0; i < n; i++) {
            String[] split = br.readLine().split(" ");
            x[i] = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        }

        init();

        long sum = 0;

        HashMap<Integer, Long>[] cache = new HashMap[n];
        for (int i = 0; i < n; i++) {
            cache[i]= new HashMap<>();
        }

        q = new int[100000][];
        q0 = 0;
        q1 = 0;
        q[q1++] = new int[]{0, n, 1};
        offset = 0;

        while (q0 < q1) {
            int[] next = q[q0++];

            if (next[0] >= next[1]) continue;

            if (cache[next[0]].containsKey(next[1]) && cache[next[0]].get(next[1]) >= 0) {
                sum += next[2] * cache[next[0]].get(next[1]);
            } else {
                int[] m = findMax(next);
                int[] mval = {x[m[0]][0], x[m[1]][1], x[m[2]][2]};
                Arrays.sort(m);

                long toCache = mult(m[0] + 1 - next[0], next[1] - m[2], mval[0], mval[1], mval[2]);
                cache[next[0]].put(next[1], toCache);

                sum += ((long) (next[2] % MOD)) * toCache;

                Function<int[], Integer> lambda = val -> {
                    if (val[0] >= val[1]) return 0;
                    Long cacheVal = cache[val[0]].get(val[1]);
                    if (cacheVal != null) {
                        if (cacheVal >= 0) {
                            return (int) ((val[2] * cacheVal) % MOD);
                        } else {
                            q[(int) (-cacheVal - 1 - offset)][2] = (q[(int) (-cacheVal - 1 - offset)][2] + val[2]) % MOD;
                            return 0;
                        }
                    }
                    q[q1++] = val;
                    cache[val[0]].put(val[1], -q1 - offset);
                    return 0;
                };

                sum += lambda.apply(new int[]{next[0], m[2], 1});
                sum += lambda.apply(new int[]{m[0] + 1, next[1], 1});
                sum += lambda.apply(new int[]{m[0] + 1, m[2], -1});
                sum %= MOD;
                
                if (q1 > q.length - 100) {
                    offset += q0;
                    int[][] q_copy = new int[q.length][];
                    System.arraycopy(q, q0, q_copy, 0, q1 - q0);
                    q = q_copy;
                    q1 -= q0;
                    q0 = 0;
                }
            }
        }

        if (sum < 0) sum += MOD;

        int answer = BigInteger.valueOf(sum).multiply(BigInteger.valueOf(((long) n) * (n + 1) / 2).modInverse(BigInteger.valueOf(MOD))).mod(BigInteger.valueOf(MOD)).intValue();

        System.out.println(answer);
    }

    static void init() {
        for (int i = 0; i < n; i++) {
            Arrays.sort(x[i]);
        }

        // lvl, index, dimension
        max = new int[20][][];


        max[0] = new int[x.length][];
        for (int i = 0; i < x.length; i++) {
            max[0][i] = new int[]{i, i, i};
        }

        int lvl = 1;
        int[][] lvlVals = max[0];
        while (lvlVals.length != 1) {
            int newLen = (lvlVals.length - 1) / 2 + 1;
            max[lvl] = new int[newLen][3];

            for (int i = 0; i < lvlVals.length; i++) {
                for (int dim = 0; dim < lvlVals[i].length; dim++) {
                    if (i % 2 == 0 || x[lvlVals[i][dim]][dim] > x[lvlVals[i - 1][dim]][dim]) {
                        max[lvl][i >> 1][dim] = lvlVals[i][dim];
                    }
                }
            }

            lvlVals = max[lvl];
            lvl++;
        }
    }

    static int mult(int... vals) {
        long result = 1;
        for (int val : vals) {
            result = (result * (val % MOD)) % MOD;
        }
        return (int) result;
    }

    // 3 indices for maxes
    private static int[] findMax(int[] next) {
        int start = next[0];
        int end = next[1];

        int bit = Integer.numberOfTrailingZeros(start);
        int[] maxIdx = new int[]{start, start, start};
        while (end >= (start + (1L << bit))) {
            for (int dim = 0; dim < 3; dim++) {
                int maxIndex = max[bit][start >> bit][dim];
                if (x[maxIdx[dim]][dim] < x[maxIndex][dim]) {
                    maxIdx[dim] = maxIndex;
                }
            }
            start += 1 << bit;
            bit = Integer.numberOfTrailingZeros(start);
        }

        while (end > start) {
            if (end >= start + (1L << bit)) {
                for (int dim = 0; dim < 3; dim++) {
                    int maxIndex = max[bit][start >> bit][dim];
                    if (x[maxIdx[dim]][dim] < x[maxIndex][dim]) {
                        maxIdx[dim] = maxIndex;
                    }
                }
                start += 1 << bit;
            }
            bit--;
        }

        return maxIdx;
    }
}