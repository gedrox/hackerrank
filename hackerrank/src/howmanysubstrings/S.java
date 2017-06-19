package howmanysubstrings;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class S {
    static String s;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int q = in.nextInt();
        s = in.next();

        Node[] nodes = new Node[26];
        LinkedList<Node> Q = new LinkedList<>();

        char[] charArray = s.toCharArray();
        for (int p = 0; p < charArray.length; p++) {
            char c = charArray[p];
            int i = c - 'a';
            if (nodes[i] == null) {
                nodes[i] = new Node();
                Q.add(nodes[i]);
            }
            nodes[i].pos.add(p);
        }

        while (!Q.isEmpty()) {
            Node node = Q.pollFirst();
            for (int p : node.pos) {
                if (p < s.length() - 1) {
                    char c = charArray[p + 1];
                    int i = c - 'a';
                    if (node.next[i] == null) {
                        node.next[i] = new Node();
                        Q.add(node.next[i]);
                    }
                    node.next[i].pos.add(p + 1);
                }
            }
        }

        System.err.println("Prep finish");

        LinkedList<Pair> QQ = new LinkedList<>();

        for (int a0 = 0; a0 < q; a0++) {
            int left = in.nextInt();
            int right = in.nextInt();

            for (Node node : nodes) {
                if (node != null)
                    QQ.add(Pair.of(node, 0));
            }

            long res = 0;

            while (!QQ.isEmpty()) {
                Pair p = QQ.pollFirst();
                int lp = Collections.binarySearch(p.node.pos, left + p.depth);
                if (lp < 0) lp = -lp - 1;
                int rp = Collections.binarySearch(p.node.pos, right);
                if (rp < 0) rp = -rp - 2;

                if (lp <= rp) {
                    res++;
                    for (Node ch : p.node.next) {
                        if (ch != null) {
                            QQ.add(Pair.of(ch, p.depth + 1));
                        }
                    }
                }
            }
            System.out.println(res);
        }
    }

    static class Pair {
        Node node;
        int depth;

        static Pair of(Node node, int depth) {
            Pair p = new Pair();
            p.node = node;
            p.depth = depth;
            return p;
        }
    }

    @Test
    public void test() {

        StringBuilder s = new StringBuilder();

        int n = 30000;
        int q = 30000;
        Random r = new Random();
        s = s.append(n).append(" ").append(q).append('\n');
        for (int i = 0; i < n; i++) {
            s = s.append((char) ('a' + r.nextInt(26)));
        }
        s = s.append('\n');
        for (int i = 0; i < q; i++) {
            int le = r.nextInt(n);
            int ri = r.nextInt(n);
            s = s.append(Math.min(le, ri)).append(" ").append(Math.max(le, ri)).append('\n');
        }

        System.out.println("ready");

//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        S2.solve(new Scanner(s), new PrintStream(o, true));

        ByteArrayOutputStream o3 = new ByteArrayOutputStream();
        S3.solve(new Scanner(s.toString()), new PrintStream(o3, true));

//        Assert.assertArrayEquals(o.toByteArray(), o3.toByteArray());
    }

    static class Node {
        Node[] next = new Node[26];
        ArrayList<Integer> pos = new ArrayList<>();
    }
}
