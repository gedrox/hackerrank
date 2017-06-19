import org.junit.Test;

import java.util.*;

public class ModulQuery {

    public static final int MAX = 40000;

    public static final boolean VALIDATE = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print(solve(in));
    }

    private static String solve(Scanner in) {
        long t0 = System.currentTimeMillis();
        int n = in.nextInt();
        int q = in.nextInt();
        int[] a = new int[n];
        for (int a_i = 0; a_i < n; a_i++) {
            a[a_i] = in.nextInt();
        }

        System.err.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();

        int SMALL = 1000;
        int SPLIT = 25;
        int ITEMS = MAX / SPLIT;
        
        // for small pregenerate counts
        int[][][] stats = new int[SMALL][SPLIT][];
        
        // smamll
        for (int i = 2; i < SMALL; i++) {
            for (int i1 = 0; i1 < SPLIT; i1++) {
                stats[i][i1] = new int[i];
                for (int a_i = i1 * ITEMS; a_i < (i1 + 1) * ITEMS && a_i < n; a_i++) {
                    stats[i][i1][a[a_i] % i]++;
                }
            }
        }

        System.err.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();

        for (int i1 = 0; i1 < SPLIT; i1++) {
            stats[0][i1] = new int[MAX + 1];
            for (int a_i = i1 * ITEMS; a_i < (i1 + 1) * ITEMS && a_i < n; a_i++) {
                stats[0][i1][a[a_i]]++;
            }
        }

        System.err.println(Runtime.getRuntime().totalMemory()/1024/1024 + "Mb");
        System.err.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();
        
        StringBuilder sb = new StringBuilder();

        for (int a0 = 0; a0 < q; a0++) {
            int left = in.nextInt();
            int right = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();

            int nextStart = left + (ITEMS - left % ITEMS) % ITEMS;
            int lastEnd = right - right % ITEMS;

            int c = 0;
            if (x == 1) {
                c = right - left + 1;
            } else if (nextStart >= lastEnd) {
                for (int i = left; i <= right; i++) {
                    if (a[i] % x == y) c++;
                }
            } else if (x < SMALL) {
                for (int i = left; i < nextStart; i++) {
                    if (a[i] % x == y) c++;
                }
                for (int i = nextStart; i < lastEnd; i += ITEMS) {
                    c += stats[x][i / ITEMS][y];
                }
                for (int i = lastEnd; i <= right; i++) {
                    if (a[i] % x == y) c++;
                }
            } else {
                for (int i = left; i < nextStart; i++) {
                    if (a[i] % x == y) c++;
                }
                for (int i = nextStart; i < lastEnd; i += ITEMS) {
                    for (int y2 = y; y2 < stats[0][i / ITEMS].length; y2 += x) {
                        c += stats[0][i / ITEMS][y2];
                    }
                }
                for (int i = lastEnd; i <= right; i++) {
                    if (a[i] % x == y) c++;
                }
            }

            sb.append(c).append('\n');
            
            if (VALIDATE) {
                for (int i = left; i <= right; i++) {
                    if (a[i] % x == y) c--;
                }
                
                if (c != 0) throw new RuntimeException();
            }
        }

        System.err.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();

        return sb.toString();
    }

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int n = MAX;
        int q = MAX;
        sb.append(n).append(" ").append(q).append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(r.nextInt(MAX)).append(" ");
        }
        sb.append('\n');
        for (int i = 0; i < q; i++) {
            int L = r.nextInt(n);
            int R = r.nextInt(n);
            int x = 1000;
            x = r.nextInt(MAX) + 1;

//            L = 1;
//            R = n - 2;

            sb.append(Math.min(L, R)).append(" ").append(Math.max(L, R)).append(" ").append(x).append(" ").append(r.nextInt(x)).append('\n');
        }
        Scanner in = new Scanner(sb.toString());

        long t0 = System.currentTimeMillis();
        
        String solve = solve(in);
        System.out.println(solve.hashCode());

        System.err.println(System.currentTimeMillis() - t0);
    }
    
//    @interface Test {}
    
}
