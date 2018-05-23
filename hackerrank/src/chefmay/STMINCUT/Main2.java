package chefmay.STMINCUT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main2 {
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int n;
    private static int[][] a;

    static int readInt() throws IOException {
        return (int) readLong();
    }

    static long readLong() throws IOException {
        long res = 0;
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
        int t = readInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t; i++) {
            readInput();
            long ans = solve();
            sb.append(ans).append('\n');
        }
        System.out.print(sb);
    }

    static int[] colors;

    static int getCol(int i) {
        if (i == colors[i]) {
            return i;
        }
        return (colors[i] = getCol(colors[i]));
    }

    static void mergeCol(int i, int j) {
        colors[getCol(i)] = getCol(j);
    }

    private static long solve() {
        int[][] aCop = new int[n][];
        for (int i = 0; i < n; i++) aCop[i] = a[i].clone();
        return maybeFaster(aCop);
    }

    private static long maybeFaster(int[][] aCop) {
        long res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (aCop[i][j] != aCop[j][i]) {
                    res += Math.max(aCop[i][j], aCop[j][i]) - Math.min(aCop[i][j], aCop[j][i]);
                    aCop[i][j] = aCop[j][i] = Math.max(aCop[i][j], aCop[j][i]);
                }
            }
        }

        // split into parts
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (aCop[i][j] > 0) {
                    mergeCol(i, j);
                }
            }
        }

        boolean[] used = new boolean[n];

        for (int i = 0; i < n; ) {
            if (used[i]) continue;

            int col = getCol(i);
            ArrayList<Integer> byCol = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (getCol(j) == col) {
                    byCol.add(j);
                }
            }

            if (byCol.size() == 1) {
                used[i] = true;
                i++;
                continue;
            }

            // how much to deduct to split?
            Collection<Integer> vals = new TreeSet<>();
            int[][] aOrig = new int[byCol.size()][byCol.size()];
            for (int x = 0; x < byCol.size(); x++) {
                for (int y = x + 1; y < byCol.size(); y++) {
                    int x1 = byCol.get(x);
                    int y1 = byCol.get(y);
                    aOrig[x][y] = aCop[x1][y1];
                    if (aCop[x1][y1] > 0) {
                        vals.add(aCop[x1][y1]);
                    }
                }
            }

            ArrayList<Integer> vals2 = new ArrayList<>(vals);
            assert vals2.size() > 0;

            int low = 0, high = vals.size() - 1;
            while (low < high) {
                int mid = (low + high) / 2;

                deduct(aCop, byCol, aOrig, vals2, mid);

                boolean hasSplit = false;
                int firstCol = getCol(byCol.get(0));
                for (int i1 : byCol) {
                    if (getCol(i1) != firstCol) {
                        hasSplit = true;
                        break;
                    }
                }

                if (hasSplit) {
                    high = mid;
                } else {
                    low = mid + 1;
                }
            }

            deduct(aCop, byCol, aOrig, vals2, low);
        }


        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                assert aCop[i][j] <= 0;
                res -= 2 * aCop[i][j];
            }
        }
        return res;
    }

    private static void deduct(int[][] aCop, ArrayList<Integer> byCol, int[][] aOrig, ArrayList<Integer> vals2, int low) {
        for (int i1 : byCol) {
            colors[i1] = i1;
        }
        for (int x = 0; x < byCol.size(); x++) {
            for (int y = x + 1; y < byCol.size(); y++) {
                int x1 = byCol.get(x);
                int y1 = byCol.get(y);
                aCop[x1][y1] = aOrig[x][y] - vals2.get(low);
                if (aCop[x1][y1] > 0) {
                    mergeCol(x1, y1);
                }
            }
        }
    }

    private static void readInput() throws IOException {
        n = readInt();
        init();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = readInt();
            }
        }
    }

    private static void init() {
        a = new int[n][n];
        colors = new int[n];
        for (int i = 0; i < n; i++) {
            colors[i] = i;
        }
    }
}
