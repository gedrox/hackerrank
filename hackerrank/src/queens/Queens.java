package queens;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Queens {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        int rQueen = in.nextInt();
        int cQueen = in.nextInt();

        int[] nearest = new int[8];
        nearest[0] = n - rQueen;
        nearest[2] = n - cQueen;
        nearest[4] = rQueen - 1;
        nearest[6] = cQueen - 1;

        nearest[1] = Math.min(nearest[0], nearest[2]);
        nearest[3] = Math.min(nearest[2], nearest[4]);
        nearest[5] = Math.min(nearest[4], nearest[6]);
        nearest[7] = Math.min(nearest[6], nearest[0]);

        for(int a0 = 0; a0 < k; a0++) {
            int rObstacle = in.nextInt();
            int cObstacle = in.nextInt();
            if (rObstacle == rQueen) {
                if (cObstacle > cQueen) {
                    nearest[2] = Math.min(nearest[2], cObstacle - cQueen - 1);
                } else {
                    nearest[6] = Math.min(nearest[6], cQueen - cObstacle - 1);
                }
            }

            if (cObstacle == cQueen) {
                if (rObstacle > rQueen) {
                    nearest[0] = Math.min(nearest[0], rObstacle - rQueen - 1);
                } else {
                    nearest[4] = Math.min(nearest[4], rQueen - rObstacle - 1);
                }
            }

            if (cObstacle - cQueen == rObstacle - rQueen) {
                if (cObstacle > cQueen) {
                    nearest[1] = Math.min(nearest[1], cObstacle - cQueen - 1);
                } else {
                    nearest[5] = Math.min(nearest[5], cQueen - cObstacle - 1);
                }
            }

            if (cObstacle - cQueen == rQueen - rObstacle) {
                if (cObstacle > cQueen) {
                    nearest[3] = Math.min(nearest[3], cObstacle - cQueen - 1);
                } else {
                    nearest[7] = Math.min(nearest[7], cQueen - cObstacle - 1);
                }
            }
        }

        System.out.println(IntStream.of(nearest).sum());
    }
}
