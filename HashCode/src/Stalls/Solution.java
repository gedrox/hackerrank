package Stalls;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Solution {
    public Gap solve(long N, long K) {
        PriorityQueue<Gap> gaps = new PriorityQueue<>(Comparator.comparing(g -> -g.size));
        gaps.add(new Gap(N));

        HashMap<Long, Gap> bySize = new HashMap<>();
        Gap lastGap = null;

        while (K > 0) {
            Gap top = lastGap = gaps.poll();
            K -= top.count;
            long a = (top.size - 1) / 2;
            long b = top.size / 2;

            if (!bySize.containsKey(a)) {
                Gap gap = new Gap(a, top.count);
                bySize.put(a, gap);
                gaps.add(gap);
            } else {
                bySize.get(a).incr(top.count);
            }
            if (!bySize.containsKey(b)) {
                Gap gap = new Gap(b, top.count);
                bySize.put(b, gap);
                gaps.add(gap);
            } else {
                bySize.get(b).incr(top.count);
            }
        }

        return lastGap;
    }

    @Test
    public void test() {
        long[][] tests = {
                {4, 2, 1, 0},
                {5, 2, 1, 0},
                {6, 2, 1, 1},
                {1000, 1000, 0, 0},
                {1000, 1, 500, 499},
                {1_000_000_000_000_000_000L, 1_000_000_000_000_000_000L, 0, 0}
        };

        for (long[] tcase : tests) {
            Gap answer = solve(tcase[0], tcase[1]);
            Assert.assertEquals(tcase[2], answer.size / 2);
            Assert.assertEquals(tcase[3], (answer.size - 1) / 2);
        }
    }

    static class Gap {
        long size;
        long count = 1;

        public Gap(long size) {
            this.size = size;
        }

        public Gap(long size, long count) {
            this.size = size;
            this.count = count;
        }

        public void incr(long count) {
            this.count += count;
        }
    }
}
