package dag;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] split = bi.readLine().split(" ");

        measure();

        int n = parseInt(split[0]);
        int m = parseInt(split[1]);
        int q = parseInt(split[2]);

        StringBuilder out = new StringBuilder();

        ArrayList<Integer>[] next = new ArrayList[n];
        ArrayList<Integer>[] prev = new ArrayList[n];
        ArrayList<Integer>[] nextChpts = new ArrayList[n];
        ArrayList<Integer>[] prevChpts = new ArrayList[n];

        int[] set = new int[n];
        int[] setI = new int[n];
        ArrayList<Integer>[] min = new ArrayList[n];
        ArrayList<Integer>[] minI = new ArrayList[n];

        int[] countPrev = new int[n];
        boolean[] chpts = new boolean[n];

        for (int n_i = 0; n_i < n; n_i++) {
            next[n_i] = new ArrayList<>();
            prev[n_i] = new ArrayList<>();
            min[n_i] = new ArrayList<>();
            minI[n_i] = new ArrayList<>();

            nextChpts[n_i] = new ArrayList<>();
            prevChpts[n_i] = new ArrayList<>();
        }

        measure();

        for (int m_i = 0; m_i < m; m_i++) {
            split = bi.readLine().split(" ");
            int nodeA = parseInt(split[0]) - 1;
            int nodeB = parseInt(split[1]) - 1;

            next[nodeA].add(nodeB);
            prev[nodeB].add(nodeA);
            countPrev[nodeB]++;
        }

        measure();

        LinkedList<Integer> queue = new LinkedList<>();
        boolean[] visited;
        // destroy shortcuts
        //.....

        double chptCount = Math.sqrt(n);

        if (chptCount > 0) {

            V[] vs = new V[n];
            for (int n_i = 0; n_i < n; n_i++) {
                vs[n_i] = new V(n_i, countPrev[n_i]);
            }

//            Arrays.sort(vs, (o1, o2) -> o1.v != o2.v ? o2.v - o1.v : (int) Math.signum(o1.i - o2.i) * Objects.hash(Math.abs(o1.i - o2.i)));


            for (int n_i = 0; n_i < chptCount; n_i++) {
//                int chpt = vs[n_i].i;
                int chpt;
                do {
                    chpt = (int) Math.floor(n * Math.random());
                } while (chpts[chpt]);
                chpts[chpt] = true;

                visited = new boolean[n];
                queue.add(chpt);
                while (!queue.isEmpty()) {
                    Integer node = queue.pollFirst();
                    prevChpts[node].add(chpt);
                    for (Integer nextNode : next[node]) {
                        if (!chpts[nextNode] && !visited[nextNode]) {
                            queue.add(nextNode);
                            visited[nextNode] = true;
                        }
                    }
                }

                visited = new boolean[n];
                queue.addAll(prev[chpt]);
                while (!queue.isEmpty()) {
                    Integer node = queue.pollFirst();
                    nextChpts[node].add(chpt);
                    if (!chpts[node]) {
                        for (Integer prevNode : prev[node]) {
                            if (!visited[prevNode]) {
                                queue.add(prevNode);
                                visited[prevNode] = true;
                            }
                        }
                    }
                }
            }
        }

        measure();

        Avg[] AVG = {new Avg(), new Avg(), new Avg()};

        for (int q_i = 1; q_i <= q; q_i++) {
            split = bi.readLine().split(" ");
            int type = parseInt(split[0]);
            int node = parseInt(split[1]) - 1;
            int x = split.length > 2 ? parseInt(split[2]) : -1;

//            int chpt=node;



            int CNT = 0;
            switch (type) {
                case 1:
                case 2:
                    queue.add(node);
                    while (!queue.isEmpty()) {
                        CNT++;
                        int chpt = queue.pollFirst();
                        if (type == 1) {
                            set[chpt] = x;
                            setI[chpt] = q_i;
                            min[chpt].clear();
                            minI[chpt].clear();
                        } else {
                            min[chpt].add(x);
                            minI[chpt].add(q_i);
                        }
                        queue.addAll(nextChpts[chpt]);
                    }

                    break;
                case 3:
                    int lastSet = 0;
                    int lastSetI = 0;

                    visited = new boolean[n];
                    queue.add(node);
                    ArrayList<Integer> visitAgain = new ArrayList<>();
                    while (!queue.isEmpty()) {
                        CNT++;
                        int p = queue.pollFirst();
                        visitAgain.add(p);
                        if (setI[p] > lastSetI) {
                            lastSet = set[p];
                            lastSetI = setI[p];
                        }
                        if (!chpts[p]) {
                            for (Integer pr : prev[p]) {
                                if (!visited[pr]) {
                                    queue.addAll(prev[p]);
                                    visited[pr] = true;
                                }
                            }
                        }
                    }

                    int smallest = lastSet;
                    for (Integer p : visitAgain) {
                        int pos = Collections.binarySearch(minI[p], lastSetI);
                        pos = -pos - 1;
                        for (int p_i = pos; p_i < minI[p].size(); p_i++) {
                            if (min[p].get(p_i) < smallest) {
                                smallest = min[p].get(p_i);
                            }

                        }
                    }

                    out.append(smallest).append('\n');

                    break;
            }

            AVG[type - 1].add(CNT);
        }

        measure();

        System.err.println(AVG[0].v / AVG[0].c);
        System.err.println(AVG[1].v / AVG[1].c);
        System.err.println(AVG[2].v / AVG[2].c);
//        System.out.println(out.toString());
    }

    static long t0;

    private static void measure() {
        long t = System.currentTimeMillis();
        if (t0 == 0) {
            t0 = t;
        } else {
            System.err.println(t - t0);
            t0 = t;
        }
    }

    static class V {
        int i, v;

        public V(int i, int v) {
            this.i = i;
            this.v = v;
        }
    }

    static class Avg {
        int c;
        double v;

        void add(double v) {
            c++;
            this.v += v;
        }
    }
}
