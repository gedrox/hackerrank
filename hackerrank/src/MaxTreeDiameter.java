import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MaxTreeDiameter {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        ArrayList<Integer>[] next = new ArrayList[n];
        
        for (int i = 0; i < n - 1; i++) {
            String[] parse = br.readLine().split(" ");
            int from = Integer.parseInt(parse[0]) - 1;
            int to = Integer.parseInt(parse[1]) - 1;
            
            next[from].add(to);
            next[to].add(from);
        }
        
        
    }
}
