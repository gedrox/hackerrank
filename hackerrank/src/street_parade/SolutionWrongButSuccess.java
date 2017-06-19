package street_parade;

import java.util.*;

public class SolutionWrongButSuccess {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        int miles = sc.nextInt();
        int min = sc.nextInt();
        int max = sc.nextInt();

        assert miles >= min;

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

        int[] diffs = {max, min, 0};

        nextInterval:
        for (int i = 0; i < n; i++) {
            for (int diff : diffs) {
                int start = a[i] - diff;
                int res = check(start, miles, a, min, max, badShort, badLong);
                if (res == 0) {
                    System.out.println(start);
                    return;
                }
                if (res == -1) {
                    continue nextInterval;
                }
                // minus one because will ++ before next iteration
                i = Math.max(res - 1, i);
            }
        }
    }

    private static int check(int start, int miles, int[] a, int min, int max, ArrayList<Integer> badShort, ArrayList<Integer> badLong) {
        int startI = Arrays.binarySearch(a, start);
        if (startI < 0) startI = -startI - 1; else startI++;

        int endI = Arrays.binarySearch(a, start+miles);
        if (endI < 0) endI = -endI - 1;

        assert startI < endI;

        if (a[startI] - start < min) return -1;
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