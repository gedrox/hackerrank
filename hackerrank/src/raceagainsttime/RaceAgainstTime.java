package raceagainsttime;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class RaceAgainstTime {
    static long raceAgainstTime(int n, long[] heights, long[] prices) {

        long[] barriers = new long[100_001];
        long[] cena = new long[100_001];
        barriers[0] = heights[0];
        int len = 1;

        for (int i = 1; i < n; i++) {

            long bestLabi = Long.MAX_VALUE;

            int newPos = len;

            // check the best barrier to skip
            for (int j = len - 1; j >= 0 && barriers[j] < heights[i]; j--) {
                newPos = j;
                long newBest = cena[j] + prices[i] + Math.abs(heights[i] - barriers[j]);
                if (newBest < bestLabi) {
                    bestLabi = newBest;
                }
            }

            boolean addPoint;
            long addCost = bestLabi;

            if (newPos == 0) {
                addPoint = true;
            } else {
                // prefer going through, skipping all lower points?
                if (prices[i] < 0) {
                    long directCost = cena[newPos - 1] + prices[i] + Math.abs(heights[i] - barriers[newPos - 1]);
                    addCost = Math.min(directCost, bestLabi);
                }

                // anyway, is it worth? maybe let's skip entirely?
                long skipCost = cena[newPos - 1];
                if (skipCost + Math.abs(barriers[newPos - 1] - heights[i]) <= addCost) {
                    addPoint = false;
                } else {
                    addPoint = true;
                }
            }

            if (addPoint) {
                len = newPos + 1;
                barriers[newPos] = heights[i];
                cena[newPos] = addCost;
            } else {
                len = newPos;
            }
        }

        long minCost = Long.MAX_VALUE;
        for (int i = 0; i < len; i++) {
            if (cena[i] < minCost) minCost = cena[i];
        }

        return minCost + n;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bi.readLine());
        long[] heights = new long[n];
        heights[0] = Integer.parseInt(bi.readLine());
        int ch = 0;
        for (int heights_i = 0; heights_i < n - 1; heights_i++) {
            while ((ch < '0' || ch > '9') && ch != '-') ch = bi.read();
            while (ch >= '0' && ch <= '9') {
                heights[heights_i + 1] *= 10;
                heights[heights_i + 1] += ch - '0';
                ch = bi.read();
            }
        }
        long[] prices = new long[n];
        for (int prices_i = 0; prices_i < n - 1; prices_i++) {
            int sign = 1;
            while ((ch < '0' || ch > '9') && ch != '-') ch = bi.read();
            if (ch == '-') {
                sign = -1;
                ch = bi.read();
            }
            while (ch >= '0' && ch <= '9') {
                prices[prices_i + 1] *= 10;
                prices[prices_i + 1] += ch - '0';
                ch = bi.read();
            }
            prices[prices_i + 1] *= sign;
        }
        long result = raceAgainstTime(n, heights, prices);
        System.out.println(result);
    }

    @Test
    public void test() {
        int n = 100_000;
        long[] heights = new long[n];
        long[] costs = new long[n];
        Random r = new Random();

        int minCost = -1_000_000_000;
        int maxCost = 1_000_000_000;

        int minH = 90;
        int maxH = 100;

        minCost = -50;
        maxCost = 100;

        for (int i = 0; i < n; i++) {
            heights[i] = r.nextInt(maxH - minH + 1) + minH;
//            heights[i] = (n + i) % 10000;
            costs[i] = r.nextInt(maxCost - minCost + 1) + minCost;
//            costs[i] = -1;
        }

        long res = raceAgainstTime(n, heights, costs);
        System.out.println(res);
    }
}
