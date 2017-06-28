package kruskalmstreallyspecialsubtree;

import java.util.*;

public class KruskalMSTReallySpecialSubtree {

    private static int[] color;

    static int getCol(int i) {
        if (color[i] == color[color[i]]) return color[i];
        return (color[i] = getCol(color[i]));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        Edge[] edges = new Edge[m];

        for (int i = 0; i < m; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            int r = sc.nextInt();

            edges[i] = new Edge(x, y, r);
        }

        Arrays.sort(edges, Comparator.<Edge, Integer>comparing(e -> e.r).thenComparing(Comparator.comparing(e -> e.x + e.y)));
        int e_i = 0;

        color = new int[n];
        for (int i = 0; i < n; i++) {
            color[i] = i;
        }

        long sum = 0;

        for (int i = 0; i < n - 1; i++) {
            while (getCol(edges[e_i].x) == getCol(edges[e_i].y)) {
                e_i++;
            }
            color[getCol(edges[e_i].x)] = getCol(edges[e_i].y);
            sum += edges[e_i].r;
            e_i++;
        }

        for (int i = 1; i < n; i++) {
            if (getCol(i) != getCol(i- 1)) {
                throw new RuntimeException();
            }
        }

        System.out.println(sum);
    }

    static class Edge {
        int x, y, r;

        public Edge(int x, int y, int r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
    }
}
