package hackerrank;

import org.junit.Test;

import java.util.*;

public class Poles {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();

        int[] a = new int[n];
        int[] w = new int[n];

        for (int a0 = 0; a0 < n; a0++) {
            a[a0] = in.nextInt();
            w[a0] = in.nextInt();
        }

        long max = Math.min(solve(list(a), list(w), k), solve2(a, w, k));
        System.out.println(solveForce(a, w, k, max));
    }

    @Test
    public void test() {
        System.out.println(solve2(new int[]{20, 30, 40}, new int[]{1, 1, 1}, 1));
    }

    @Test
    public void test2() {
        System.out.println(solve(list(10, 12, 16, 18, 30, 32), list(15, 17, 18, 13, 10, 1), 2));
        System.out.println(solve2(new int[]{10, 12, 16, 18, 30, 32}, new int[]{15, 17, 18, 13, 10, 1}, 2));
    }

    private static List<Integer> list(int... arr) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int el : arr) res.add(el);
        return res;
    }

    static long solve2(int[] a, int[] w, int k) {
        long total = 0;
        int n = a.length;
//        Integer[] w2 = w.clone();

        for (int i = k; i < n; i++) {
//            w2[k - 1] += w2[i];
            total += ((long) a[i] - a[k - 1]) * w[i];
        }

        int PREV = 0, CURR = 1, NEXT = 2;

        int[][] conf = new int[k][];
        for (int i = 0; i < k; i++) {
            conf[i] = new int[]{i - 1, i, i + 1};
        }
        conf[k - 1][NEXT] = n;

        while (true) {
            boolean anychanges = false;
            for (int which = k - 1; which > 0; which--) {
                int floo = conf[which][CURR];
                int prev = conf[which][PREV];
                int ceil = conf[which][NEXT];

                long smallest = Integer.MAX_VALUE;
                int bestI = -1;

                for (int i = floo + 1; i < ceil; i++) {
                    long diff = ((long) a[i - 1] - a[prev]) * w[i - 1];
                    for (int j = i; j < ceil; j++) {
                        diff -= ((long) a[i] - a[i - 1]) * w[j];
                    }
                    if (diff < 0 && diff < smallest) {

                        smallest = diff;
                        bestI = i;


                    } else {
                        break;
                    }
                }

                if (bestI >= 0) {
                    total += smallest;
                    anychanges = true;

                    conf[which][CURR] = bestI;
                    if (which != k - 1) {
                        conf[which + 1][PREV] = bestI;
                    }
                    conf[which - 1][NEXT] = bestI;
                }
            }

            if (!anychanges) break;
        }

        System.err.println(Arrays.deepToString(conf));

        return total;
    }

//    static long solve3(int[] a, int[] w, int k) {
//        int n = a.length;
//        ArrayList<int[]> splits = new ArrayList<>();
//        splits.add(new int[]{0, n});
//        long total = 0;
//        for (int i = 0; i < n; i++) {
//            total += ((long) a[i] - a[0]) * w[i];
//        }
//
//        for (int k_i = 0; k_i < k - 1; k_i++) {
//            
//            long bestDiff = Long.MAX_VALUE;
//            int bestSplit = -1;
//            int bestIndex = -1;
//            
//            for (int i = 0; i < splits.size(); i++) {
//                long diff = 0;
//                for (int j = splits.get(i)[1] - 1; j > splits.get(i)[0]; j--) {
//                    
//                }
//            }
//        }
//    }

    static long solve(List<Integer> a, List<Integer> w, int k) {
        long total = 0;

        while (a.size() != k) {
            long min = Long.MAX_VALUE;
            int move = -1;
            for (int i = 1; i < a.size(); i++) {
                long cost = ((long) a.get(i) - a.get(i - 1)) * w.get(i);
                if (cost < min) {
                    min = cost;
                    move = i;
                }
            }

            total += min;
            w.set(move - 1, w.get(move - 1) + w.get(move));
            a.remove(move);
            w.remove(move);
        }

        return total;
    }

    @Test
    public void testRand3() {
        while (true) {
            int n = 5;
            int[] a = new int[n];
            int[] w = new int[n];

            Random rand = new Random();

            for (int i = 0; i < n; i++) {
                a[i] = (i == 0 ? 0 : rand.nextInt(100) + a[i - 1]);
                w[i] = rand.nextInt(100);
            }

            //        int[] a = {0, 1, 50, 100};
            //        int[] w = {1, 10, 100, 50};


            long solve = solve(list(a), list(w), 3);
            long x = solve2(a, w, 3);
            long x1 = solveFor3(a, w);

            if (Math.min(solve, x) != x1) {
                System.err.println(Arrays.toString(a));
                System.err.println(Arrays.toString(w));
                throw new RuntimeException(solve + " != " + x1);
            }
        }
    }

    @Test
    public void testSpecial() {
        int[] a = {0, 5, 15, 20, 29};
        int[] w = {0, 60, 13, 33, 50};

        System.out.println(solve(list(a), list(w), 3));
        System.out.println(solve2(a, w, 3));
        System.out.println(solveFor3(a, w));

        System.out.println("now.,.");
        System.out.println(solveForce(a, w, 3, Math.min(solve(list(a), list(w), 3), solve2(a, w, 3))));
    }

    static long solveForce(int[] a, int[] w, int k, long max) {
        
        HashMap<Path, Long> cache = new HashMap<>();
        
        int n = a.length;
        long[][] x = new long[n][n];
        for (int from = 0; from < n; from++) {
            for (int to = 0; to < from; to++) {
                x[from][to] = (a[from] - a[to]) * w[from];
            }
        }

        Path init = new Path();
        LinkedList<Path> q = new LinkedList<>();
        q.add(init);

        while (!q.isEmpty()) {
            Path path = q.pollFirst();

            if (path.total >= max) continue;
            
            if (cache.containsKey(path)) {
                if (cache.get(path) <= path.total) {
                    continue;
                }
            }
            cache.put(path, path.total);

            if (path.curr == n - 1) {
                if (path.total < max) {
                    max = path.total;
                }
            } else {
                if (path.baseCount < k) {
                    Path nextBasePath = path.clone();
                    nextBasePath.curr++;
                    nextBasePath.base = nextBasePath.curr;
                    nextBasePath.baseCount++;

                    q.add(nextBasePath);
                }
                if (n - path.curr > k - path.baseCount) {
                    path.curr++;
                    path.total += x[path.curr][path.base];

                    q.add(path);
                }
            }
        }

        return max;
    }

    static class Path implements Cloneable {
        int base;
        int curr;
        int baseCount = 1;
        long total;

        public Path clone() {
            try {
                return (Path) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Path path = (Path) o;

            if (curr != path.curr) return false;
            return baseCount == path.baseCount;
        }

        @Override
        public int hashCode() {
            int result = curr;
            result = 31 * result + baseCount;
            return result;
        }
    }

    static long solveFor3(int[] a, int[] w) {

        long min = Long.MAX_VALUE;
        int[] solution = null;

        long sum1 = 0;
        for (int i = 1; i < a.length; i++) {

            sum1 += ((long) a[i - 1] - a[0]) * w[i - 1];

            long sum2 = 0;
            for (int j = i + 1; j < a.length; j++) {
                sum2 += ((long) a[j - 1] - a[i]) * w[j - 1];

                long sum = sum1 + sum2;
                for (int k = j + 1; k < a.length; k++) {
                    sum += ((long) a[k] - a[j]) * w[k];
                }

                if (sum < min) {
                    min = sum;
                    solution = new int[]{i, j};
                }
            }
        }

        System.err.println(Arrays.toString(solution));

        return min;
    }
}
