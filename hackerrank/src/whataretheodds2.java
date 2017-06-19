import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class whataretheodds2 {
    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        String S[] = in.readLine().split(" ");
        int[] s = new int[n];
        for(int s_i=0; s_i < n; s_i++){
            s[s_i] = Integer.parseInt(S[s_i]);
        }
        
        int[] hash = new int[n + 1];

        HashMap<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        
        int xor = 0;
        for (int i = 0; i < n; i++) {
            xor ^= s[i];
            hash[i + 1] = xor;
            
            count.put(xor, (count.containsKey(xor) ? count.get(xor) : 0) + 1);
        }

        long total = 0;

        for (int i = 0; i <= n; i++) {
            count.put(hash[i], count.get(hash[i]) - 1);
            if (count.containsKey(xor ^ hash[i])) {
                total += count.get(xor ^ hash[i]);
            }
        }
        
        System.out.println(total);
    }
}
