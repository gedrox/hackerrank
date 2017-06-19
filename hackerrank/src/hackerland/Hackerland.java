package hackerland;

import org.junit.Test;
import org.testng.Assert;

import java.util.*;

public class Hackerland {

    public static final int ADDITIONAL = 100000;
    private static final int MAX_NODES = 50000;
    static BitSet[] limitedCache;
    static int[] cachePos;
    static int[] cacheOwner;
    static Random r;
    static int cache_i;
    private static int[] fin;
    private static int[] first;
    private static int[][] edges;
    private static HashMap<Integer, ArrayList<Integer>> questIndex;
    private static int[][] quests;
    private static int[] answers;
    private static int answCount;
    private static ArrayList<Integer>[] loopNodes;

    static void cleanCache() {
        limitedCache = new BitSet[10000];
        cachePos = new int[MAX_NODES];
        cacheOwner = new int[10000];
        r = new Random(0);
        cache_i = 0;
        for (int i = 0; i < cachePos.length; i++) {
            cachePos[i] = -1;
        }
        for (int i = 0; i < cacheOwner.length; i++) {
            cacheOwner[i] = -1;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Boolean> result = solve(in);
        for (boolean a : result) {
            System.out.println(a ? "Yes" : "No");
        }
    }

    private static ArrayList<Boolean> solve(Scanner in) {

        cleanCache();
        
        int n = in.nextInt();
        int m = in.nextInt();

        fin = new int[MAX_NODES];
        first = new int[MAX_NODES];
        for (int i = 0; i < fin.length; i++) {
            fin[i] = -1;
            first[i] = -1;
        }
        edges = new int[m + ADDITIONAL][];
        int edgeCnt = 0;

        for (int a0 = 0; a0 < m; a0++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;

            if (first[u] == -1) first[u] = edgeCnt;
            edges[edgeCnt] = new int[]{v, fin[u]};
            fin[u] = edgeCnt++;
        }

        loopNodes = new ArrayList[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            loopNodes[i] = new ArrayList<>();
            loopNodes[i].add(i);
        }
        int[] map = new int[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            map[i] = -1;
        }
        int smallestUnprocessed = 0;
        forever:
        while (true) {
            while (map[smallestUnprocessed] != -1) {
                smallestUnprocessed++;
                if (smallestUnprocessed == n) {
                    break forever;
                }
            }

            int[] visited = new int[n];
            visit(smallestUnprocessed, visited, map, 1);
        }

        for (int i = 0; i < edgeCnt; i++) {
            int[] edge = edges[i];
            edge[0] = map[edge[0]];
        }

        int q = in.nextInt();
        quests = new int[q][];
        int qCount = 0;

        questIndex = new HashMap<>();

        for (int a0 = 0; a0 < q; a0++) {
            int qType = in.nextInt();

            if (qType == 1) {

                int node = map[in.nextInt() - 1];
                int direction = in.nextInt();
                int u, v;
                map[n] = n;
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
                int u = map[in.nextInt() - 1];
                int v = map[in.nextInt() - 1];

                quests[qCount] = new int[]{u, v};
                if (!questIndex.containsKey(u)) {
                    questIndex.put(u, new ArrayList<>());
                }
                questIndex.get(u).add(qCount);
                qCount++;
            }
        }

        answers = new int[qCount];
        answCount = 0;
        int firstNotAnswered = 0;

        while (answCount < qCount) {
            while (answers[firstNotAnswered] != 0) {
                firstNotAnswered++;
            }

            int initNode = quests[firstNotAnswered][0];
            boolean[] visited = new boolean[n];

            process(initNode, new BitSet(), visited);
        }

        ArrayList<Boolean> ret = new ArrayList<>();
        
        for (int answer : answers) {
            ret.add(answer == 1);
        }
        
        return ret;
    }

    private static int visit(int node, int[] visited, int[] map, int level) {
        if (map[node] == -1) map[node] = node;
        if (visited[node] != 0) {
            return node;
        }
        visited[node] = level;

        int minLevel = Integer.MAX_VALUE;
        int topLooper = -1;

        for (int edge_i = fin[node]; edge_i >= 0; edge_i = edges[edge_i][1]) {
            int v = edges[edge_i][0];
            int looper = visit(v, visited, map, level + 1);
            if (looper != -1 && looper != node) {
                if (visited[looper] < minLevel) {
                    minLevel = visited[looper];
                    topLooper = looper;
                }

                if (map[looper] != map[node]) {

                    int from, to;
                    if (loopNodes[map[node]].size() > loopNodes[map[looper]].size()) {
                        from = map[looper];
                        to = map[node];
                    } else {
                        from = map[node];
                        to = map[looper];
                    }

                    if (from == to) throw new RuntimeException();

                    loopNodes[to].addAll(loopNodes[from]);
                    for (Integer moveNode : loopNodes[from]) {
                        if (map[moveNode] == to) {
                            throw new RuntimeException();
                        }
                        map[moveNode] = to;
                    }

                    edges[first[to]][1] = fin[from];
                    fin[from] = -1;
                    first[to] = first[from];
                    first[from] = -1;

                    loopNodes[from].clear();
                }
            }
        }

        visited[node] = 0;
        return topLooper;
    }

    static BitSet process(int node, BitSet check, boolean[] visited) {

        if (cachePos[node] != -1) return limitedCache[cachePos[node]];

        if (visited[node]) {
            return new BitSet();
        }
        visited[node] = true;

        BitSet localCheck = new BitSet();
        if (questIndex.containsKey(node)) {
            for (int q_i : questIndex.get(node)) {
                localCheck.set(quests[q_i][1]);
            }
        }

        BitSet allCheck = (BitSet) check.clone();
        allCheck.or(localCheck);

        BitSet answer = new BitSet();
        answer.set(node);

        if (allCheck.get(node)) {
            check.set(node, false);
            allCheck.set(node, false);
        }

        for (int edge_i = fin[node]; edge_i >= 0; edge_i = edges[edge_i][1]) {
            int v = edges[edge_i][0];
//            System.err.printf("next from %d to %d%n", node + 1, v + 1);
            answer.or(process(v, allCheck, visited));
        }

        if (questIndex.containsKey(node)) {
            for (Iterator<Integer> iterator = questIndex.get(node).iterator(); iterator.hasNext(); ) {
                int q_i = iterator.next();
                if (answer.get(quests[q_i][1])) {
                    answers[q_i] = 1;
                } else {
                    answers[q_i] = -1;
                }
                answCount++;
                iterator.remove();
            }
        }

        int pos;
        if (cache_i < limitedCache.length) {
            pos = cache_i++;
        } else {
            pos = r.nextInt(15000);
        }
        if (pos < limitedCache.length) {
            if (cacheOwner[pos] != -1) {
                cachePos[cacheOwner[pos]] = -1;
            }
            cacheOwner[pos] = node;
            cachePos[node] = pos;
            limitedCache[pos] = answer;
        }

        visited[node] = false;
        return answer;
    }

    @Test
    public void test() {
        //1495897795690
        for (int J = 0; J < 1; J++) {
            long seed = System.currentTimeMillis();
            seed = 1495898192264L;
            System.out.println("seed: " + seed);
            Random r = new Random(seed);

//        int n = r.nextInt(MAX_NODES);
//        int m = r.nextInt(MAX_NODES);
//        int q1 = MAX_NODES - n;
//        int q2 = 100000 - q1;

            int n = r.nextInt(20) + 1;
            int m = r.nextInt(40) + 1;
            int q1 = r.nextInt(20);
            int q2 = 100000 - q1;
            q2 = 19;

//        n = 10;
//        m = 20;
//        q1 = 0;
//        q2 = 10;

            int q = q1 + q2;

            StringBuilder sb = new StringBuilder();
            sb.append(n).append(" ").append(m).append('\n');
            for (int i = 0; i < m; i++) {
                sb.append(r.nextInt(n) + 1).append(" ").append(r.nextInt(n) + 1).append('\n');
            }

            sb.append(q).append('\n');
            for (int i = 0; i < q1; i++) {
                sb.append("1 ").append(r.nextInt(n) + 1).append(" ").append(r.nextInt(2)).append('\n');
                n++;
            }

            for (int i = 0; i < q2; i++) {
                sb.append("2 ").append(r.nextInt(n) + 1).append(" ").append(r.nextInt(n) + 1).append('\n');
            }

//        for (int i = 0; i < q; i++) {
//            if (r.nextInt(2) == 0) {
//                sb.append("1 ").append(r.nextInt(n) + 1).append(" ").append(r.nextInt(2)).append('\n');
//                n++;
//            } else {
//                sb.append("2 ").append(r.nextInt(n) + 1).append(" ").append(r.nextInt(n) + 1).append('\n');
//            }
//        }

        System.out.println(sb.toString());

            long t0 = System.currentTimeMillis();
            ArrayList<Boolean> result = solve(new Scanner(sb.toString()));
            System.err.println(System.currentTimeMillis() - t0 + "ms");
        for (boolean a : result) {
            System.out.println(a ? "Yes" : "No");
        }

            t0 = System.currentTimeMillis();
            ArrayList<Boolean> expected = Simple.solve(new Scanner(sb.toString()));
            System.err.println(System.currentTimeMillis() - t0 + "ms");

            Assert.assertEquals(result, expected);
        }
    }
}
