package PathStatistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PathStatistics {
    static int[] level;
    private static int n;
    private static int[] parent;
    private static int[][] ch;
    private static ArrayList<Integer>[] next;

    public static void main(String[] args) throws IOException {

        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));

        String[] split = bi.readLine().split(" ");
        setN(Integer.parseInt(split[0]));
        int Q = Integer.parseInt(split[1]);

        int[] c = new int[n];

        
        HashMap<Integer, Integer> c2Index = new HashMap<>();
        ArrayList<Integer> index2C = new ArrayList<>();
        int cCnt = 0;
        split = bi.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            c[i] = Integer.parseInt(split[i]);
            if (!c2Index.containsKey(c[i])) {
                c2Index.put(c[i], cCnt++);
                index2C.add(c[i]);
            }
        }

        for (int i = 0; i < n - 1; i++) {
            split = bi.readLine().split(" ");
            int u = Integer.parseInt(split[0]) - 1;
            int v = Integer.parseInt(split[1]) - 1;

            addEdge(u, v);
        }

        parent = new int[n];
        parent[0] = -1;
        ch = new int[n][];

        level = new int[n];

        int[] queue = new int[n];
        int S = 0, E = 1;

        // C sum from root to node
        long[] R = new long[n];
        R[0] = c[0];

        while (S < E) {
            int r = queue[S++];
            ch[r] = new int[next[r].size() - (parent[r] == -1 ? 0 : 1)];
            int i = 0;

            for (int ch_i : next[r]) {
                if (ch_i != parent[r]) {
                    parent[ch_i] = r;
                    level[ch_i] = level[r] + 1;
                    ch[r][i++] = ch_i;
                    queue[E++] = ch_i;
                    R[ch_i] = R[r] + c[ch_i];
                }
            }
        }
        
        Freq[][] fromRoot = new Freq[n][];

        for (int node : queue) {
            fromRoot[node] = new Freq[cCnt];
            for (int i = 0; i < cCnt; i++) {
                fromRoot[node][i] = new Freq(i);    
            }
            if (node != 0) {
                Freq[] parData = fromRoot[parent[node]];
                for (int i = 0; i < parData.length; i++) {
                    if (parData[i] != null) {
                        fromRoot[node][i].cnt += parData[i].cnt;
                    }
                }
            }
            fromRoot[node][c2Index.get(c[node])].cnt++;
        }

        StringBuilder sb = new StringBuilder();
        for (int q_i = 0; q_i < Q; q_i++) {
            HashMap<Integer, Freq> byC = new HashMap<Integer, Freq>();

            split = bi.readLine().split(" ");
            int u = Integer.parseInt(split[0]) - 1;
            int v = Integer.parseInt(split[1]) - 1;
            int k = Integer.parseInt(split[2]) - 1;

            if (level[u] < level[v]) {
                int tmp = u;
                u = v;
                v = tmp;
            }

            while (level[u] > level[v]) {
                add(byC, c[u]);
                u = parent[u];
            }

            while (u != v) {
                add(byC, c[u]);
                u = parent[u];
                add(byC, c[v]);
                v = parent[v];
            }

            add(byC, c[v]);

            ArrayList<Freq> forSort = new ArrayList<Freq>(byC.values());
            forSort.sort(Comparator.<Freq, Integer>comparing(fr -> -fr.cnt).thenComparing(fr -> -fr.x));

            sb.append(forSort.get(k).x).append('\n');
        }

        System.out.print(sb);
    }

    private static void add(HashMap<Integer, Freq> byC, int i) {
        if (byC.containsKey(i)) {
            byC.get(i).cnt++;
        } else {
            byC.put(i, new Freq(i));
        }
    }

    static void setN(int n) {
        PathStatistics.n = n;
        next = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            next[i] = new ArrayList<>();
        }
    }

    static void addEdge(int u, int v) {
        next[u].add(v);
        next[v].add(u);
    }

    static class Freq {
        int x, cnt = 0;

        public Freq(int x) {
            this.x = x;
        }
    }
}
