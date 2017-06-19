package hackerland;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class HackerlandTidy {

    static final int ADDITIONAL = 100000;
    static final int CACHE_SIZE = 10000;
    static final int MAX_NODES = 50000;

    // cache
    static final boolean CACHE_ENABLED = true;
    static BitSet[] limitedCache;
    static int[] cachePos;
    static int cache_i;

    // cycle recognition
    static int[] index;
    static int[] lowindex;
    static boolean[] onStack;
    static int scc_i;
    static int[] stack = new int[MAX_NODES];
    static int stack_i;
    static int[] map;

    // graph
    static int[] first;
    static int[] fin;
    static int[][] edges;

    // queries
    static ArrayList<Integer>[] questIndex;
    static int[][] quests;
    static Boolean[] answers;

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        solve(bi);
        StringBuilder sb = new StringBuilder();
        for (boolean answer : answers) {
            sb.append(answer ? "Yes" : "No").append('\n');
        }
        System.out.print(sb.toString());
    }

    static void cleanCache() {
        if (!CACHE_ENABLED) return;
        limitedCache = new BitSet[CACHE_SIZE];
        cachePos = new int[MAX_NODES];
        cache_i = 0;
        for (int i = 0; i < cachePos.length; i++) {
            cachePos[i] = -1;
        }
    }

    static void solve(BufferedReader in) throws IOException {
        cleanCache();

        String[] split;
        split = in.readLine().split(" ");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);

        fin = new int[MAX_NODES];
        first = new int[MAX_NODES];
        for (int i = 0; i < fin.length; i++) {
            fin[i] = -1;
            first[i] = -1;
        }
        edges = new int[m + ADDITIONAL][];
        int edgeCnt = 0;

        for (int a0 = 0; a0 < m; a0++) {
            split = in.readLine().split(" ");
            int u = Integer.parseInt(split[0]) - 1;
            int v = Integer.parseInt(split[1]) - 1;

            if (first[u] == -1) first[u] = edgeCnt;
            edges[edgeCnt] = new int[]{v, fin[u]};
            fin[u] = edgeCnt++;
        }

        map = new int[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            map[i] = i;
        }

        SCC(n);

        for (int i = 0; i < edgeCnt; i++) {
            edges[i][0] = map[edges[i][0]];
        }

        split = in.readLine().split(" ");
        int q = Integer.parseInt(split[0]);
        quests = new int[q][];
        int qCount = 0;

        questIndex = new ArrayList[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            questIndex[i] = new ArrayList<>();
        }

        for (int a0 = 0; a0 < q; a0++) {
            split = in.readLine().split(" ");
            int qType = Integer.parseInt(split[0]);

            if (qType == 1) {

                int node = map[Integer.parseInt(split[1]) - 1];
                int direction = Integer.parseInt(split[2]);
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
                int u = map[Integer.parseInt(split[1]) - 1];
                int v = map[Integer.parseInt(split[2]) - 1];

                quests[qCount] = new int[]{u, v};
                questIndex[u].add(qCount);
                qCount++;
            }
        }

        answers = new Boolean[qCount];

        for (int q_i = 0; q_i < qCount; q_i++) {
            if (answers[q_i] == null) {
                int initNode = quests[q_i][0];
                process(initNode, new BitSet());
            }
        }
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
            int w = edges[edge_i][0];
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

    static void process(int node, BitSet parentAnswer) {
        if (CACHE_ENABLED && cachePos[node] != -1) {
            parentAnswer.or(limitedCache[cachePos[node]]);
            return;
        }

        BitSet answer = new BitSet();
        answer.set(node);

        for (int edge_i = fin[node]; edge_i >= 0; edge_i = edges[edge_i][1]) {
            int v = edges[edge_i][0];
            if (node != v) {
                process(v, answer);
            }
        }

        fillAnswers(node, answer);

        int pos;
        if (CACHE_ENABLED && cache_i < limitedCache.length) {
            pos = cache_i++;
            cachePos[node] = pos;
            limitedCache[pos] = answer;
        }

        parentAnswer.or(answer);
    }

    static void fillAnswers(int node, BitSet answer) {
        for (int q_i : questIndex[node]) {
            answers[q_i] = answer.get(quests[q_i][1]);
        }
        questIndex[node].clear();
    }

}
