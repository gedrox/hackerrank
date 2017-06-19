package hc2017r1.manic;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class ManicMoving {

    static final int PICK_1ST = 0;
    static final int PICK_2ND = 1;
    static final int DROP_SINGLE = 2;
    static final int DROP_2ND = 3;

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String line = bi.readLine();

        long longest = 0;

        // file path from "copy as path" feature
        if (line.contains(":") || line.contains("/")) {
            line = line.replace("\"", "");
            bi = new BufferedReader(new InputStreamReader(new FileInputStream(line)));
            line = bi.readLine();
        }

        int t = parseInt(line);
        StringBuilder sb = new StringBuilder();
        for (int t_i = 1; t_i <= t; t_i++) {
            String[] split = bi.readLine().split(" ");
            int n = parseInt(split[0]);
            int m = parseInt(split[1]);
            int k = parseInt(split[2]);

            World w = new World(n);
            for (int i = 0; i < m; i++) {
                split = bi.readLine().split(" ");
                w.join(parseInt(split[0]) - 1, parseInt(split[1]) - 1, parseInt(split[2]));
            }

            int[] s = new int[k];
            int[] d = new int[k];
            for (int i = 0; i < k; i++) {
                split = bi.readLine().split(" ");
                s[i] = parseInt(split[0]) - 1;
                d[i] = parseInt(split[1]) - 1;
            }

            long t0 = System.currentTimeMillis();
            long answer = solve(w, s, d);
            longest = Math.max(longest, System.currentTimeMillis() - t0);

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');

            System.out.print('.');
        }

        Files.write(Paths.get("src/hc2017r1/i-love-manic.txt"), sb.toString().getBytes());

        System.out.println();
        System.out.print(sb.toString());
        System.out.println();
        System.out.printf("Longest run was %dms%n", longest);
    }

    static long solve(World w, int[] s, int[] d) {
        Long[][] dist = calculateDistances(w);
        if (!checkExistence(s, d, dist)) {
            return -1;
        }
        long path = constantPath(s, d, dist);
        path += shortestPath(s, d, dist);
        return path;
    }

    static Long[][] calculateDistances(World w) {

        Long[][] min = new Long[w.n][];
        for (int i = 0; i < w.n; i++) {
            min[i] = new Long[w.n];
            min[i][i] = 0L;
        }

        LinkedList<Integer> q = new LinkedList<>();
        for (int i = 0; i < w.n; i++) {
            q.add(i);
            while (!q.isEmpty()) {
                int city = q.pollFirst();
                for (Road road : w.cities[city]) {
                    if (min[i][road.target] == null || min[i][road.target] > min[i][city] + road.cost) {
                        min[i][road.target] = min[i][city] + road.cost;
                        q.add(road.target);
                    }
                }
            }

        }

        return min;
    }

    private static boolean checkExistence(int[] s, int[] d, Long[][] dist) {
        for (int i = 0; i < s.length; i++) {
            if (dist[i == 0 ? 0 : d[i - 1]][s[i]] == null) {
                return false;
            }
            if (dist[s[i]][d[i]] == null) {
                return false;
            }
        }
        return true;
    }

    static long constantPath(int[] S, int[] T, Long[][] dist) {
        long constant = dist[0][S[0]];
        for (int i = 1; i < S.length; i++) {
            constant += dist[T[i - 1]][S[i]];
        }
        return constant;
    }

    static Long shortestPath(int[] s, int[] t, Long[][] dist) {
        int k = s.length;
        Long min[][] = new Long[k][];
        for (int i = 0; i < k; i++) {
            min[i] = new Long[4];
        }

        LinkedList<int[]> q = new LinkedList<>();
        q.add(new int[]{0, PICK_1ST});
        min[0][PICK_1ST] = 0L;

        while (!q.isEmpty()) {
            int[] pick = q.pollFirst();
            int cust = pick[0];
            int action = pick[1];
            long distSoFar = min[cust][action];

            switch (action) {
                case PICK_1ST:
                    nextAction(cust, DROP_SINGLE, distSoFar + dist[s[cust]][t[cust]], min, q);
                    if (cust + 1 < k) {
                        nextAction(cust + 1, PICK_2ND, distSoFar + dist[s[cust]][s[cust + 1]], min, q);
                    }
                    break;
                case PICK_2ND:
                    nextAction(cust - 1, DROP_2ND, distSoFar, min, q);
                    break;
                case DROP_SINGLE:
                    if (cust + 1 < k) {
                        nextAction(cust + 1, PICK_1ST, distSoFar, min, q);
                    }
                    break;
                case DROP_2ND:
                    nextAction(cust + 1, DROP_SINGLE, distSoFar + dist[t[cust]][t[cust + 1]], min, q);
                    if (cust + 2 < k) {
                        nextAction(cust + 2, PICK_2ND, distSoFar + dist[t[cust]][s[cust + 2]], min, q);
                    }
                    break;
            }
        }

        return min[k - 1][DROP_SINGLE];
    }

    private static void nextAction(int targetCustomer, int action, long newDistance, Long[][] min, LinkedList<int[]> q) {
        if (min[targetCustomer][action] == null || min[targetCustomer][action] > newDistance) {
            min[targetCustomer][action] = newDistance;
            q.add(new int[]{targetCustomer, action});
        }
    }

    static class World {
        int n;
        ArrayList<Road>[] cities;

        World(int n) {
            this.n = n;
            this.cities = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                cities[i] = new ArrayList<>();
            }
        }

        void join(int a, int b, int cost) {
            cities[a].add(new Road(b, cost));
            cities[b].add(new Road(a, cost));
        }
    }

    static class Road {
        int target;
        int cost;

        public Road(int target, int cost) {
            this.target = target;
            this.cost = cost;
        }
    }

    @Test
    public void smallCity() {
        World w = new World(3);
        w.join(0, 1, 4);
        w.join(1, 2, 7);

        Long[][] dist = calculateDistances(w);
        System.out.println(Arrays.deepToString(dist));
    }

    @Test
    public void largeCity() throws IOException {
        Random r = new Random();
        long longest = 0;

        int t = 100;

        StringBuilder sb = new StringBuilder();
        sb.append(t).append('\n');

        for (int q_i = 0; q_i < t; q_i++) {

            int n = 100;
            int m = r.nextInt(5000) + 1;
            int k = 5000;

            sb.append(n).append(' ').append(m).append(' ').append(k).append('\n');

            Random r1 = new Random();
            World w = new World(n);
            for (int i1 = 0; i1 < m; i1++) {
                int s = r1.nextInt(n);
                int d = r1.nextInt(n);
                int g = r1.nextInt(1000) + 1;
                w.join(s, d, g);

                sb.append(s + 1).append(' ').append(d + 1).append(' ').append(g).append('\n');
            }

            int[] s = new int[k];
            int[] d = new int[k];
            for (int i = 0; i < k; i++) {
                s[i] = r.nextInt(n);
                d[i] = r.nextInt(n);
                sb.append(s[i] + 1).append(' ').append(d[i] + 1).append('\n');
            }

            long t0 = System.currentTimeMillis();
            System.out.println(solve(w, s, d));
            longest = Math.max(longest, System.currentTimeMillis() - t0);
        }
        System.out.println("Longest: " + longest + "ms");

        Files.write(Paths.get("src/hc2017r1/manic-rand.in"), sb.toString().getBytes());
    }

}
