package eventree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class EvenTree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        int[][] edge = new int[2 * m][];
        int[] fin = new int[n];
        for (int i = 0; i < n; i++) {
            fin[i] = -1;
        }

        for (int i = 0; i < m; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;

            edge[2 * i] = new int[]{y, fin[x]};
            fin[x] = 2 * i;

            edge[2 * i + 1] = new int[]{x, fin[y]};
            fin[y] = 2 * i + 1;
        }
        
        int[] c = new int[n];
        
        LinkedList<int[]> q = new LinkedList<>();
        q.add(new int[] {0, 1, -1});
        q.add(new int[] {0, 2, -1});
        boolean[] visited = new boolean[n];
        visited[0] = true;
        
        while (!q.isEmpty()) {
            int[] next = q.poll();
            int id = next[0];
            int p = next[2];
            if (next[1] == 1) {
                
                c[id] = 1;
                
                for (int eid = fin[id]; eid >= 0; eid = edge[eid][1]) {
                    int ch = edge[eid][0];
                    if (!visited[ch]) {
                        visited[ch] = true;
                        q.addFirst(new int[] {ch, 2, id});
                        q.addFirst(new int[] {ch, 1, id});
                    }
                }
                
            } else {
                if (p != -1) {
                    c[p] += c[id];
                }
            }
        }

        int answer = -1;
        for (int i : c) {
            if (i % 2 == 0) answer++;
        }

        System.out.println(answer);
    }
}

