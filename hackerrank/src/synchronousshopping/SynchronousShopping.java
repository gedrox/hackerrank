package synchronousshopping;

import java.util.LinkedList;
import java.util.Scanner;

public class SynchronousShopping {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int k = sc.nextInt();
        int target = (1 << k) - 1;

        int[] f = new int[n];

        for (int i = 0; i < n; i++) {
            int fCnt = sc.nextInt();
            for (int i1 = 0; i1 < fCnt; i1++) {
                f[i] |= 1 << (sc.nextInt() - 1);
            }
        }

        int[][] edges = new int[2 * m][];
        int e_i = 0;
        int[] fin = new int[n];
        for (int i = 0; i < n; i++) {
            fin[i] = -1;
        }
        
        for (int i = 0; i < m; i++) {
            int from = sc.nextInt() - 1;
            int to = sc.nextInt() - 1;
            int time = sc.nextInt();
            
            edges[e_i] = new int[] {to, fin[from], time};
            fin[from] = e_i++;
            
            edges[e_i] = new int[] {from, fin[to], time};
            fin[to] = e_i++;
        }

        int[][] min = new int[n][target + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= target; j++) {
                min[i][j] = Integer.MAX_VALUE;
            }
        }
        
        LinkedList<int[]> q = new LinkedList<>();
        min[0][f[0]] = 0;
        q.add(new int[] {0, f[0]});
        
        while (!q.isEmpty()) {
            int[] next = q.poll();
            int from = next[0];
            int fish = next[1];
            
            for (int edge_i = fin[from]; edge_i >= 0; edge_i = edges[edge_i][1]) {
                int to = edges[edge_i][0];
                int time = edges[edge_i][2];
                int fishTo = fish | f[to];
                if (min[to][fishTo] > min[from][fish] + time) {
                    min[to][fishTo] = min[from][fish] + time;
                    q.add(new int[]{to, fishTo});
                }
            }
        }
        
        int minTotalTime = Integer.MAX_VALUE;
        for (int i = 0; i <= target; i++) {
            for (int j = 0; j <= target; j++) {
                int time = Math.max(min[n - 1][i], min[n - 1][j]);
                if ((i | j) == target && minTotalTime > time) {
                    minTotalTime = time;
                }
            }
        }
        System.out.println(minTotalTime);
    }
}
