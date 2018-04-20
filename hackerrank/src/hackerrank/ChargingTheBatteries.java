package hackerrank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;

public class ChargingTheBatteries {

    private static int[] x;
    private static int[] y;
    private static int[] wall;
    private static int n;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();

        x = new int[m];
        y = new int[m];
        Integer[] i = new Integer[m];
        wall = new int[m];
        
        for(int a0 = 0; a0 < m; a0++){
            x[a0] = in.nextInt();
            y[a0] = in.nextInt();
            i[a0] = a0;
            
            if (x[a0] == 0) wall[a0] = 0;
            else if (y[a0] == n) wall[a0] = 1;
            else if (x[a0] == n) wall[a0] = 2;
            else wall[a0] = 3;
        }

        Arrays.sort(i, Comparator.comparing(ChargingTheBatteries::index).thenComparing(Function.identity()));

        long min = Long.MAX_VALUE;
        for (int i1 = 0; i1 < m; i1++) {
            long d = findDist(i[i1], i[(i1 + k - 1) % m]);
            if (d < min) {
                min = d; 
            }
        }
        System.out.println(min);
    }
    
    private static long findDist(int i, int j) {
        int d1 = index(i);
        int d2 = index(j);
        
        if (d1 == d2) return i <= j ? 0 : 4 * n;
        return d2 > d1 ? d2 - d1 : 4 * n + (d2 - d1);
    }
    
    static int index(int i) {
        switch (wall[i]) {
            case 0: return y[i];
            case 1: return n + x[i];
            case 2: return 3*n - y[i];
            case 3: return 4*n - x[i];
        }
        throw new IllegalArgumentException();
    }
}
