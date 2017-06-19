package bonnieAndClyde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BonnieAndClyde {

    private static int[] level;
    private static Node[] inNodes;
    private static ArrayList<ArrayList<ArrayList<Integer>>> byLevel;
    private static int[] left;
    private static int[] right;
    private static int[] tree_id;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] split = br.readLine().split(" ");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int q = Integer.parseInt(split[2]);

        int edgeCnt = 0;
        int[][] edges = new int[2 * m][];
        int[] fin = new int[n];
        for (int i = 0; i < n; i++) {
            fin[i] = -1;
        }

        inNodes = new Node[n];
        Node[] outNodes = new Node[n];
        for (int i = 0; i < n; i++) {
            inNodes[i] = new Node("IN " + (i + 1));
            inNodes[i].nodeId = i;
            outNodes[i] = new Node("OUT " + (i + 1));
            outNodes[i].nodeId = i;
            inNodes[i].addEdge(outNodes[i]);
        }

        for (int a0 = 0; a0 < m; a0++) {
            split = br.readLine().split(" ");
            int u = Integer.parseInt(split[0]) - 1;
            int v = Integer.parseInt(split[1]) - 1;

            outNodes[u].addEdge(inNodes[v]);
            outNodes[v].addEdge(inNodes[u]);

            edges[edgeCnt] = new int[]{v, fin[u]};
            fin[u] = edgeCnt++;

            edges[edgeCnt] = new int[]{u, fin[v]};
            fin[v] = edgeCnt++;
        }

        boolean[] visited = new boolean[n];
        int tree_count = 0;
        tree_id = new int[n];
        level = new int[n];
        for (int i = 0; i < n; i++) {
            tree_id[i] = -1;
            level[i] = -1;
        }

        byLevel = new ArrayList<>();
        left = new int[n];
        right = new int[n];
        int lri = 0;

        for (int i = 0; i < n; i++) {
            if (tree_id[i] != -1) continue;

            byLevel.add(new ArrayList<>());

            LinkedList<int[]> queue = new LinkedList<>();
            visited[i] = true;
            queue.add(new int[]{i, 0, 1});
            queue.add(new int[]{i, 0, 2});

            while (!queue.isEmpty()) {
                int[] next = queue.pollFirst();
                int node_id = next[0];

                if (next[2] == 1) {
                    left[node_id] = lri++;
                    int lvl = next[1];

                    tree_id[node_id] = tree_count;
                    level[node_id] = lvl;
                    if (byLevel.get(tree_count).size() == lvl) {
                        byLevel.get(tree_count).add(new ArrayList<>());
                    }
                    byLevel.get(tree_count).get(lvl).add(node_id);

                    for (int edge_i = fin[node_id]; edge_i >= 0; edge_i = edges[edge_i][1]) {
                        int w = edges[edge_i][0];
                        if (visited[w]) continue;
                        visited[w] = true;
                        queue.addFirst(new int[]{w, lvl + 1, 2});
                        queue.addFirst(new int[]{w, lvl + 1, 1});
                    }
                } else {
                    right[node_id] = lri++;
                }
            }

            for (ArrayList<Integer> treeByLvl : byLevel.get(tree_count)) {
                treeByLvl.sort(Comparator.comparing(node_id -> left[node_id]));
            }

            tree_count++;
        }
        
        StringBuilder sb = new StringBuilder();

        for (int a0 = 0; a0 < q; a0++) {
            split = br.readLine().split(" ");
            int u = Integer.parseInt(split[0]) - 1;
            int v = Integer.parseInt(split[1]) - 1;
            int w = Integer.parseInt(split[2]) - 1;

            final boolean yes;
            if (tree_id[u] != tree_id[w] || tree_id[v] != tree_id[w]) {
                yes = false;
            } else {
                
                int commonUW = ancestor(u, w);
                int commonVW = ancestor(v, w);

                yes = (commonUW == w ^ commonVW == w) || solve2(u, v, w);
            }

            sb.append(yes ? "YES\n" : "NO\n");
        }
        System.out.print(sb.toString());
    }

    private static boolean solve2(int realU, int realV, int realW) {
        if (realU == realW) return true;
        if (realV == realW) return true;
        if (realU == realV) return false;

        HashSet<Edge> toReset = new HashSet<>();

        boolean check = flow(realU, realW, toReset);
        if (!check) return false;
        boolean OK = flow(realV, realW, toReset);

        for (Edge edge : toReset) {
            edge.capacity = edge.originalCapacity;
            edge.inverse.capacity = edge.inverse.originalCapacity;
        }

        return OK;
    }

    private static boolean flow(int realU, int realW, HashSet<Edge> toReset) {

        Edge[] path = new Edge[Node.COUNTER];
        boolean[] visited = new boolean[Node.COUNTER];

        LinkedList<Node> qu = new LinkedList<>();
        qu.add(inNodes[realU]);
        visited[inNodes[realU].i] = true;

        if (!findPath(realW, path, visited, qu)) return false;

        Node node = inNodes[realW];
        while (path[node.i] != null) {
            Edge edge = path[node.i];
            edge.capacity = false;
            edge.inverse.capacity = true;
            toReset.add(edge);
            node = edge.from;
        }

        return true;
    }

    private static boolean findPath(int realW, Edge[] path, boolean[] visited, LinkedList<Node> qu) {
        while (!qu.isEmpty()) {
            Node poll = qu.poll();
            for (Edge edge : poll.next) {
                if (!visited[edge.to.i] && edge.capacity) {
                    qu.add(edge.to);
                    path[edge.to.i] = edge;
                    visited[edge.to.i] = true;
                    if (edge.to == inNodes[realW]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int ancestor(int a, int b) {
        if (isAnc(a, b)) {
            return a;
        }
        if (isAnc(b, a)) {
            return b;
        }

        int lvl0 = 0;
        int lvl1 = Math.min(level[a], level[b]);

        int prnt = byLevel.get(tree_id[a]).get(0).get(0);

        while (lvl1 - lvl0 > 1) {
            int lvl = (lvl1 + lvl0) / 2;
            int prntCand = ancestorByLvl(a, lvl);
            if (isAnc(prntCand, a) && isAnc(prntCand, b)) {
                prnt = prntCand;
                lvl0 = lvl;
            } else {
                lvl1 = lvl;
            }
        }

        return prnt;
    }

    private static int ancestorByLvl(int a, int lvl) {
        ArrayList<Integer> stack = byLevel.get(tree_id[a]).get(lvl);
        int ind = Collections.binarySearch(stack, a, Comparator.comparing(i -> left[i]));
        return stack.get(-ind - 2);
    }

    private static boolean isAnc(int a, int b) {
        return left[a] <= left[b] && right[a] >= right[b];
    }

    static class Node {
        static ArrayList<Node> ALL = new ArrayList<>();
        static int COUNTER = 0;

        final int i = COUNTER++;
        final String name;
        public int nodeId;
        ArrayList<Edge> next = new ArrayList<>();

        Node(String name) {
            this.name = name;
            ALL.add(this);
        }

        public void addEdge(Node node) {
            Edge edge = new Edge(this, node);
            next.add(edge);
            node.next.add(edge.inverse);
        }

        @Override
        public String toString() {
            return "Node: " + name;
        }
    }

    static class Edge {
        final boolean originalCapacity;
        Node from;
        Node to;
        boolean capacity;
        Edge inverse;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
            this.originalCapacity = true;
            this.capacity = true;
            this.inverse = new Edge(to, from, this);
        }

        public Edge(Node from, Node to, Edge inverse) {
            this.from = from;
            this.to = to;
            this.originalCapacity = false;
            this.capacity = false;
            this.inverse = inverse;
        }

        @Override
        public String toString() {
            return "to " + to + " (cap: " + capacity + ")";
        }
    }
}
