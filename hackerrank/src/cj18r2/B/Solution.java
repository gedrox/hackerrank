package cj18r2.B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Solution {

    private static final boolean ENABLE_ANY_GOOD = true;
    static int ch = 0;
    public static final int SIZE = 501;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int R, B;
    static int remR, remB;
    static int count = 0;
    static int maxCount = 0;
    static int MAX = 33;
    static int[] a = new int[MAX];

    static int[] REG = new int[MAX];
    static int[] REG_JUG = new int[MAX];
    static int[] bestA;

    static void prep() {
        for (int i = 0; i < MAX; i++) {

        }
    }

    static int readInt() throws IOException {
        return (int) readLong();
    }

    static long readLong() throws IOException {
        long res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        int T = readInt();

        for (int t_i = 1; t_i <= T; t_i++) {
            readInput();
            int answer = solve2();
            sb.append("Case #").append(t_i).append(": ").append(answer).append('\n');
        }

        System.out.print(sb.toString());
    }

    static void readInput() throws IOException {
        R = readInt();
        B = readInt();
    }

    static int solve() {
        remR = R;
        remB = B;

        a[0] = 1;
        count = 1;

        for (int i = 1; i < MAX; i++) {
            int each = i * (i + 1) / 2;
            if (Math.min(remR, remB) >= each) {
                count += i + 1;
                remR -= each;
                remB -= each;

                for (int j = 0; j <= i; j++) {
                    a[j]++;
                }

            } else {
                break;
            }
        }

        maxCount = count;

        for (int i = 0; i < MAX; i++) {
            if (i == 0 || a[i] < a[i - 1]) {
                rec(i);
            }
        }

        return maxCount - 1;
    }

    static void rec(int addTo) {

        a[addTo]++;

        remB -= addTo;
        remR -= a[addTo] - 1;
        count++;

        if (remB >= 0 && remR >= 0) {
            if (maxCount < count) {
                maxCount = count;
                bestA = a.clone();
            }

            if (remB > 0 || remR > 0) {
                for (int i = 0; i < MAX; i++) {
                    if (i == 0 || a[i] < a[i - 1]) {
                        rec(i);
                    }
                }
            }
        }

        count--;
        remB += addTo;
        remR += a[addTo] - 1;

        a[addTo]--;
    }

    static int solve2() {
//        HashMap<Rem, Integer> rmd = new HashMap<>();
//        rmd.put(new Rem(R, B), 0);

        int[] rmd = getInts();
        rmd[SIZE * R + B] = 0;

        int takeB, takeR;
        
        int max = 0;
        
        for (int i = 0; i < MAX; i++) {
            
            takeR = i;
            takeB = 0;

//            HashMap<Rem, Integer> rmd2 = new HashMap<>();
            int[] rmd2 = getInts();

            for (int j = 1; (i + 1) * j * (i + j - 1) / 2 <= R + B; j++) {
//            for (int j = 1; j <= MAX; j++) {

//                boolean anyGood = !ENABLE_ANY_GOOD;
                
//                for (Rem rem : rmd.keySet()) 
                for (int r = 0; r < SIZE * SIZE; r++) 
                {
                    if (rmd[r] < 0) continue;
                    
                    
                    int remr = r / SIZE;
                    int remb = r % SIZE;
                    
//                    if (r == 501*501-1) {
//                        System.out.println(1);
//                    }
                    
                    if (remr >= takeR && remb >= takeB) {
//                        anyGood = true;
//                        Rem x = new Rem(remr - takeR, remb - takeB);
                        int x = SIZE * (remr - takeR) + (remb - takeB);
//                        int curr = 0;
//                        if (rmd2.containsKey(x)) {
//                            curr = rmd2.get(x);
//                        }
//
//                        int newCount = rmd.get(rem) + j;
//                        if (newCount > curr) {
//                            rmd2.put(x, newCount);
//                            max = Math.max(max, newCount);
//                        }
                        
                        if (rmd[r] + j > rmd2[x]) {
                            rmd2[x] = rmd[r] + j;
                            max = Math.max(max, rmd2[x]);
                        }
                    }
                }

//                if (!anyGood) {
//                    break;
//                }
                
                takeR += i;
                takeB += j;
            }
            
            rmd = rmd2;
//            System.err.println(Arrays.toString(rmd));
        }

        return max - 1;
    }

    private static int[] getInts() {
        int[] rmd = new int[SIZE * SIZE];
        for (int i = 0; i < rmd.length; i++) {
            rmd[i] = Integer.MIN_VALUE;
        }
        return rmd;
    }

    static class Rem {
        int r, b;

        public Rem(int r, int b) {
            this.r = r;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            Rem rem = (Rem) o;
            return b == rem.b && r == rem.r;
        }

        @Override
        public int hashCode() {
            return Objects.hash(b, r);
        }
    }

    static int solve3() {
        int RB = R + B;
        int answer = 0;
        int i = 1;
        while (RB >= i * (i + 1)) {
            RB -= i * (i + 1);
            i++;
            answer += i;
        }
        answer += RB / i;
        return answer;
    }

}
