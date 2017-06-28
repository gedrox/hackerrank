package box4toys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;

public class Boxes4Toys {

    static int MOD = 1000000007;
    private static int[][][] max;
    private static int[][] x;
    private static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());

        x = new int[n][];

        for (int i = 0; i < n; i++) {
            String[] split = br.readLine().split(" ");
            x[i] = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};
        }

        init();

        long sum = 0;

        HashMap<Integer, Integer>[] cache = new HashMap[n];
        for (int i = 0; i < n; i++) {
            cache[i] = new HashMap();
        }

        LinkedList<int[]> q = new LinkedList<>();
        q.add(new int[]{0, n, 1});
        while (!q.isEmpty()) {
            int[] next = q.poll();

            if (next[0] >= next[1]) continue;

            if (cache[next[0]].containsKey(next[1])) {
                sum += next[2] * cache[next[0]].get(next[1]);
            } else {
                int[] m = findMax(next);
                int[] mval = {x[m[0]][0], x[m[1]][1], x[m[2]][2]};
                Arrays.sort(m);

                int toCache = mult(m[0] + 1 - next[0], next[1] - m[2], mval[0], mval[1], mval[2]);
                cache[next[0]].put(next[1], toCache);

                sum += next[2] * toCache;
                sum %= MOD;

                Function<int[], Integer> lambda = val -> {
                    if (val[0] >= val[1]) return 0;
                    Integer cacheVal = cache[val[0]].get(val[1]);
//                    if (cacheVal != null) {
//                        return cacheVal;
//                    }
                    q.add(val);
                    return 0;
                };

                sum += lambda.apply(new int[]{next[0], m[2], 1});
                sum += lambda.apply(new int[]{m[0] + 1, next[1], 1});
                sum += lambda.apply(new int[]{m[0] + 1, m[2], -1});
                sum %= MOD;
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