package tailorshop;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class S {

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder("100000 1\n");
        for (int i = 0; i < 100000; i++) {
            sb.append("1 ");
        }
        Scanner in = new Scanner(sb.toString());

//        System.out.println(solve2(in));

        Assert.assertEquals(5L, solve2(new Scanner("2 3\n" +
                "4 5")));
        Assert.assertEquals(9L, solve2(new Scanner("3 2\n" +
                "4 6 6")));
        Assert.assertEquals(5000050000L, solve2(in));
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] line = br.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int p = Integer.parseInt(line[1]);
        int[] a = new int[n];
        line = br.readLine().split(" ");
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = Integer.parseInt(line[a_i]);
        }

        Arrays.sort(a);

        long sum = 0;
        int last = 0;
        for (int i : a) {
            int k = (i-1)/p+1;
            if (last >= k) {
                last++;
            } else {
                last = k;
            }
            sum += last;
        }
        System.out.println(sum);
    }

    private static long solve2(Scanner in) {
        int n = in.nextInt();
        int p = in.nextInt();
        int[] a = new int[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }

        Arrays.sort(a);

        long sum = 0;
        int last = 0;
        for (int i : a) {
            int k = (i-1)/p+1;
            if (last >= k) {
                last++;
            } else {
                last = k;
            }
            sum += last;
        }

//        TreeMap<Integer, Integer> count = new TreeMap<>();
//        for (int i : a) {
//            int c = (i - 1) / p + 1;
//            count.put(c, Optional.ofNullable(count.get(c)).orElse(0) + 1);
//        }
//
//
//        Integer[] counts = count.keySet().toArray(new Integer[0]);
//        for (int i = 0; i < counts.length; i++) {
//            int c = count.get(counts[i]);
//            int space = (i == (counts.length - 1)) ? Integer.MAX_VALUE : (counts[i + 1] - counts[i]);
//            if (space >= c) {
//                sum += (long) (counts[i] + (counts[i] + c - 1)) * c / 2;
//            } else {
//                sum += (long) (counts[i] + (counts[i+1] - 1)) * space / 2;
//                count.put(counts[i+1], count.get(counts[i+1]) + c - space);
//            }
//        }

        return sum;

    }

    private static long solve(Scanner in) {
        int n = in.nextInt();
        int p = in.nextInt();
        int[] a = new int[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }

        boolean[] used = new boolean[100001];

        long sum = 0;

        for (int i : a) {
            int c = (i - 1) / p + 1;
            while (used[c]) c++;
            used[c] = true;
            sum += c;
        }

        return sum;
    }
}
