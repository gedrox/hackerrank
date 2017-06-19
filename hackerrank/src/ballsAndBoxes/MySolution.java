package ballsAndBoxes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class MySolution {

    static Node S = new Node("START");
    static Node T = new Node("FINISH");


    public static void main(String[] args) {
//        int n = 2;
//        int m = 2;
//        int[] A = {1, 1};
//        int[] C = {0, 0};
//        int[][] B = {{9, 8}, {7, 0}};

//        int n = 10;
//        int m = 2;
//        int[] A = {5, 2, 3, 14, 20, 11, 3, 2, 17, 7};
//        int[] C = {5, 2};
//        int[][] B = {
//                {129, 402},
//                {415, 283},
//                {898, 42},
//                {638, 728},
//                {719, 33},
//                {630, 708},
//                {16, 721},
//                {124, 359},
//                {11, 231},
//                {218, 510}
//        };

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[] A = new int[n];
        for (int A_i = 0; A_i < n; A_i++) {
            A[A_i] = in.nextInt();
        }
        int[] C = new int[m];
        for (int C_i = 0; C_i < m; C_i++) {
            C[C_i] = in.nextInt();
        }
        int[][] B = new int[n][m];
        for (int B_i = 0; B_i < n; B_i++) {
            for (int B_j = 0; B_j < m; B_j++) {
                B[B_i][B_j] = in.nextInt();
            }
        }

        long t0 = System.currentTimeMillis();

        Node[] colorNodes = new Node[n];
        Node[] boxNodes = new Node[m];
        for (int i = 0; i < colorNodes.length; i++) {
            Node colorNode = colorNodes[i] = new Node("color #" + i);
            S.addEdge(colorNode, A[i], 0);
        }

        for (int i = 0; i < boxNodes.length; i++) {
            boxNodes[i] = new Node("box #" + i);
            if (C[i] > 0) {
                boxNodes[i].addEdge(T, C[i], 0);
            }
            for (int c = 0; c < n - C[i]; c++) {
                boxNodes[i].addEdge(T, 1, -2 * c - 1);
            }
        }

        for (int col = 0; col < colorNodes.length; col++) {
            for (int box = 0; box < boxNodes.length; box++) {
                colorNodes[col].addEdge(boxNodes[box], 1, B[col][box]);
            }
        }

        long result = 0;
        
        long c = 0, c2 = 0;
        long s1 = 0, s2 = 0;

        Node[] queue = new Node[405 * 405];

        while (true) {
            c++;
            long t = System.currentTimeMillis();
            for (Node node : Node.ALL) {
                node.maxCandies = Long.MIN_VALUE;
            }
            S.maxCandies = 0;

            queue[0] = S;
            int X = 0, Y = 1;

            while (X < Y) {
                Node node = queue[X++];
                for (Edge edge : node.next) {
                    if (edge.capacity > 0 && node.maxCandies + edge.candies > edge.to.maxCandies) {
                        edge.to.maxCandies = node.maxCandies + edge.candies;
                        queue[Y++] = edge.to;
                    }
                }
            }
            s1 += System.currentTimeMillis() - t;
            t = System.currentTimeMillis();

            if (T.maxCandies <= 0) {
                break;
            }

            while (true) 
            {
                c2++;
                boolean[] visited = new boolean[Node.COUNTER];
                visited[S.i] = true;
                ArrayList<Edge> edges = new ArrayList<>();
                long maxCapacity = findMaxPath(S, edges, visited);
                if (maxCapacity <= 0) {
                    break;
                }
                for (Edge edge : edges) {
                    edge.capacity -= maxCapacity;
                    edge.inverse.capacity += maxCapacity;
                }
                result += maxCapacity * T.maxCandies;
            }
            s2 += (System.currentTimeMillis() - t);
        }

        System.out.println(result);
        System.err.println(c);
        System.err.println(c2);
        System.err.println(s1);
        System.err.println(s2);
        System.err.println(System.currentTimeMillis() - t0 + "ms");
    }

    static long findMaxPath(Node node, ArrayList<Edge> path, boolean[] visited) {
        if (node == T) {
            return Long.MAX_VALUE;
        }
        for (Edge edge : node.next) {
            if (!visited[edge.to.i] && edge.capacity > 0 && node.maxCandies + edge.candies == edge.to.maxCandies) {
                path.add(edge);
                visited[edge.to.i] = true;
                long maxPath = findMaxPath(edge.to, path, visited);
                if (maxPath <= 0) {
                    path.remove(path.size() - 1);
                    visited[edge.to.i] = false;
                } else {
                    return Math.min(maxPath, edge.capacity);
                }
            }
        }
        return 0;
    }

    static class Node {

        static ArrayList<Node> ALL = new ArrayList<>();
        static int COUNTER = 0;

        final int i = COUNTER++;
        final String name;
        ArrayList<Edge> next = new ArrayList<>();
        long maxCandies = Long.MIN_VALUE;

        Node(String name) {
            this.name = name;
            ALL.add(this);
        }

        public void addEdge(Node node, int capacity, int candies) {
            Edge edge = new Edge(this, node, capacity, candies);
            next.add(edge);
            node.next.add(edge.inverse);
        }

        @Override
        public String toString() {
            return "Node: " + name;
        }
    }

    static class Edge {
        Node from;
        Node to;
        long capacity;
        long candies;
        Edge inverse;

        public Edge(Node from, Node to, long capacity, long candies) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.candies = candies;
            this.inverse = new Edge(to, from, 0, -candies, this);
        }

        public Edge(Node from, Node to, long capacity, long candies, Edge inverse) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.candies = candies;
            this.inverse = inverse;
        }

        @Override
        public String toString() {
            return "to " + to + " (cap: " + capacity + ", cand: " + candies + ")";
        }
    }
}
