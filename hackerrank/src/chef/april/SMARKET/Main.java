package chef.april.SMARKET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int n = 8;
        int[] A = new int[n];
        Random r = new Random(0);

        for (int i = 0; i < n; i++) {
            A[i] = r.nextInt(10);
        }

        A = new int[] {20 ,10 ,10 ,10, 7, 7, 7, 10};

        int[][] a = new int[1000][];

        ArrayList<Integer> start = new ArrayList<>();
        int[] orders = new int[100000];

        int last = 0;
        int order = 0;
        for (int i = 0; i < A.length; i++) {

            if (i == 0) {
                a[i / 100] = new int[100001];
            } else if (i % 100 == 0) {
                a[i / 100] = a[i / 100 - 1].clone();
            }

            if (A[i] != last) {
                order = 0;
                last = A[i];
                start.add(i);
            }
            order++;

            orders[i] = order;
            a[i / 100][order]++;
        }
        start.add(n);

        int L = 3;
        int R = 6;
        int lvl = 3;

        L--;
        R--;

        // 1. calculate [-----R--| result
        // 2. deduct --| part by checking starts after R before |, special check the start right before R (or at R)
        // 3. deduct [--|--L first part [--|

        int R1 = R / 100;
        int res = R1 >= 0 ? a[R1][lvl] : 0;

        int pos = Collections.binarySearch(start, R);
        if (pos < 0) {
            pos = -pos - 2;
        }

        if (R - start.get(pos) + 1 < lvl && start.get(pos + 1) - start.get(pos) >= lvl) {
            res--;
        }

        for (int p = pos + 1; p < start.size() - 1 && start.get(p)/100*100 <= R1; p++) {
            if (Math.min(start.get(p + 1), (R + 1) * 100) - start.get(p) >= lvl) {
                res--;
            }
        }

        int L1 = L / 100 - 1;
        if (L1 >= 0) {
            res -= a[L1][lvl];
        }

        int posL = Collections.binarySearch(start, 100 * (L1 + 1));
        if (posL < 0) {
            posL = - posL - 2;
        }

        if (100 * (L1 + 1) - start.get(posL) < lvl && start.get(posL + 1) - start.get(posL) >= lvl) {
            if (start.get(posL + 1) - L < lvl) {
                res--;
            }
        }

        for (int p = posL + 1; p < start.size() - 1 && start.get(p) < L; p++) {

//            if (Math.min(start.get(p + 1), ))

//            if (L - start.get(p) < lvl)

//            if (Math.min(start.get(p + 1), 100 * (L1 + 1)) - start.get(p) >= lvl) {
//                if (start.get(p + 1) - L < lvl) {
//                    res--;
//                }
//            }
        }

        System.out.println(res);
    }
}
