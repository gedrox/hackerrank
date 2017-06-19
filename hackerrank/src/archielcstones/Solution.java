package archielcstones;

import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        long n = scan.nextLong();
        int m = scan.nextInt();
        long[] s = new long[m];
        for (int i = 0; i < m; i++) {
            s[i] = scan.nextLong();
        }

        if (canWin(n, s, 1)) {
            System.out.println("First");
        } else {
            System.out.println("Second");
        }
    }


    private static boolean canWin(Long pileSize, long[] s, int depth) {
        // System.out.println(depth + " :: " + pileSize);
        boolean myTurn = depth % 2 == 1;
        boolean canWin = myTurn ? false : true;

        for (long div : s) {
            if (pileSize % div != 0) {
                continue;
            }
            long newPileSize = pileSize / div;
            boolean even = div % 2 == 0;

            boolean hasSteps = hasSteps(newPileSize, s);
            boolean canWinThis = canWin(newPileSize, s, depth + 1);
            if (even && hasSteps) {
                canWinThis = !canWinThis;
            }

            if (myTurn && canWinThis) {
                canWin = true;
                break;
            } else if (!myTurn && !canWinThis ){
                canWin = false;
                break;
            }
        }

        return canWin;
    }

    private static boolean hasSteps(Long pileSize, long[] s) {
        for (long div : s) {
            if (pileSize % div == 0) {
                return true;
            }
        }
        return false;
    }
}
