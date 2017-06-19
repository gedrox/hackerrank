package bonnieAndClyde;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class BonnieAndClydeFlow {
    private static Node[] inNodes;
    private static Node[] outNodes;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int q = in.nextInt();

        inNodes = new Node[n];
        outNodes = new Node[n];
        for (int i = 0; i < n; i++) {
            inNodes[i] = new Node("IN " + (i + 1));
            outNodes[i] = new Node("OUT " + (i + 1));
            inNodes[i].addEdge(outNodes[i]);
        }

        for (int a0 = 0; a0 < m; a0++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;

            outNodes[u].addEdge(inNodes[v]);
            outNodes[v].addEdge(inNodes[u]);
        }

        for (int a0 = 0; a0 < q; a0++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int w = in.nextInt() - 1;

            boolean yes = willItFlow(u, v, w);

            System.out.println(yes ? "YES" : "NO");
        }
    }

    private static boolean willItFlow(int u, int v, int w) {
        HashSet<Edge> toReset = new HashSet<>();

        try {
            return flow(u, w, toReset) && flow(v, w, toReset);
        } finally {
            for (Edge edge : toReset) {
                edge.capacity = edge.originalCapacity;
                edge.inverse.capacity = edge.inverse.originalCapacity;
            }
        }
    }

    private static boolean flow(int u, int w, HashSet<Edge> toReset) {

        Edge[] path = new Edge[Node.COUNTER];
        
        if (!findPath(u, w, path)) return false;

        Node node = inNodes[w];
        while (path[node.i] != null) {
            Edge edge = path[node.i];
            edge.capacity--;
            edge.inverse.capacity++;
            toReset.add(edge);
            node = edge.from;
        }

        return true;
    }

    private static boolean findPath(int u, int w, Edge[] path) {
        boolean[] visited = new boolean[Node.COUNTER];

        LinkedList<Node> qu = new LinkedList<>();
        qu.add(inNodes[u]);
        visited[inNodes[u].i] = true;
        
        while (!qu.isEmpty()) {
            Node poll = qu.poll();
            for (Edge edge : poll.next) {
                if (!visited[edge.to.i] && edge.capacity > 0) {
                    qu.add(edge.to);
                    path[edge.to.i] = edge;
                    visited[edge.to.i] = true;
                    if (edge.to == inNodes[w]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static class Node {
        static ArrayList<Node> ALL = new ArrayList<>();
        static int COUNTER = 0;

        final int i = COUNTER++;
        final String name;
        ArrayList<Edge> next = new ArrayList<>();

        Node(String name) {
            this.name = name;
            ALL.add(this);
        }

        public void addEdge(Node node) {
            Edge edge = new Edge(this, node, 1);
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
        int capacity;
        final int originalCapacity;
        Edge inverse;

        public Edge(Node from, Node to, int capacity) {
            this.from = from;
            this.to = to;
            this.originalCapacity = capacity;
            this.capacity = capacity;
            this.inverse = new Edge(to, from, 0, this);
        }

        public Edge(Node from, Node to, int capacity, Edge inverse) {
            this.from = from;
            this.to = to;
            this.originalCapacity = capacity;
            this.capacity = capacity;
            this.inverse = inverse;
        }

        @Override
        public String toString() {
            return "to " + to + " (cap: " + capacity + ")";
        }
    }
}
