package chef.april.dishlife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = parseInt(br.readLine());

        for (int t_i = 0; t_i < T; t_i++) {
            String[] line = br.readLine().split(" ");
            int n = parseInt(line[0]);
            int k = parseInt(line[1]);

            int[][] p = new int[n][];
            int[] count = new int[k];

            for (int n_i = 0; n_i < n; n_i++) {
                line = br.readLine().split(" ");
                int P = parseInt(line[0]);
                p[n_i] = new int[P];
                for (int P_i = 0; P_i < P; P_i++) {
                    int prod = parseInt(line[P_i + 1]) - 1;
                    p[n_i][P_i] = prod;
                    count[prod]++;
                }
            }
            
            String res = null;
            for (int c : count) {
                if (c == 0) {
                    res = "sad";
                    break;
                }
            }
            
            if (res == null) {
                nextIsland:
                for (int n_i = 0; n_i < n; n_i++) {
                    for (int prod : p[n_i]) {
                        if (count[prod] == 1) {
                            continue nextIsland;
                        }
                    }
                    res = "some";
                    break;
                }
            }
            
            if (res == null) {
                res = "all";
            }

            System.out.println(res);
        }
    }
}
