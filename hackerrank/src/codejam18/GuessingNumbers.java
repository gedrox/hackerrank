package codejam18;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GuessingNumbers {
    public static void main(String[] args) {
        InputStream in = System.in;
        PrintStream out = System.out;

        solve(in, out);
    }
    
//    @Test
//    public void test() throws IOException {
//        Process exec = Runtime.getRuntime().exec("C:\\Users\\agedroics\\AppData\\Local\\Programs\\Python\\Python36-32\\python.exe testing_tool.py");
//        InputStream in = exec.getInputStream();
//        OutputStream out = exec.getOutputStream();
//        PrintStream out2 = new PrintStream(out);
//        try {
//            solve(in, out2);
//        } catch (Exception e) {
//            
//        }
//    }

    private static void solve(InputStream in, PrintStream out) {
        Scanner sc = new Scanner(in);
        int T = sc.nextInt();
        nextTestCase:
        for (int t_i = 0; t_i < T; t_i++) {
            int A = sc.nextInt();
            int B = sc.nextInt();
            int N = sc.nextInt();
            
            while (true) {
                int G = (A + 1 + B) / 2;
                out.println(G);
                out.flush();
                
                String res = sc.next();
                if (res.equals("CORRECT")) {
                    continue nextTestCase;
                }
                if (res.equals("TOO_SMALL")) {
                    A = G;
                }
                if (res.equals("TOO_BIG")) {
                    B = G - 1;
                }
            }
        }
    }
}
