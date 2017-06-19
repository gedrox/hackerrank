package bot;

import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        int b_x = sc.nextInt();
        int b_y = sc.nextInt();
        int n = 5;
        int m = 5;
        sc.nextLine();
        int d_x = -1, d_y = -1, dist = Integer.MAX_VALUE;

        // zero char means no information
        char[] intel = new char[n*m];
        File intelFile = new File("intel");

        if (intelFile.exists()) {
            try (FileReader reader = new FileReader(intelFile)) {
                int read = reader.read(intel, 0, n * m);
                if (read != n * m) throw new RuntimeException();
            }
        }

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < m; j++) {
                if (line.charAt(j) != 'o') {
                    intel[i*m + j] = line.charAt(j) == 'd' ? 'd' : '-';
                }
                if ((line.charAt(j) == 'd' || intel[i*m + j] == 'd') && Math.abs(i - b_x) + Math.abs(j - b_y) < dist) {
                    dist = Math.abs(i - b_x) + Math.abs(j - b_y);
                    d_x = i;
                    d_y = j;
                }
            }
        }

        if (d_x == -1) {

            // search for the nearest point where we get some info
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (intel[i*m + j] == 0) {
                        double dirX = Math.signum(b_x - i);
                        int diffX = Math.abs(b_x - i);
                        if (diffX > 0) diffX--;

                        double dirY = Math.signum(b_y - j);
                        int diffY = Math.abs(b_y - j);
                        if (diffY > 0) diffY--;

                        if (diffX + diffY < dist) {
                            dist = diffX + diffY;
                            d_x = (int) (b_x - dirX * diffX);
                            d_y = (int) (b_y - dirY * diffY);
                        }
                    }
                }
            }

            if (dist == 0) throw new RuntimeException();
        }

        if (dist == 0) {
            System.out.println("CLEAN");
        } else if (d_x > b_x) {
            System.out.println("DOWN");
        } else if (d_x < b_x) {
            System.out.println("UP");
        } else if (d_y > b_y) {
            System.out.println("RIGHT");
        } else if (d_y < b_y) {
            System.out.println("LEFT");
        }

        try (FileWriter writer = new FileWriter(intelFile)) {
            writer.write(intel);
            writer.flush();
        }
    }
}