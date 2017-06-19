package chef.april.ROWSOLD;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t_i = 0; t_i < T; t_i++) {
            ArrayList<Integer> b = new ArrayList<>();
            int i = 0;
            b.add(0);
            
            long moves = 0;
            
            String S = sc.next();
            int count = 0;
            for (int s_i = 0; s_i < S.length(); s_i++) {
                if (b.get(i) != 0 && S.charAt(s_i) == '0') {
                    i++;
                    b.add(0);
                }
                
                moves += count;
                
                if (S.charAt(s_i) == '1') {
                    count++;
                    b.set(i, b.get(i) + 1);
                }
            }
            
            moves -= ((long) count) * (count - 1) / 2;
            
            for (int b_i = 0; b_i < b.size(); b_i++) {
                moves += b.get(b_i) * (b.size() - b_i - 1);
            }
            System.out.println(moves);
        }
    }
}
