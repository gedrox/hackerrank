package hackerrank;

import java.util.ArrayList;
import java.util.Scanner;

public class WireRemoval {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        
        ArrayList<Integer>[] ch = new ArrayList[n];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = new ArrayList<>();
        }
        
        for(int a0 = 0; a0 < n-1; a0++){
            int x = in.nextInt() - 1;
            int y = in.nextInt() - 1;
            ch[x].add(y);
            ch[y].add(x);
        }
        in.close();
        
        int[] realParent = new int[n];
        int[] depth = new int[n];
        int[] q = new int[n];
        int S = 0, E = 1;
        // node #1 is root
        q[0] = 0;
        while (S < E) {
            int next = q[S++];
            for (int i : ch[next]) {
                if (i != realParent[next]) {
                    realParent[i] = next;
                    q[E++] = i;
                    depth[i] = depth[next] + 1;
                }
            }
        }
        
        int[] cnt = new int[n];
        long div1 = 0;
        long div2 = 0;

        for (int i = q.length - 1; i >= 0; i--) {
            int node = q[i];
            cnt[node]++;
            cnt[realParent[node]] += cnt[node];
            
            div1 += ((long) cnt[node]) * depth[node];
            div2 += depth[node];
        }

        System.out.println(n - 1d * div1 / div2);
    }
}
