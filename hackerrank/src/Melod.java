import org.junit.Test;

import java.util.ArrayList;
import java.util.Scanner;

public class Melod {

    public static final char[] C1 = new char[]{'a', 'e', 'i', 'o', 'u'};
    public static final char[] C2 = new char[]{'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'};

    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();

        StringBuilder sb = solve(n);
        System.out.print(sb.toString());
    }
    
    @Test
    public void test() {
        
        StringBuilder sb = new StringBuilder();
        
        char[][] chars = get(6, false);
        for (char[] aChar : chars) {
            for (char c : aChar) {
                sb.append(c);
            }
            sb.append('\n');
        }
        
        
//        System.out.println(chars.length);
        chars = get(6, true);
        for (char[] aChar : chars) {
            for (char c : aChar) {
                sb.append(c);
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
//        System.out.println(chars.length);
    }
    @Test
    public void test2() {
        System.out.println(solve(6).toString());
    }

    private static StringBuilder solve(int n) {

        ArrayList<String> A = getStrings(C1, C2, n, true);
        ArrayList<String> B = getStrings(C1, C2, n, false);

        StringBuilder sb = new StringBuilder();
        for (String s : A) sb.append(s).append('\n');
        for (String s : B) sb.append(s).append('\n');
        return sb;
    }
    
    static char[][] get(int n, boolean b) {
        int total = 1;
        
        boolean b1 = b;
        for (int n1 = n - 1; n1 >= 0; n1--) {
            total *= (b1 ? C1 : C2).length;
            b1 = !b1;
        }
        
        long t0 = System.currentTimeMillis();
        char[][] out = new char[total][n];
        System.err.println(System.currentTimeMillis() - t0);

        t0 = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            int j = i;
            for (int n1 = n - 1; n1 >= 0; n1--) {
                char[] C = b ? C1 : C2;
                out[i][n1] = C[j % C.length];
                j /= C.length;
                b = !b;
            }
        }
        System.err.println(System.currentTimeMillis() - t0);
        
        return out;
    }

    private static ArrayList<String> getStrings(char[] c1, char[] c2, int n, boolean b) {
        ArrayList<String> x = new ArrayList<>();
        x.add("");
        for (int i1 = 0; i1 < n; i1++) {
            char[] chars = b ? c1 : c2;
            ArrayList<String> y = new ArrayList<>(x.size() * chars.length);
            for (String s : x) {
                for (char c : chars) {
                    y.add(s + c);
                }
            }
            b = !b;
            x = y;
        }
        return x;
    }
}
