package hackerland;

import org.junit.Test;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

public class HackerlandTrimLoop {

    public static final int ADDITIONAL = 100000;
    private static final int MAX_NODES = 50000;
    private static int[] first;
    private static int[] fin;
    private static int[][] edges;
    private static HashMap<Integer, ArrayList<Integer>> questIndex;
    private static int[][] quests;
    private static int[] answers;

    private static final boolean CACHE_ENABLED = false;
    public static final int CACHE_SIZE = 50000;
    static BitSet[] limitedCache;
    static int[] cachePos;
    static Random r;
    static int cache_i;
    static void cleanCache() {
        if (!CACHE_ENABLED) return;
        limitedCache = new BitSet[CACHE_SIZE];
        cachePos = new int[MAX_NODES];
        r = new Random(0);
        cache_i = 0;
        for (int i = 0; i < cachePos.length; i++) {
            cachePos[i] = -1;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Boolean> result = solve(bi);
        StringBuilder sb = new StringBuilder();
        for (boolean a : result) {
            sb.append(a ? "Yes" : "No").append('\n');
        }
        System.out.print(sb.toString());
    }

    private static ArrayList<Boolean> solve(BufferedReader in) throws IOException {

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
        
        int[] map = new int[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            map[i] = i;
        }
        
        SCC(map, n);

        for (int i = 0; i < edgeCnt; i++) {
            edges[i][0] = map[edges[i][0]];
        }

        split = in.readLine().split(" ");
        int q = Integer.parseInt(split[0]);
        quests = new int[q][];
        int qCount = 0;

        questIndex = new HashMap<>();
        for (int i = 0; i < MAX_NODES; i++) {
            questIndex.put(i, new ArrayList<>());
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
                questIndex.get(u).add(qCount);
                qCount++;
            }
        }

        answers = new int[qCount];
        int firstNotAnswered = 0;

        forever:
        while (true) {
            int initNode = quests[firstNotAnswered][0];
            int[] visited = new int[n];

            process(initNode, visited, new BitSet(), 1);

            if (answers[firstNotAnswered] == 0) throw new RuntimeException();
            
            while (answers[firstNotAnswered] != 0) {
                firstNotAnswered++;
                if (firstNotAnswered == qCount) {
                    break forever;
                }
            }
            System.err.println("Solve " + firstNotAnswered);
        }

        ArrayList<Boolean> ret = new ArrayList<>();
        
        for (int answer : answers) {
            ret.add(answer == 1);
        }
        
        return ret;
    }

    static int[] index;
    static int[] lowindex;
    static boolean[] onStack;
    static int I;
    static int[] stack = new int[MAX_NODES];
    static int stack_i;
    
    private static void SCC(int[] map, int n) {
        index = new int[n];
        lowindex = new int[n];
        onStack = new boolean[n];
        I = 1;
        
        for (int i = 0; i < n; i++) {
            stack_i = 0;
            if (index[i] == 0) {
                strongconnect(i, map);
            }
        }
    }

    private static void strongconnect(int v, int[] map) {
        index[v] = I;
        lowindex[v] = I;
        I++;
        stack[stack_i++] = v;
        onStack[v] = true;

        for (int edge_i = fin[v]; edge_i >= 0; edge_i = edges[edge_i][1]) {
            int w = edges[edge_i][0];
            if (index[w] == 0) {
                strongconnect(w, map);
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


    static HashMap<Integer, ArrayList<Integer>> buffer = new HashMap<>();

    static int process(int node, int[] visited, BitSet parentAnswer, int level) {

        if (CACHE_ENABLED && cachePos[node] != -1) {
            fillAnswers(node, limitedCache[cachePos[node]]);
            parentAnswer.or(limitedCache[cachePos[node]]);
            return -1;
        }

        if (visited[node] != 0) {
            return node;
        }
        visited[node] = level;

        BitSet answer = new BitSet();
        answer.set(node);

        int minLooperLevel = Integer.MAX_VALUE;
        int topLooper = -1;

        for (int edge_i = fin[node]; edge_i >= 0; edge_i = edges[edge_i][1]) {
            int v = edges[edge_i][0];
            int looper = process(v, visited, answer, level + 1);
            if (looper != -1) {
                if (visited[looper] < minLooperLevel) {
                    minLooperLevel = visited[looper];
                    topLooper = looper;
                }
            }
        }
        
        if (topLooper != -1 && topLooper != node) {
            if (!buffer.containsKey(topLooper)) {
                buffer.put(topLooper, new ArrayList<>());
            }
            buffer.get(topLooper).add(node);
            if (buffer.containsKey(node)) {
                buffer.get(topLooper).addAll(buffer.get(node));
                buffer.remove(node);
            }
            
            questIndex.get(topLooper).addAll(questIndex.get(node));
            questIndex.get(node).clear();
        } else {
            topLooper = -1;
        }

        fillAnswers(node, answer);

        int pos;
        if (CACHE_ENABLED && topLooper == -1 && cache_i < limitedCache.length) {
            pos = cache_i++;
//        } else {
//            pos = r.nextInt(15000);
//        }
//        if (pos < limitedCache.length) {
//            if (cacheOwner[pos] != -1) {
//                cachePos[cacheOwner[pos]] = -1;
//            }
//            cacheOwner[pos] = node;
            cachePos[node] = pos;
            limitedCache[pos] = answer;
            
            if (buffer.containsKey(node)) {
                for (Integer bufferedNode : buffer.get(node)) {
                    cachePos[bufferedNode] = pos;
                }
            }
            buffer.remove(node);
        }

        visited[node] = 0;
        parentAnswer.or(answer);
        
        return topLooper;
    }

    private static void fillAnswers(int node, BitSet answer) {
        if (questIndex.containsKey(node)) {
            for (Iterator<Integer> iterator = questIndex.get(node).iterator(); iterator.hasNext(); ) {
                int q_i = iterator.next();
                if (answer.get(quests[q_i][1])) {
                    answers[q_i] = 1;
                } else {
                    answers[q_i] = -1;
                }
                iterator.remove();
            }
        }
    }

    @Test
    public void test() throws IOException {
        //1495897795690
        for (int J = 0; J < 1000; J++) {
            long seed = System.currentTimeMillis();
//            seed = 1495898192264L;
            System.out.println("seed: " + seed);
            Random r = new Random(seed);

//        int n = r.nextInt(MAX_NODES);
//        int m = r.nextInt(MAX_NODES);
//        int q1 = MAX_NODES - n;
//        int q2 = 100000 - q1;

            int n = r.nextInt(20) + 1;
            int m = r.nextInt(40) + 1;
            int q1 = r.nextInt(10000);
            int q2 = 100000 - q1;
//            q2 = 1;

//        n = 10;
//        m = 20;
//        q1 = 0;
//        q2 = 10;

            StringBuilder sb = createTestCase(r, n, m, q1, q2);

//        for (int i = 0; i < q; i++) {
//            if (r.nextInt(2) == 0) {
//                sb.append("1 ").append(r.nextInt(n) + 1).append(" ").append(r.nextInt(2)).append('\n');
//                n++;
//            } else {
//                sb.append("2 ").append(r.nextInt(n) + 1).append(" ").append(r.nextInt(n) + 1).append('\n');
//            }
//        }

//        System.out.println(sb.toString());

            long t0 = System.currentTimeMillis();
            ArrayList<Boolean> result = solve(new BufferedReader(new StringReader(sb.toString())));
//            System.err.println(System.currentTimeMillis() - t0 + "ms");
//        for (boolean a : result) {
//            System.out.println(a ? "Yes" : "No");
//        }

            t0 = System.currentTimeMillis();
            ArrayList<Boolean> expected = Simple.solve(new Scanner(sb.toString()));
//            System.err.println(System.currentTimeMillis() - t0 + "ms");

            Assert.assertEquals(result, expected);
        }
    }

    private StringBuilder createTestCase(Random r, int n, int m, int q1, int q2) {
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
        return sb;
    }

    @Test
    public void testPerf() throws IOException {
        StringBuilder testCase = createTestCase(new Random(0), 100, 300, 40000, 50000);
        ArrayList<Boolean> res = solve(new BufferedReader(new StringReader(testCase.toString())));
        System.out.println(res.size());
    }
}
