import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Hard {
    public static void main(String[] args) throws FileNotFoundException {
//        Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new FileInputStream("hard.in"));
        int n = in.nextInt();
        int m = in.nextInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].i = i;
        }

        Path[] paths = new Path[m];

        for (int a0 = 0; a0 < m; a0++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int a = in.nextInt();
            int b = in.nextInt();
            paths[a0] = new Path(nodes[u], nodes[v], a, b);
        }

        Arrays.sort(paths, Comparator.<Path>comparingDouble(p -> - p.r).thenComparing(Comparator.comparingInt(p -> p.a)));

        ArrayList<Integer>[] pos = new ArrayList[n];
        for (int i1 = 0; i1 < n; i1++) {
            pos[i1] = new ArrayList<>();
        }
        for (int i1 = 0; i1 < m; i1++) {
            pos[paths[i1].source.i].add(i1);
            pos[paths[i1].target.i].add(i1);
        }

        int prevA = -1, prevB = -1, A, B;

        while (true) {

            System.err.println("\nnext\n");

            int[] colors = new int[n];
            ArrayList<Integer>[] colored = new ArrayList[n];
            for (int i1 = 0; i1 < n; i1++) {
                colors[i1] = i1;
                colored[i1] = new ArrayList<>();
                colored[i1].add(i1);
            }

            A = 0;
            B = 0;

            int rem = n - 1;
//            boolean[] visited = new boolean[n];
//            ArrayList<Integer> X = new ArrayList<>();
//            visited[0] = true;
//            X.addAll(pos[0]);

            boolean negative = false;

            int ind = 0;

            while (rem > 0) {

                Path path = paths[ind++];

                if (colors[path.source.i] != colors[path.target.i]) {

                    double prevR = 1. * prevA / prevB;
                    if (!negative && prevA != -1 && path.r <= prevR) {
                        int finalB = prevB;
                        ind--;
                        Arrays.sort(paths, ind, m, Comparator.comparingDouble((p) -> (prevR - p.r) / (1. * finalB / p.b + 1)));
                        negative = true;

                        continue;
                    }

//                    if (!visited[path.source.i]) {
//                        visited[path.source.i] = true;
//                        for (int pathId : pos[path.source.i]) {
//                            if (!visited[paths[pathId].source.i] || !visited[paths[pathId].target.i]) {
//                                X.add(pathId);
//                            }
//                        }
//                    } else {
//                        visited[path.target.i] = true;
//                        for (int pathId : pos[path.target.i]) {
//                            if (!visited[paths[pathId].source.i] || !visited[paths[pathId].target.i]) {
//                                X.add(pathId);
//                            }
//                        }
//                    }

                    if (colored[colors[path.source.i]].size() < colored[colors[path.target.i]].size()) {
                        colored[colors[path.target.i]].addAll(colored[colors[path.source.i]]);
                        ArrayList<Integer> clear = colored[colors[path.source.i]];
                        for (int index : colored[colors[path.source.i]]) {
                            colors[index] = colors[path.target.i];
                        }
                        clear.clear();
                    } else {
                        colored[colors[path.source.i]].addAll(colored[colors[path.target.i]]);
                        ArrayList<Integer> clear = colored[colors[path.target.i]];
                        for (int index : colored[colors[path.target.i]]) {
                            colors[index] = colors[path.source.i];
                        }
                        clear.clear();
                    }

                    A += path.a;
                    B += path.b;
                    rem--;

//                    System.err.println(path);

//                    Collections.sort(X);
                }

            }

            if (A == prevA && B == prevB) {
                break;
            }

            prevA = A;
            prevB = B;
        }

        int C = gcd(A, B);

        System.out.println((A/C) + "/" + (B/C));
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            a = a % b;
            int tmp = a;
            a = b;
            b = tmp;
        }
        return a;
    }

    static class Path {
        Node source;
        Node target;
        int a;
        int b;
        double r;

        public Path(Node source, Node target, int a, int b) {
            this.source = source;
            this.target = target;
            this.a = a;
            this.b = b;
            r = ((double) a) / b;
        }

        @Override
        public String toString() {
            return Math.min(source.i, target.i) + "\t" + Math.max(source.i, target.i) + "\t" + a + "\t" + b + "\t" + r;
        }
    }

    static class Node {
        int i;
//        ArrayList<Path> next = new ArrayList<>();
    }
}
