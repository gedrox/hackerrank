package hackerrank;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Airports2 {

    static int n;
    static int d;
    static int[] x;
    static int index = 0;
    static int minIndex;
    static int maxIndex;
    static TreeSet<Gap> gaps;
    static PriorityQueue<Gap> topGaps;
    static int oldVoidA;
    static int oldVoidB;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine());
        StringBuilder sb = new StringBuilder();
        for (int t_i = 0; t_i < t; t_i++) {
            String[] split = in.readLine().split(" ");
            n = Integer.parseInt(split[0]);
            d = Integer.parseInt(split[1]);
            x = new int[n];
            split = in.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                x[i] = Integer.parseInt(split[i]);
            }
            prepare();
            for (int i = 0; i < n; i++) {
                int res = solveNext();
                sb.append(res).append(' ');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }

    static void prepare() {
        minIndex = 0;
        maxIndex = 0;
        n = x.length;
        index = 0;
        gaps = new TreeSet<>(Comparator.comparing(Gap::getStart));
        topGaps = new PriorityQueue<>(Comparator.comparing(Gap::getSize).reversed());
        Gap rootGap = new Gap(-1000000000, 2000000000);
        gaps.add(rootGap);
        topGaps.add(rootGap);
        oldVoidA = -1000000000;
        oldVoidB = 1000000000;
    }

    static int solveNext() {
        int newX = x[index++];

        if (index == 1) {
            return 0;
        }

        Integer newSplitPoint = null;
        if (newX < x[minIndex]) {
            if (minIndex != maxIndex) {
                newSplitPoint = x[minIndex];
            }
            minIndex = index - 1;
        } else if (newX > x[maxIndex]) {
            if (minIndex != maxIndex) {
                newSplitPoint = x[maxIndex];
            }
            maxIndex = index - 1;
        } else {
            newSplitPoint = newX;
        }

        if (x[maxIndex] - x[minIndex] >= 2 * d - 1) {
            return 0;
        }

        // need to find largest gap inside void
        // both points including
        int voidA = x[maxIndex] - d + 1;
        int voidB = x[minIndex] + d - 1;

        // cut in front
        if (voidA > oldVoidA) {
            for (Iterator<Gap> iterator = gaps.iterator(); iterator.hasNext(); ) {
                Gap gap = iterator.next();
                if (voidA <= gap.start) break;
                gap.outdated = true;
                iterator.remove();
                if (voidA < gap.start + gap.size) {
                    Gap newGap = new Gap(voidA, gap.size - (voidA - gap.start));
                    gaps.add(newGap);
                    topGaps.add(newGap);
                    break;
                }
            }
            oldVoidA = voidA;
        }

        if (voidB < oldVoidB) {
            for (Iterator<Gap> iterator = gaps.descendingIterator(); iterator.hasNext(); ) {
                Gap gap = iterator.next();
                if (voidB >= gap.end) break;
                gap.outdated = true;
                iterator.remove();
                if (voidB >= gap.start) {
                    Gap newGap = new Gap(gap.start, voidB - gap.start + 1);
                    gaps.add(newGap);
                    topGaps.add(newGap);
                    break;
                }
            }
            oldVoidB = voidB;
        }

        // split in point
        if (newSplitPoint != null && newSplitPoint >= voidA && newSplitPoint <= voidB) {
            Gap search = new Gap(newSplitPoint, 0);
            Gap floor = gaps.floor(search);
            if (floor != null) {
                if (newSplitPoint <= floor.end) {
                    floor.outdated = true;
                    gaps.remove(floor);
                    Collection<Gap> split = floor.split(newSplitPoint);
                    gaps.addAll(split);
                    topGaps.addAll(split);
                }
            }
        }

        // special case
        if (index == 2) {
            return Math.max(0, d - (x[maxIndex] - x[minIndex]));
        }

        while (!topGaps.isEmpty() && topGaps.peek().outdated) {
            topGaps.poll();
        }

        return voidB - voidA + 1 - (topGaps.isEmpty() ? 0 : topGaps.peek().size);
    }

    @Test
    public void test1() {
        d = 1;
        x = new int[]{0, 0, 0};
        testSolve(0, 1, 1);
    }

    @Test
    public void test2() {
        d = 4;
        x = new int[]{1, -1, 2, -1, 1};
        testSolve(0, 2, 2, 3, 3);
    }

    @Test
    public void test3() {
        d = 4;
        x = new int[]{0, 6, 3, 2};
        testSolve(0, 0, 1, 1);
    }

    @Test
    public void test4() {
        d = 4;
        x = new int[]{0, 5, 3, 2};
        testSolve(0, 0, 1, 2);
    }

    @Test
    public void test5() {
        d = 4;
        x = new int[]{0, 4, 0, 4, 2};
        testSolve(0, 0, 0, 0, 2);
    }

    @Test
    public void test6() {
        d = 4;
        x = new int[]{0, 3, 0, 3};
        testSolve(0, 1, 1, 2);
    }

    @Test
    public void test7() {
        d = 100;
        x = new int[]{0, 100, 20, 30, 40, 50, 90, 80, 70, 110, 120};
        testSolve();
    }

    private void testSolve(int... expect) {
        prepare();
        for (int i = 0; i < n; i++) {
            int res = solveNext();
            System.out.println(res);
            if (expect.length > 0) Assert.assertEquals(expect[i], res);
        }
    }

    static class Gap {
        int start;
        int size;
        int end;
        boolean outdated = false;

        Gap(int start, int size) {
            this.start = start;
            this.size = size;
            this.end = start + size - 1;
        }

        Collection<Gap> split(int cut) {
            assert cut >= start;
            assert cut < start + size;
            if (size == 1) {
                return Collections.emptyList();
            }
            if (cut == start) {
                return Collections.singleton(new Gap(start + 1, size - 1));
            }
            if (cut == start + size - 1) {
                return Collections.singleton(new Gap(start, size - 1));
            }
            ArrayList<Gap> split = new ArrayList<>(2);
            split.add(new Gap(start, cut - start));
            split.add(new Gap(cut + 1, size - (cut - start) - 1));
            return split;
        }

        public int getStart() {
            return start;
        }

        public int getSize() {
            return size;
        }
    }
}
