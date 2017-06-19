package hackonacci;

import java.io.*;

public class S {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] line = br.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int q = Integer.parseInt(line[1]);

        char a[][] = new char[n][];
        char c[] = {'Y', 'Y', 'X', 'Y', 'X', 'X', 'Y'};


        for (int i = 0; i < n; i++) {
            a[i] = new char[n];
            for (int j = 0; j < n; j++) {
                a[i][j] = c[(((i+1)%7) * ((i+1)%7) * ((j+1)%7) * ((j+1)%7)) % 7];
            }
        }

        int res[] = new int[4];
        res[0] = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] != a[j][n - 1 - i]) res[1]++;
                if (a[i][j] != a[n - 1 - i][n - 1 - j]) res[2]++;
                if (a[i][j] != a[n - 1 - j][i]) res[3]++;
            }
        }

        for (int i = 0; i < q; i++) {
            int nextQ = Integer.parseInt(br.readLine());
            System.out.println(res[(nextQ / 90) % 4]);
        }
    }
}
