package hackerrank;

import java.util.Arrays;

public class TestShuffle {
    public static void main(String[] args) {
        int[][] stat = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    int[] y = {0, 1, 2};
                    swap(y, 0, i);
                    swap(y, 1, j);
                    swap(y, 2, k);

                    stat[0][y[0]]++;
                    stat[1][y[1]]++;
                    stat[2][y[2]]++;
                }
            }
        }

        System.out.println(Arrays.deepToString(stat));
    }

    private static void swap(int[] y, int i, int k) {
        int tmp = y[i];
        y[i] = y[k];
        y[k] = tmp;
    }
}
