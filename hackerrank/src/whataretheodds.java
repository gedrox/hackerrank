import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

public class whataretheodds {
    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine());
        String S[] = in.readLine().split(" ");
        int[] s = new int[n];
        for(int s_i=0; s_i < n; s_i++){
            s[s_i] = Integer.parseInt(S[s_i]);
        }
        
        double[][] hashS = new double[n + 1][2];
        double[][] hashE = new double[n + 1][2];
        hashS[0][0] = 0;
        hashS[0][1] = 0;
        
        hashE[n][0] = n;
        hashE[n][1] = 0;
        
        int xor = 0;
        for (int i = 0; i < n; i++) {
            xor ^= s[i];
            hashS[i + 1][0] = i + 1;
            hashS[i + 1][1] = xor;
        }
        xor = 0;
        for (int i = n - 1; i >= 0; i--) {
            xor ^= s[i];
            hashE[i][0] = i;
            hashE[i][1] = xor;
        }

        int bad = 0;
        for (int i = 0; i <= n; i++) {
            if (hashS[i][1] == hashE[i][1]) {
                bad++;
            }
        }

        Comparator<double[]> c = Comparator.comparingInt(a -> (int) (2*a[1]));
        Arrays.sort(hashS, c);
        Arrays.sort(hashE, c);

        long total = 0;
        
        int a = 0, b = 0;
        while (a <= n && b <= n) {
            if (hashS[a][1] == hashE[b][1]) {
                xor = (int) hashS[a][1];

                int newa = Arrays.binarySearch(hashS, a+1, n+1, new double[]{-1, xor + .5}, c);
                newa = -newa - 1;
                int newb = Arrays.binarySearch(hashE, b+1, n+1, new double[]{-1, xor + .5}, c);
                newb = -newb - 1;

                total += ((long) (newa - a)) * (newb - b);

                a = newa;
                b = newb;
            } else if (hashS[a][1] < hashE[b][1]) {
                a = Arrays.binarySearch(hashS, a+1, n+1, new double[]{-1, hashE[b][1] - .5}, c);
                a = - a - 1;
            } else {
                b = Arrays.binarySearch(hashE, b+1, n+1, new double[]{-1, hashS[a][1] - .5}, c);
                b = - b - 1;
            }
        }
        System.out.println((total - bad) / 2);
    }
}
