package roadtrip1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class RoadTrip1 {

    private static BufferedReader bi;
    private static String[] split;
    private static int split_i = 0;

    public static void main(String[] args) throws IOException {
        bi = new BufferedReader(new InputStreamReader(System.in));

        readLine();
        int n = nextInt();
        int q = nextInt();
        
        int w[] = new int[n - 1];
        readLine();
        for (int i = 0; i < w.length; i++) {
            w[i] = nextInt();
        }

        int[] g = new int[n];
        int[] p = new int[n];
        
        for (int i = 0; i < n; i++) {
            readLine();
            g[i] = nextInt();
            p[i] = nextInt();
        }

        int[][] Q = new int[q][];
        for (int i = 0; i < q; i++) {
            readLine();
            int x = nextInt() - 1;
            int y = nextInt() - 1;
            Q[i] = new int[]{i, x, y, (int) Math.floor(x / Math.sqrt(n))};
        }

        Comparator<int[]> comp1 = Comparator.comparing(qEl -> qEl[3]);
        Comparator<int[]> comp2 = Comparator.comparing(qEl -> qEl[2]);
        Arrays.sort(Q, comp1.thenComparing(comp2));
        
        int x = 0;
        int y = 0;
        int rem = 0;
        int minPrice = Integer.MAX_VALUE;
        int cost = 0;
        ArrayList<Integer> prices = new ArrayList<>();
        boolean sorted = true;
        int[] costs = new int[n - 1];

        for (int[] qEl : Q) {
            while (y < qEl[2]) {
                prices.add(p[y]);
                sorted = false;
                minPrice = Math.min(minPrice, p[y]);
                rem += g[y];
                if (rem >= w[y]) {
                    rem -= w[y];
                    costs[y] = 0;
                } else {
                    costs[y] = minPrice * (w[y] - rem);
                    cost += costs[y];
                    rem = 0;
                }
                y++;
            }
            while (y > qEl[2]) {
                y--;
                if (!sorted) Collections.sort(prices);
                int pos = Collections.binarySearch(prices, p[y]);
                prices.remove(pos);
                minPrice = prices.isEmpty() ? Integer.MAX_VALUE : prices.get(0);
                cost -= costs[y];
                
//                rem
            }

            System.out.println(qEl[0] + ": " + cost);
        }
    }

    private static int nextInt() {
        return Integer.parseInt(split[split_i++]);
    }

    private static void readLine() throws IOException {
        split = bi.readLine().split(" ");
        split_i = 0;
    }
}
