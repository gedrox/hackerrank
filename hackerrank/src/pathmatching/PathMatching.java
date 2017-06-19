package pathmatching;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PathMatching {

    private static Node[] nodes;
    private static ArrayList<Node>[] byLevel;
    private static int n;
    private static String s;
    private static String p;
    private static int edges;
    private static int[] count;
    private static int[] revCount;
    private static int pLen;
    
//    @Test
//    public void testCase16() throws IOException {
//        main(null);
//    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/pathmatching/case16.in")));
        String[] split = br.readLine().split(" ");

        n = Integer.parseInt(split[0]);

        int q = Integer.parseInt(split[1]);
        s = br.readLine();
        p = br.readLine();

        for (int a0 = 0; a0 < n - 1; a0++) {
            split = br.readLine().split(" ");
            int u = Integer.parseInt(split[0]);
            int v = Integer.parseInt(split[1]);
            addEdge(u, v);
        }

        StringBuilder sb = new StringBuilder();
        for (int a0 = 0; a0 < q; a0++) {
            split = br.readLine().split(" ");
            int u = Integer.parseInt(split[0]);
            int v = Integer.parseInt(split[1]);

            int c = solve(u, v);
//            System.out.println(c);
            sb.append(c).append('\n');
        }
        System.out.print(sb.toString());
    }

    private static void init() {

        pLen = p.length();
        
        LinkedList<Node> queue = new LinkedList<>();
        nodes[0].level = 0;
        queue.add(nodes[0]);

        byLevel = new ArrayList[n];

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (byLevel[node.level] == null) {
                byLevel[node.level] = new ArrayList<>();
            }
            byLevel[node.level].add(node);

            for (Node next : node.next) {
                if (next.level == -1) {
                    node.children.add(next);
                    next.parent = node;
                    next.level = node.level + 1;
                    queue.add(next);
                }
            }
        }

        Node[] sorted = new Node[n];
        int sorted_i = 0;
        
        LinkedList<int[]> lrq = new LinkedList<>();
        lrq.add(new int[]{0, 1});
        lrq.add(new int[]{0, 2});
        int x = 0;

        while (!lrq.isEmpty()) {
            int[] next = lrq.poll();
            Node node = nodes[next[0]];
            if (next[1] == 1) {
                sorted[sorted_i++] = node;
                node.left = x++;
                for (Node child : node.children) {
                    lrq.addFirst(new int[]{child.index, 2});
                    lrq.addFirst(new int[]{child.index, 1});
                }
            } else {
                node.right = x++;
            }
        }

        for (ArrayList<Node> levelNodes : byLevel) {
            if (levelNodes == null) break;
            levelNodes.sort(Comparator.comparing(node -> node.left));
        }
        
        count = doCount(sorted, p);
        revCount = doCount(sorted, new StringBuilder(p).reverse().toString());
    }

    private static int[] doCount(Node[] sorted, String p) {
        int[] count = new int[n];
        ArrayList<Integer>[] state = new ArrayList[n];

        for (Node node : sorted) {
            ArrayList<Integer> parentState;
            if (node.parent == null) {
                parentState = new ArrayList<>();
            } else {
                parentState = state[node.parent.index];
                count[node.index] = count[node.parent.index];
            }

            state[node.index] = new ArrayList<>(parentState.size() + 1);

            char ch = s.charAt(node.index);
            for (int ind : parentState) {
                if (ch == p.charAt(ind)) {
                    if (ind + 1 == p.length()) {
                        count[node.index]++;
                    } else {
                        state[node.index].add(ind + 1);
                    }
                }
            }

            if (ch == p.charAt(0)) {
                if (p.length() == 1) {
                    count[node.index]++;
                } else {
                    state[node.index].add(1);
                }
            }
        }
        
        return count;
    }

    private static int solve(int u, int v) {
        u--;
        v--;
        
        Node a = nodes[u];
        Node b = nodes[v];

        StringBuilder prefix = new StringBuilder();
        StringBuilder suffix = new StringBuilder();

        Node anc = ancestor(a, b);
        assert isAnc(anc, a) && isAnc(anc, b);
        
        int answer = 0;
        
        int depthA = a.level - anc.level + 1;
        if (depthA >= pLen) {
            answer += revCount[u];
            if (anc.level + pLen - 2 >= 0) {
                Node ancA = ancestor(a, anc.level + pLen - 2);
                answer -= revCount[ancA.index];
            }
        }

        int depthB = b.level - anc.level + 1;
        if (depthB >= pLen) {
            answer += count[v];
            if (anc.level + pLen - 2 >= 0) {
                Node ancB = ancestor(b, anc.level + pLen - 2);
                answer -= count[ancB.index];
            }
        }
        
        if (pLen == 1) {
            if (p.charAt(0) == s.charAt(anc.index)) {
                answer--;
            }
            return answer;
        }
        if (pLen == 2) {
            return answer;
        }
        
        if (depthA >= pLen) {
            a = ancestor(a, anc.level + pLen - 2);
        }
        if (depthB >= pLen) {
            b = ancestor(b, anc.level + pLen - 2);
        }
        
        while (a != anc) {
            prefix.append(s.charAt(a.index));
            a = a.parent;
        }
        while (b != anc) {
            suffix.append(s.charAt(b.index));
            b = b.parent;
        }
        prefix.append(s.charAt(anc.index));

        String sub = prefix.toString() + suffix.reverse().toString();

        int offset = 0;
        while ((offset = sub.indexOf(p, offset) + 1) != 0) {
            answer++;
        }
        return answer;
    }

    private static void addEdge(int u, int v) {
        assert edges < n - 1;
        if (edges == 0) {
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }
        }
        
        u--;
        v--;
        nodes[u].next.add(nodes[v]);
        nodes[v].next.add(nodes[u]);

        edges++;
        if (edges == n - 1) {
            init();
        }
    }
    
    static Node ancestor(Node a, int lvl) {
        int ind = Collections.binarySearch(byLevel[lvl], a, Comparator.comparing(node -> node.left));
        return byLevel[lvl].get(-ind - 2);
    }

    static Node ancestor(Node a, Node b) {
        if (isAnc(a, b)) {
            return a;
        }
        if (isAnc(b, a)) {
            return b;
        }
        
        int lvl0 = 0;
        int lvl1 = Math.min(a.level, b.level);
        
        Node prnt = nodes[0];
        while (lvl1 - lvl0 > 1) {
            int lvl = (lvl1 + lvl0) / 2;
            Node prntCand = ancestor(a, lvl);
            if (isAnc(prntCand, a) && isAnc(prntCand, b)) {
                prnt = prntCand;
                lvl0 = lvl;
            } else {
                lvl1 = lvl;
            }
        }
        
        return prnt;
    }

    private static boolean isAnc(Node a, Node b) {
        return a.left <= b.left && a.right >= b.right;
    }

    static class Node {
        final int index;
        ArrayList<Node> next = new ArrayList<>();
        Node parent;
        ArrayList<Node> children = new ArrayList<>();
        int level = -1;
        int left;
        int right;

        Node(int index) {
            this.index = index;
        }
    }
    
    @Test
    public void testSimple() {
        n = 5;

        s = "abbaa";
        p = "ab";
        
        addEdge(1, 2);
        addEdge(2, 4);
        addEdge(2, 5);
        addEdge(1, 3);

        Assert.assertEquals(1, solve(4, 5));
        Assert.assertEquals(1, solve(5, 4));
        Assert.assertEquals(2, solve(4, 3));
        Assert.assertEquals(1, solve(3, 4));
        Assert.assertEquals(0, solve(2, 4));
    }

    @Test
    public void testSimple2() {
        n = 5;

        s = "abbaa";
        p = "a";

        addEdge(1, 2);
        addEdge(2, 4);
        addEdge(2, 5);
        addEdge(1, 3);

        Assert.assertEquals(2, solve(4, 5));
        Assert.assertEquals(2, solve(5, 4));
        Assert.assertEquals(2, solve(4, 3));
        Assert.assertEquals(2, solve(3, 4));
        Assert.assertEquals(1, solve(2, 4));
    }

    @Test
    public void testSimple3() {
        n = 5;

        s = "abbaa";
        p = "aba";

        addEdge(1, 2);
        addEdge(2, 4);
        addEdge(2, 5);
        addEdge(1, 3);

        Assert.assertEquals(1, solve(4, 5));
        Assert.assertEquals(1, solve(5, 4));
        Assert.assertEquals(1, solve(4, 3));
        Assert.assertEquals(1, solve(3, 4));
        Assert.assertEquals(0, solve(2, 4));
    }

    @Test
    public void testBig() {
        Random r = new Random(0);
        n = 100000;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }
        s = sb.toString();
        p = s.substring(0, 10);

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < n - 1; i++) {
            addEdge(i + 2, r.nextInt(i + 1) + 1);
//            addEdge(i + 2, i + 1);
        }
        System.out.println(System.currentTimeMillis() - t0 + "ms");

        int[] expected = {};//2, 6, 7, 3, 5, 7, 6, 6, 8, 6, 3, 0, 5, 2, 6, 6, 7, 3, 4, 7, 7, 6, 3, 8, 8, 3, 4, 5, 4, 8, 4, 9, 5, 1, 18, 5, 4, 5, 8, 6, 5, 4, 4, 3, 7, 8, 6, 4, 12, 1, 8, 1, 5, 3, 6, 4, 1, 2, 3, 5, 4, 5, 8, 8, 2, 3, 2, 5, 5, 5, 2, 6, 6, 2, 2, 6, 7, 1, 6, 5, 3, 7, 1, 9, 8, 5, 4, 7, 6, 8, 8, 2, 4, 2, 9, 8, 6, 2, 4, 7, 5, 10, 9, 5, 4, 2, 2, 2, 11, 11, 4, 3, 5, 11, 4, 9, 9, 3, 6, 5, 4, 1, 3, 8, 8, 5, 3, 1, 5, 9, 6, 5, 7, 4, 7, 7, 4, 6, 6, 2, 3, 4, 3, 7, 8, 4, 5, 0, 2};
        
        for (int i = 0; i < n; i++) {
//            int answer = solve(n/2, n);
            int answer = solve(r.nextInt(n) + 1, r.nextInt(n) + 1);
//            System.out.print(answer + ", ");
//            if (i < expected.length) {
//                Assert.assertEquals(expected[i], answer);
//            }
        }

        System.out.println(System.currentTimeMillis() - t0 + "ms");
    }
}
