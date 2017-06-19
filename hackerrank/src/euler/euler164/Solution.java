package euler.euler164;

import org.junit.Test;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
//        for (int i = 3; i <= 1000; i++) {
//            System.out.printf("%d - %d%n", i, getSum(i));
//        }

        int n = new Scanner(System.in).nextInt();
        System.out.println(getSum(n));
    }

    private static int getSum(int n) {
        int[] times = new int[100];
        for (int i = 100; i < 1000; i++) {
            int sum = (i % 10) + ((i / 10) % 10) + (i / 100);
            if (sum < 10) {
                times[i % 100]++;
            }
        }

        for (int i = 3; i < n; i++) {
            int[] newTimes = new int[100];

            for (int last2 = 0; last2 < times.length; last2++) {
                if (times[last2] > 0) {
                    int f = last2 / 10;
                    int s = last2 % 10;
                    for (int t = 0; t < 10 - f - s; t++) {
                        newTimes[s * 10 + t] = (int) (((long) newTimes[s * 10 + t] + times[last2]) % 1000000007);
                    }
                }
            }

            times = newTimes;
        }

        long sum = 0;
        for (int time : times) {
            sum += time;
        }
        return (int) (sum % 1000000007);
    }

    @Test
    public void test() {

    }
}
