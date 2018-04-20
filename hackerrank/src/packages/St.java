package packages;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class St {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int n;
    private static double sqrtn;

    static int readInt() throws IOException {
        int res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }

    public static void main(String[] args) throws IOException {

        n = readInt();

        int[] rawT = new int[n];
        for (int tItr = 0; tItr < n; tItr++) {
            rawT[tItr] = readInt();
        }
        int q = readInt();
        int[] a = new int[q];
        int[] b = new int[q];
        for (int qItr = 0; qItr < q; qItr++) {
            a[qItr] = readInt() - 1;
            b[qItr] = readInt() - 1;
        }
        Student[] st = solve(rawT, q, a, b);

        for (Student s : st) {
            System.out.println(s.answer);
        }
    }

    private static Student[] solve(int[] rawT, int q, int[] a, int[] b) {

        sqrtn = Math.sqrt(n);
//        sqrtn = 1000;
//        long t0;
//        t0 = System.currentTimeMillis();

        // types starting 0
        int[] t = new int[n];
        HashMap<Integer, Integer> types = new HashMap<>();
        boolean allDiff = true;

        for (int tItr = 0; tItr < n; tItr++) {
            int tItem = rawT[tItr];
            if (!types.containsKey(tItem)) {
                types.put(tItem, types.size());
            } else {
//                if (allDiff) {
//                    System.err.println("NOT DIFFERENT");
//                }
                allDiff = false;
            }
            t[tItr] = types.get(tItem);
        }


//        System.err.println(System.currentTimeMillis() - t0);
//        t0 = System.currentTimeMillis();


        Student[] st = new Student[q];
        Student[] ord = new Student[q];

        for (int qItr = 0; qItr < q; qItr++) {
            ord[qItr] = st[qItr] = new Student(a[qItr], b[qItr]);
        }

        if (allDiff) {
            for (Student s : st) {
                s.answer = s.j - s.i + 1;
            }
            return st;
        }

//        System.err.println(System.currentTimeMillis() - t0);
//        t0 = System.currentTimeMillis();

        Arrays.sort(ord);

//        System.err.println(System.currentTimeMillis() - t0);
//        t0 = System.currentTimeMillis();

        int x = ord[0].i, y = ord[0].i;
        int[] cnt = new int[types.size()];
        cnt[t[x]] = 1;
        int dist = 1;
        int steps = 0;

//        TreeSet<Integer> jumps = new TreeSet<>();

        for (int qItr = 0; qItr < q; qItr++) {
            Student s = ord[qItr];

            // reread
//            if (Math.abs(s.i - x) + Math.abs(s.j - y) > s.j - s.i + 100000) {
////                System.err.println("Reread");
//                x = s.i;
//                y = s.i;
//                cnt = new int[types.size()];
//                cnt[t[s.i]] = 1;
//                dist = 1;
//            }

//            jumps.add(-(Math.abs(s.i - x) + Math.abs(s.j - y)));
            steps += Math.abs(s.i - x) + Math.abs(s.j - y);
            
            if (x < s.i) {
                while (x < s.i) {
                    int newVal = --cnt[t[x]];
                    if (newVal == 0) --dist;
                    else if (newVal == 1) ++dist;
                    x++;
                }
            } else {
                while (x > s.i) {
                    x--;
                    int newVal = ++cnt[t[x]];
                    if (newVal == 1) ++dist;
                    else if (newVal == 2) --dist;
                }
            }
            if (y > s.j) {
                while (y > s.j) {
                    int newVal = --cnt[t[y]];
                    if (newVal == 0) --dist;
                    else if (newVal == 1) ++dist;
                    y--;
                }
            } else {
                while (y < s.j) {
                    y++;
                    int newVal = ++cnt[t[y]];
                    if (newVal == 1) ++dist;
                    else if (newVal == 2) --dist;
                }
            }

            s.answer = dist;
        }
        System.err.println("STEPS = " + (1. * steps));

//        System.err.println(System.currentTimeMillis() - t0);
//        t0 = System.currentTimeMillis();

        return st;
    }

    @Test
    public void generate() {
        Random r = new Random(0);
        int n = 1000000;
        int q = 1000000;
        int minT = 0;
        int maxT = 1000000;

        int[] t = new int[n];
        for (int i = 0; i < n; i++) {
            t[i] = r.nextInt(maxT - minT + 1) + minT;
//            t[i] = i;
        }
        int[] a = new int[q];
        int[] b = new int[q];
        for (int i = 0; i < q; i++) {
            a[i] = r.nextInt(n);
            b[i] = r.nextInt(n);
            if (a[i] > b[i]) {
                int tmp = a[i];
                a[i] = b[i];
                b[i] = tmp;
            }
        }

//        System.out.println(n);
//        for (int i = 0; i < n; i++) {
//            System.out.print(t[i] + " ");
//        }
//        System.out.println();
//        System.out.println(q);
//        for (int i = 0; i < q; i++) {
//            System.out.println(a[i] + " " + b[i]);
//        }


        long t0 = System.currentTimeMillis();
        St.n = n;
        Student[] answ = solve(t, q, a, b);
        System.err.println(System.currentTimeMillis() - t0);

        if (false)
            for (int i = 0; i < q; i++) {
                HashMap<Integer, Integer> count = new HashMap<>();
                for (int p = a[i]; p <= b[i]; p++) {
                    count.compute(t[p], (k, v) -> v == null ? 1 : v + 1);
                }
                int c = 0;
                for (int v : count.values()) {
                    if (v == 1) {
                        c++;
                    }
                }

//            System.out.println(" I think " + c + ", script thinks " + answ[i].answer);

                assert c == answ[i].answer;
            }
    }

    static class Student implements Comparable<Student> {
        final int i, j, a;
        int b;
        int answer;

        public Student(int i, int j) {
            this.i = i;
            this.j = j;

            a = (int) Math.floor(i / sqrtn);
            b = (int) Math.floor(j / sqrtn);
        }

        @Override
        public int compareTo(Student o) {
            if (a != o.a) return a - o.a;
            if (j != o.j) return (a % 2 == 0 ? 1 : -1) * (j - o.j);
//            if (b != o.b) return (a % 2 == 0 ? 1 : -1) * (b - o.b);
//            if (j != o.j) return (j - o.j);
            return i - o.i;

        }

        @Override
        public String toString() {
            return "Student{" +
                    "i=" + i +
                    ", j=" + j +
                    ", a=" + a +
                    '}';
        }
    }

}
