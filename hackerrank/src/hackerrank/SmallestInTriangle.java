package hackerrank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class SmallestInTriangle {

    public static void main(String[] args) throws FileNotFoundException {
//        Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new FileInputStream(new File("tr14.in")));
        int n = in.nextInt();
        int m = in.nextInt();
        int x = in.nextInt();
        long k = in.nextLong();
        int[] a = new int[n];
        for(int a_i = 0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }
        int[] b = new int[m];
        for(int b_i = 0; b_i < m; b_i++){
            b[b_i] = in.nextInt();
        }
        in.close();
        
        int rowLimit = Math.min(n, m - x);
        Random r = new Random(0);
        
        long L;
        if (n < m - x) {
            L = (long) (2 * m - 2 * x - n + 1) * n / 2;
        } else {
            L = (long) (m - x + 1) * (m - x) / 2;
        }
        
        int tries = 1000000;
        long[] res = new long[tries];
        int try_i = 0;
        
        while (try_i < tries) {
            int a_i = r.nextInt(rowLimit);
            int b_i = r.nextInt(m - x) + x;
            if (a_i <= b_i - x) {
                res[try_i++] = (long) a[a_i] * b[b_i];
            }
        }
        
        Arrays.sort(res);

        int mid = (int) ((k - 1) * tries / L);
        int span = 10000;
        
        long low = mid < span ? Long.MIN_VALUE : res[mid - span];
        long high = mid > tries - span - 1 ? Long.MAX_VALUE : res[mid + span];

        ArrayList<Long> midVals = new ArrayList<>(); 

        long lc = 0, hc = 0;
        for (int i = 0; i < rowLimit; i++) {
            for (int j = i + x; j < m; j++) {
                long val = (long) a[i] * b[j];
                if (val < low) lc++;
                else if (val > high) hc++;
                else midVals.add(val);
            }
        }
        
        Collections.sort(midVals);
        
        long target = k - 1 - lc;
        if (target < 0) {
            System.err.println("Might be wrong");
            System.out.println(midVals.get(0));
        } else if (target >= midVals.size()) {
            System.err.println("Might be wrong2");
            System.out.println(midVals.get(midVals.size() - 1));
        } else {
            System.out.println(midVals.get((int) target));
        }
        
    }
}
