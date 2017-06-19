package street_parade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bi.readLine());

        long t = System.currentTimeMillis();

        String[] astr = bi.readLine().split(" ");
        int a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(astr[i]);
        }

        System.err.println(((System.currentTimeMillis() - t) * 0.001));

        astr = bi.readLine().split(" ");
        int miles =Integer.parseInt(astr[0]);
        int min = Integer.parseInt(astr[1]);
        int max = Integer.parseInt(astr[2]);

//        assert miles >= min;

        if (miles <= max) {
            System.out.println(a[0] - max - 1);
            return;
        }

        ArrayList<Integer> badShort = new ArrayList<>();
        ArrayList<Integer> badLong = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            if (a[i] - a[i-1] < min) badShort.add(i);
            if (a[i] - a[i-1] > max) badLong.add(i);
        }

        System.err.println(((System.currentTimeMillis() - t) * 0.001));

        int[] diffs = {max, min, 0};

        ArrayList<Integer> startsAll = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int diff : diffs) {
                startsAll.add(a[i] - diff);
                startsAll.add(a[i] + diff - miles);
            }
        }

        System.err.println(((System.currentTimeMillis() - t) * 0.001));

        Collections.sort(startsAll);
        Integer[] starts = startsAll.toArray(new Integer[0]);

        System.err.println(((System.currentTimeMillis() - t) * 0.001));

//        int Zero = Arrays.binarySearch(starts, a[0] - max);
//        if (Zero < 0) Zero = -Zero-1;
//
//        System.err.println(((System.currentTimeMillis() - t) * 0.001) + " start at " + Zero);

//        nextInterval:
        for (int i = 0; i < starts.length; i++) {
            int start = starts[i];
            int res = check(start, miles, a, min, max, badShort, badLong);
            if (res == 0) {
                System.out.println(start);

                System.err.println((System.currentTimeMillis() - t) * 0.001);

                return;
            }
            if (res == -1) {
                continue;
            }

            int smallestNext = a[res - 1];
            int nextPos = Arrays.binarySearch(starts, smallestNext);
            if (nextPos < 0) {
                nextPos = -nextPos - 1;
            }
            if (nextPos - 1 > i) {
                i = nextPos - 1;
                System.err.println(((System.currentTimeMillis() - t) * 0.001) + " Jump to " + i);
            }
//
//            // minus one because will ++ before next iteration
//            if (res - 1 > i) {
//
//                i = res - 1;
//                continue nextInterval;
//            }
        }
    }

    private static int check(int start, int miles, int[] a, int min, int max, ArrayList<Integer> badShort, ArrayList<Integer> badLong) {
//        System.err.println("Check " + start);
        int startI = Arrays.binarySearch(a, start);
        if (startI < 0) startI = -startI - 1; else startI++;

        int endI = Arrays.binarySearch(a, start+miles);
        if (endI < 0) endI = -endI - 1;

//        assert startI < endI;

        if (startI >= a.length) return -1;
        if (endI == 0) return -1;

        if (a[startI] - start < min) return -1;
        if (a[startI] - start > max) return -1;
        if (start + miles - a[endI-1] < min) return -1;
        if (start + miles - a[endI-1] > max) return -1;

        if (endI - startI == 1) return 0;

        int badShortStart = Collections.binarySearch(badShort, startI + 1);
        int badShortEnd = Collections.binarySearch(badShort, endI - 1);
        if (badShortEnd >= 0) {
            // continue with the next after "bad" interval
            return badShort.get(badShortEnd) + 1;
        }
        if (badShortStart != badShortEnd) {
            return badShort.get(-badShortEnd - 2) + 1;
        }

        int badLongStart = Collections.binarySearch(badLong, startI + 1);
        int badLongEnd = Collections.binarySearch(badLong, endI - 1);
        if (badLongEnd >= 0) {
            // continue with the last "bad" interval
            return badLong.get(badLongEnd);
        }
        if (badLongStart != badLongEnd) {
            return badLong.get(-badLongEnd - 2) + 1;
        }

        return 0;
    }
}
