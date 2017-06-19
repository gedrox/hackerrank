package bonnieAndClyde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class BC3 {

    static final int MAX_NODES = 100000;

    // cycle recognition
    static int[] index;
    static int[] lowindex;
    static boolean[] onStack;
    static int scc_i;
    static int[] stack = new int[MAX_NODES];
    static int stack_i;
    static int[] map;

    // graph
    private static int[] fin;
    private static int[] first;
    private static int[][] edges;
    private static boolean[] visited;
    private static int[] parent;
    private static int[] realParent;
    private static int[] level;
    private static Node[] inNodes;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] split = br.readLine().split(" ");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int q = Integer.parseInt(split[2]);

        int edgeCnt = 0;
        edges = new int[2 * m][];
        fin = new int[n];
        first = new int[n];
        for (int i = 0; i < n; i++) {
            fin[i] = -1;
            first[i] = -1;
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

            edges[edgeCnt] = new int[]{v, fin[u], a0, u};
            if (first[u] == -1) first[u] = edgeCnt;
            fin[u] = edgeCnt++;

            edges[edgeCnt] = new int[]{u, fin[v], a0, v};
            if (first[v] == -1) first[v] = edgeCnt;
            fin[v] = edgeCnt++;
        }

        map = new int[n];
        for (int i = 0; i < n; i++) {
            map[i] = i;
        }

        visited = new boolean[m];
        SCC(n);

        for (int i = 0; i < edgeCnt; i++) {
            edges[i][0] = map[edges[i][0]];
        }

        visited = new boolean[m];
        int tree_count = 0;
        int[] tree_id = new int[n];
        parent = new int[n];
        realParent = new int[n];
        level = new int[n];
        for (int i = 0; i < n; i++) {
            tree_id[i] = -1;
            parent[i] = -1;
            realParent[i] = -1;
            level[i] = -1;
        }

        for (int i = 0; i < n; i++) {
            if (map[i] != i) continue;
            if (tree_id[i] != -1) continue;

            LinkedList<int[]> queue = new LinkedList<>();
            //visited[i] = true;
            queue.add(new int[] {i, 0});

            while (!queue.isEmpty()) {
                int[] next = queue.pollFirst();
                int node_id = next[0];
                int lvl = next[1];

                tree_id[node_id] = tree_count;
                level[node_id] = lvl;

                for (int edge_i = fin[node_id]; edge_i >= 0; edge_i = edges[edge_i][1]) {
                    if (visited[edges[edge_i][2]]) continue;
                    visited[edges[edge_i][2]] = true;

                    int w = edges[edge_i][0];
                    if (w == node_id) continue;

                    queue.add(new int[] {w, lvl + 1});
                    parent[w] = node_id;
                    realParent[w] = edges[edge_i][3];
                }
            }

            tree_count++;
        }

        StringBuilder sb = new StringBuilder();

        for (int a0 = 0; a0 < q; a0++) {
            split = br.readLine().split(" ");
            int realU = Integer.parseInt(split[0]) - 1;
            int realV = Integer.parseInt(split[1]) - 1;
            int realW = Integer.parseInt(split[2]) - 1;

            int u = map[realU];
            int v = map[realV];
            int w = map[realW];

            final boolean yes;
            if (tree_id[u] != tree_id[v] || tree_id[u] != tree_id[w]) {
                yes = false;
            } else {

                int[] commonUW = ancestor(u, w);
                int[] commonVW = ancestor(v, w);
                if (commonUW[1] != -1) realU = commonUW[1];
                if (commonVW[1] != -1) realV = commonVW[1];

                if (commonUW[0] != w && commonVW[0] != w) {
                    yes = false;
                } else if (commonUW[0] != w || commonVW[0] != w) {

                    // sample case with "4" as root
                    if (commonUW[0] != w) {
                        yes = solve2(realU, realV, realW);
                    } else {
                        yes = solve2(realU, realV, realW);
                    }

                } else {

                    int[] commonUV = ancestor(u, v);
                    if (commonUV[0] == w) {
                        // think "8"
                        yes = solve2(realU, realV, realW);
                    } else {
                        yes = false;
                    }
                }
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
        if (!check) throw new RuntimeException();
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
            for (Iterator<Edge> iterator = poll.next.iterator(); iterator.hasNext(); ) {
                Edge edge = iterator.next();
                if (map[edge.to.nodeId] != map[realW]) {
                    iterator.remove();
                    continue;
                }

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

    private static int[] ancestor(int u, int v) {
        int realParentU = -1;
        int levelDiff = level[u] - level[v];
        for (int i = 0; i < levelDiff; i++) {
            realParentU = realParent[u];
            u = parent[u];
        }
        for (int i = 0; i < -levelDiff; i++) {
            v = parent[v];
        }

        while (u != v) {
            realParentU = realParent[u];

            u = parent[u];
            v = parent[v];
        }

        return new int[] {u, realParentU};
    }

    static void SCC(int n) {
        index = new int[n];
        lowindex = new int[n];
        onStack = new boolean[n];
        scc_i = 1;

        for (int i = 0; i < n; i++) {
            stack_i = 0;
            if (index[i] == 0) {
                strongconnect(i);
            }
        }
    }

    static void strongconnect(int v) {
        index[v] = scc_i;
        lowindex[v] = scc_i;
        scc_i++;
        stack[stack_i++] = v;
        onStack[v] = true;

        for (int edge_i = fin[v]; edge_i >= 0; edge_i = edges[edge_i][1]) {
            if (visited[edges[edge_i][2]]) continue;
            int w = edges[edge_i][0];
            visited[edges[edge_i][2]] = true;

            if (index[w] == 0) {
                strongconnect(w);
                lowindex[v] = Math.min(lowindex[v], lowindex[w]);
            } else if (onStack[w]) {
                lowindex[v] = Math.min(lowindex[v], index[w]);
            }
        }

        if (lowindex[v] == index[v]) {
            int w, master = -1;
            do {
                w = stack[--stack_i];
                onStack[w] = false;
                if (master == -1) {
                    master = w;
                } else {
                    map[w] = master;

                    if (fin[w] != -1) {
                        edges[first[master]][1] = fin[w];
                        fin[w] = -1;
                        first[master] = first[w];
                        first[w] = -1;
                    }

                }
            } while (w != v);
        }
    }

    static class Node {
        static ArrayList<Node> ALL = new ArrayList<>();
        static int COUNTER = 0;

        final int i = COUNTER++;
        final String name;
        ArrayList<Edge> next = new ArrayList<>();
        public int nodeId;

        Node(String name) {
            this.name = name;
            ALL.add(this);
        }

        public void addEdge(Node node) {
            Edge edge = new Edge(this, node);
            next.add(edge);
            node.next.add(edge.inverse);
        }


    }

    static class Edge {
        Node from;
        Node to;
        boolean capacity;
        final boolean originalCapacity;
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

    }
}

