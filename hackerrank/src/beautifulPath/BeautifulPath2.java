package beautifulPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * https://www.hackerrank.com/challenges/beautiful-path
 */
public class BeautifulPath2 {

    public static final int MAX = 2047;

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
        
        int finalMin = 0;
        int mask = MAX;

        int A = sc.nextInt() - 1;
        int B = sc.nextInt() - 1;
        
        while (true) {
            int[] min = new int[n];
            for (int i = 0; i < n; i++) {
                min[i] = mask;
            }

            min[A] = 0;
            ArrayList<Integer> q = new ArrayList<>(1000000);
            q.add(A);
            int X = 0;
            while (X < q.size()) {
                int node = q.get(X++);
                int val = min[node];
                for (int edge_i = fin[node]; edge_i >= 0; edge_i = path[edge_i][2]) {
                    int newMin = (val | path[edge_i][1]) & mask;
                    if (newMin < min[path[edge_i][0]]) {
                        min[path[edge_i][0]] = newMin;
                        q.add(path[edge_i][0]);
                    }
                }
            }
            
            if (min[B] == MAX) {
                finalMin = -1;
                break;
            } else if (min[B] == 0) {
                break;
            } else {

                int highestBit = 0;
                while (min[B] != 1) {
                    min[B] /= 2;
                    highestBit++;
                }
                
                finalMin |= 1 << highestBit;
                mask -= (1 << highestBit);
            }
        }
        
        System.out.println(finalMin);
        System.err.println(System.currentTimeMillis() - t0 + "ms");
    }
}
