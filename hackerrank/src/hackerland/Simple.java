package hackerland;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Simple {
    private static final int MAX_NODES = 50000;
    private static final int ADDITIONAL = 100000;
    static int[] fin;
    static int[][] edges;
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Boolean> answers = solve(in);

        for (boolean answer : answers) {
            System.out.println(answer ? "Yes" : "No");
        }
    }

    public static ArrayList<Boolean> solve(Scanner in) {
        int n = in.nextInt();
        int m = in.nextInt();

        fin = new int[MAX_NODES];
        for (int i = 0; i < fin.length; i++) {
            fin[i] = -1;
        }
        edges = new int[m + ADDITIONAL][];
        int edgeCnt = 0;

        for (int a0 = 0; a0 < m; a0++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;

            edges[edgeCnt] = new int[]{v, fin[u]};
            fin[u] = edgeCnt++;
        }

        ArrayList<Boolean> answers = new ArrayList<>();

        int q = in.nextInt();
        for (int a0 = 0; a0 < q; a0++) {
            int qType = in.nextInt();

            if (qType == 1) {

                int node = in.nextInt() - 1;
                int direction = in.nextInt();
                int u, v;
                if (direction == 0) {
                    u = node;
                    v = n++;
                } else {
                    u = n++;
                    v = node;
                }

                edges[edgeCnt] = new int[]{v, fin[u]};
                fin[u] = edgeCnt++;
            }
            if (qType == 2) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;

                LinkedList<Integer> Q = new LinkedList<>();
                boolean[] visited = new boolean[n];
                Q.add(u);
                boolean found = u == v;
                
                while (!Q.isEmpty() && !found) {
                    int node = Q.pollFirst();
                    for (int edge_i = fin[node]; edge_i >= 0; edge_i = edges[edge_i][1]) {
                        int nextNode = edges[edge_i][0];
                        if (!visited[nextNode]) {
                            visited[nextNode] = true;
                            if (nextNode == v) {
                                found = true;
                                break;
                            }
                            Q.add(nextNode);
                        }
                    }
                }

                answers.add(found);
            }
        }
        return answers;
    }
}
