package arraypairs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        sc.nextLine();
        String[] split = sc.nextLine().split(" ");
        
        int[] a = new int[n];
        ArrayList<Position> pos = new ArrayList<>(n);

        long res = 0;
        int j = 0;

        for (int i = 0; i < n; i++) {
            int val = Integer.parseInt(split[i]);
            if (val == 1) {
                res += j;
                res += n - i - 1;
            } else {
                a[j] = val;
                pos.add(new Position(j, val));
                j++;
            }
        }

        n = j;

        Collections.sort(pos);

        int[] topDown = new int[n];

        for (int i = 0; i < pos.size(); i++) {
            Position p = pos.get(i);
            topDown[n - 1 - i] = p.i;
        }
        
        res += split(a, topDown, 0, n-1);
        

//        for (int i = 0; i < n; i++) {
//            int max = a[i];
//            for (int j = i+1; j < n; j++) {
//                if (a[j] > max) {
//                    max = a[j];
//                }
//                if (((long) a[i]) * a[j] <= max) {
//                    res++;
//                }
//            }
//        }
        System.out.println(res);
    }

    private static long split(int[] a, int[] topDown, int min, int max) {
        if (max - min <= 1) return 0;
//        while (topDown[splitIndex] < min || topDown[splitIndex] > max) {
//            splitIndex++;
//        }
        int splitPos = topDown[0];

        long res = 0;

        int leftSize = splitPos - min;
        int rightSize = max - splitPos;
        int[] topDownLeft = new int[leftSize];
        int[] topDownRight = new int[rightSize];
        int leftI = leftSize - 1;
        int rightI = rightSize - 1;

        int[] left = new int[leftSize];
        int[] right = new int[rightSize];

        for (int i = topDown.length - 1; i >= 1; i--) {
            int pos = topDown[i];
            if (pos >= min && pos <= max) {
                if (pos < splitPos) {
                    left[leftSize - 1 - leftI] = a[pos];
                    topDownLeft[leftI--] = pos;
                } else {
                    right[rightSize - 1 - rightI] = 2*a[pos];
                    topDownRight[rightI--] = pos;
                }
            }
        }

        if (splitPos != min && splitPos != max) {

            long midProduct = ((long) left[leftSize / 2]) * right[rightSize / 2] / 2;
            if (midProduct < a[splitPos]) {
//                System.err.println("inverse");
                // inverse
                res += ((long) leftSize) * rightSize;
                int prevL = 0;
                int prevRes = 0;
                for (int i = leftSize - 1; i >= 0; i--) {
                    Integer L = left[i];

                    // simple cache
                    if (L == prevL) {
                        res -= prevRes;
                        continue;
                    }

                    prevL = L;
                    prevRes = Arrays.binarySearch(right, 2*(a[splitPos] / L + 1)-1);
                    if (prevRes < 0) prevRes = -prevRes - 1;
                    prevRes = rightSize - prevRes;

//                    for (Integer R : right) {
//                        if ((long) L * R > a[splitPos]) {
//                            prevRes++;
//                        } else {
//                            break;
//                        }
//                    }
                    if (prevRes == 0) break;
                    res -= prevRes;
                }
            } else {
//                System.err.println("direct");
                // direct
                int prevL = 0;
                int prevRes = 0;
                for (int i = 0; i < leftSize; i++) {
                    Integer L = left[i];

                    // simple cache
                    if (L == prevL) {
                        res += prevRes;
                        continue;
                    }

                    prevRes = Arrays.binarySearch(right, 2*(a[splitPos] / L + 1)-1);
                    if (prevRes < 0) prevRes = -prevRes - 1;
                    prevL = L;
//                    for (int i1 = right.size() - 1; i1 >= 0; i1--) {
//                        Integer R = right.get(i1);
//                        if ((long) L * R <= a[splitPos]) {
//                            prevRes++;
//                        } else {
//                            break;
//                        }
//                    }
                    if (prevRes == 0) break;
                    res += prevRes;
                }
            }
        }

//        System.err.printf("running for %d-%d and %d-%d%n", min, splitPos - 1, splitPos + 1, max);
        res += split(a, topDownLeft, min, splitPos - 1);
        res += split(a, topDownRight, splitPos + 1, max);

//        System.err.printf("min=%d max=%d res=%d%n", min, max, res);

        return res;
    }

    static class Position implements Comparable<Position> {
        int i;
        int v;
        double random = Math.random();

        public Position(int i, int v) {
            this.i = i;
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            return i == position.i && v == position.v;

        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + v;
            return result;
        }

        @Override
        public int compareTo(Position o) {
            if (this.v != o.v) return this.v - o.v;
//            return this.i - o.i;
            return Double.compare(this.random, o.random);
        }
    }
}
