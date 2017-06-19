package kingdomdivision;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class KingdomDivision {

    static int MOD = 1000000007;

    static ArrayList<Integer>[] next;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        next = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            next[i] = new ArrayList<>();
        }
        for (int a0 = 0; a0 < n - 1; a0++) {
            int u = in.nextInt();
            int v = in.nextInt();

            next[u - 1].add(v - 1);
            next[v - 1].add(u - 1);
        }

        int[] res0 = new int[n];
        int[] res1 = new int[n];

        LinkedList<Integer> depthFirst = new LinkedList<>();

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(0);

        boolean[] visited = new boolean[n];

        while (!queue.isEmpty()) {
            Integer val = queue.pop();
            visited[val] = true;
            depthFirst.addFirst(val);
            for (Integer nextNode : next[val]) {
                if (!visited[nextNode]) {
                    queue.add(nextNode);
                }
            }
        }

//        System.out.println(depthFirst);

        for (Integer node : depthFirst) {
            long mul1 = 1;
            long mul2 = 1;
            for (Integer nextNode : next[node]) {
                if (!visited[nextNode]) {
                    mul1 *= res1[nextNode];
                    mul2 *= (2L * res1[nextNode] + res0[nextNode]) % MOD;
                    mul1 %= MOD;
                    mul2 %= MOD;
                }
            }

            res0[node] = (int) mul1; // all branches are OK, set node to (1) and all start with (2)
            res1[node] = (int) (mul2 - mul1);
            if (res1[node] < 0) res1[node] += MOD;

            visited[node] = false;
        }

//        System.out.println(Arrays.toString(res0));
//        System.out.println(Arrays.toString(res1));

        System.out.println((2 * ((long) res1[0] + MOD)) % MOD);
    }


}
