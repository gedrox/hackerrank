package kickstart2018.C;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GenC {
    static Random r = new Random(0);
    
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        int T = 20;
        int L = 20_000;
        int W_len = 100_000 / L;
        int N = 1_000_000;

        sb.append(T).append('\n');
        
        for (int t_i = 0; t_i < T; t_i++) {
            
            sb.append(L).append('\n');
            
            for (int i = 0; i < L; i++) {
                for (int j = 0; j < W_len; j++) {
                    sb.append(randChar());
                }
                if (i < L - 1) sb.append(' ');
            }
            sb.append('\n');
            
            sb.append(randChar()).append(' ');
            sb.append(randChar()).append(' ');
            sb.append(r.nextInt(1_000_000 - 1) + 2).append(' ');
            sb.append(r.nextInt(1_000_000_001)).append(' ');
            sb.append(r.nextInt(1_000_000_001)).append(' ');
            sb.append(r.nextInt(1_000_000_001)).append(' ');
            sb.append(r.nextInt(1_000_000_000) + 1).append('\n');
        }

        Files.write(Paths.get("src/kickstart2018/C/genC.in"), sb.toString().getBytes());
    }

    private static char randChar() {
        return (char) ('a' + r.nextInt(26));
    }
}
