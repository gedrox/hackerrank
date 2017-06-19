package street_parade;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Case {
    public static void main(String[] args) throws FileNotFoundException {
        int n = 200000 - 1;
        int a[] = new int[n];
        a[0] = 0;
        for (int i = 1; i < n; i++) {
            a[i] = a[i-1] + (i % 2 == 1 ? 1 : 3);
        }

        //System.out.println(Arrays.toString(a));

        PrintStream out = new PrintStream("in.txt");
//        PrintStream out = System.out;

        out.println(n);
        for (int i = 0; i < n; i++) {
            out.print(a[i] + " ");
        }
        out.println();
        out.println("4 2 3");
    }
}
