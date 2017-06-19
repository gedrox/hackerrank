package chef.april.similardishes;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < T; i++) {
            String[] a = sc.nextLine().split(" ");
            String[] b = sc.nextLine().split(" ");
            int eq = 0;
            for (String s : a) {
                for (String s1 : b) {
                    if (s.equals(s1)) {
                        eq++;
                    }
                }
            }
            System.out.println(eq >= 2 ? "similar" : "dissimilar");
        }
    }
}
