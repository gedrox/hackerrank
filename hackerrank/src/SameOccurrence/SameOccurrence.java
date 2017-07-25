package SameOccurrence;

import org.junit.Test;

import java.io.*;
import java.util.*;

public class SameOccurrence {

    public static final int[] EMPTY_INT_ARRAY = new int[0];
    static int MAX = 8010;
    private static HashMap<String, Integer> cache = new HashMap<>();
    private static HashMap<Integer, int[]> pos = new HashMap<>();
    private static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] split = br.readLine().split(" ");
        n = Integer.parseInt(split[0]);
        int q = Integer.parseInt(split[1]);

        int[] a = new int[n];
        split = br.readLine().split(" ");
        for(int i = 0; i < n; i++){
            a[i] = Integer.parseInt(split[i]);
        }

        prepare(a);

        StringBuilder sb = new StringBuilder();
        for(int a0 = 0; a0 < q; a0++){
            split = br.readLine().split(" ");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);

            long finalSum = solve(x, y);
            sb.append(finalSum).append("\n");
        }
        System.out.println(sb.toString());
    }

    private static void prepare(int[] a) {
        n = a.length;
        HashMap<Integer, List<Integer>> pos0 = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            if (!pos0.containsKey(a[i])) pos0.put(a[i], new ArrayList<>());
            pos0.get(a[i]).add(i);
        }

        for (int x : pos0.keySet()) {
            List<Integer> posVals = pos0.get(x);
            int[] arr = new int[posVals.size()];
            for (int i = 0; i < posVals.size(); i++) {
                arr[i] = posVals.get(i);
            }
            pos.put(x, arr);
        }
    }

    private static int solve(int x, int y) {
        if (!pos.containsKey(x)) x = -1;
        if (!pos.containsKey(y)) y = -1;
        
        if (x == y) {
            return (n + 1) * n / 2;
        }
        if (x > y) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        
        if (cache.containsKey(x + "-" + y)) {
            return cache.get(x + "-" + y);
        }

        int[] xp = pos.getOrDefault(x, EMPTY_INT_ARRAY);
        int[] yp = pos.getOrDefault(y, EMPTY_INT_ARRAY);
        
        if (xp.length == 0 && yp.length == 0) {
            return (n + 1) * n / 2;
        }
        
        int x_i = 0;
        int y_i = 0;

        int currPos = 0;
        HashMap<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);

        int currDiff = 0;
        int finalSum = 0;

        while (x_i < xp.length || y_i < yp.length) {
            
            int prevPos = currPos;
            int prevDiff = currDiff;
            
            if (x_i == xp.length || (y_i < yp.length && yp[y_i] < xp[x_i])) {
                // use next y
                currDiff--;
                currPos = yp[y_i++];
            } else {
                // use next x
                currDiff++;
                currPos = xp[x_i++];
            }
            
            int sumStart = count.getOrDefault(prevDiff, 0);
            int sumCount = currPos - prevPos;
            finalSum += (2 * sumStart + sumCount - 1) * sumCount / 2;

            count.put(prevDiff, count.getOrDefault(prevDiff, 0) + sumCount);
        }

        // final steps
        int sumStart = count.getOrDefault(currDiff, 0);
        int sumCount = n - currPos;
        finalSum += (2 * sumStart + sumCount - 1) * sumCount / 2;

        cache.put(x + "-" + y, finalSum);
        
        return finalSum;
    }
    
    @Test
    public void test() {
        prepare(new int[] {1, 2, 1});
        System.out.println(solve(1, 2));
        System.out.println(solve(4, 5));
    }
    
    @Test
    public void testBig() {
        n = 8000;
        Random r = new Random(0);
        int[] a = new int[n];
        int bound = 10000;
        for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(bound);
        }
        prepare(a);
        for (int i = 0; i < 500000; i++) {
            solve(r.nextInt(bound), r.nextInt(bound));
        }
    }
}
