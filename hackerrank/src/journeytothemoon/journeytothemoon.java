package journeytothemoon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class journeytothemoon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m= sc.nextInt();
        
        int[][] edges = new int[2 * m][];
        int e_i = 0;
        int[] fin = new int[n];
        for (int i = 0; i < n; i++) {
            fin[i] = -1;
        }

        for (int i = 0; i < m; i++) {
            int from = sc.nextInt();
            int to = sc.nextInt();

            edges[e_i] = new int[] {to, fin[from]};
            fin[from] = e_i++;

            edges[e_i] = new int[] {from, fin[to]};
            fin[to] = e_i++;
        }
        
        ArrayList<Integer> sizes = new ArrayList<>();
        int cId = 1;
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            if (c[i] == 0) {
                int cnt = 1;
                LinkedList<Integer> q = new LinkedList<>();
                q.add(i);
                c[i] = cId;
                while (!q.isEmpty()) {
                    int next = q.poll();
                    for (int edge_i = fin[next]; edge_i >= 0; edge_i = edges[edge_i][1]) {
                        int to = edges[edge_i][0];
                        if (c[to] == 0) {
                            c[to] = cId;
                            cnt++;
                            q.add(to);
                        }
                    }
                }
                sizes.add(cnt);
            }
        }
        
        long res = ((long) n) * n;
        for (Integer size : sizes) {
            res -= size * size;
        }
        res /= 2;
        System.out.println(res);
    }
}
