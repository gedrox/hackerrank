package beautifulPath;

import java.util.*;

/**
 * https://www.hackerrank.com/challenges/beautiful-path
 */
public class BeautifulPath {
    public static final int MAX = 1 << 30 - 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        int n = sc.nextInt();
        int m = sc.nextInt();
        
        int[] fin = new int[n];
        for (int i = 0; i < n; i++) {
            fin[i] = -1;
        }
        int[][] path = new int[2 * m][];

        for (int i = 0; i < 2*m; i += 2) {
            int from = sc.nextInt() - 1;
            int to = sc.nextInt() - 1;
            int cost = sc.nextInt();
            path[i] = new int[] {to, cost, fin[from]};
            path[i + 1] = new int[] {from, cost, fin[to]};
            fin[from] = i;
            fin[to] = i + 1;
        }
        
        long t0 = System.currentTimeMillis();
        
        HashSet<Integer>[] min = new HashSet[n];
        for (int i = 0; i < n; i++) {
            min[i] = new HashSet<>();
        }
        
        int A = sc.nextInt() - 1;
        int B = sc.nextInt() - 1;
        
        min[A].add(0);
        ArrayList<Integer> q = new ArrayList<>(1000000);
        q.add(A);
        q.add(0);
        int X = 0;
        while (X < q.size()) {
            int node = q.get(X++);
            int val = q.get(X++);
            if (!min[node].contains(val)) continue;
            nextEdge:
            for (int edge_i = fin[node]; edge_i >= 0; edge_i = path[edge_i][2]) {
                
                int newMin = val | path[edge_i][1];
                HashSet<Integer> currMins = min[path[edge_i][0]];
                if (currMins.contains(newMin)) continue;
                for (Iterator<Integer> iterator = currMins.iterator(); iterator.hasNext(); ) {
                    int currMin = iterator.next();
                    if ((currMin | newMin) == newMin) {
                        continue nextEdge;
                    }
                    if ((currMin | newMin) == currMin) {
                        iterator.remove();
                    }
                }

                currMins.add(newMin);
                q.add(path[edge_i][0]);
                q.add(newMin);
            }
        }
        
        int smallest = MAX;
        for (Integer integer : min[B]) {
            smallest = Math.min(smallest, integer);
        }

        System.out.println(smallest == MAX ? -1 : smallest);
        System.err.println(System.currentTimeMillis() - t0 + "ms");
    }
}
