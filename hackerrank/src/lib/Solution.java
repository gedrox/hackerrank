package lib;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(in.readLine());
        for(int a0 = 0; a0 < Q; a0++){
            String[] split = in.readLine().split(" ");
            int n = Integer.parseInt(split[0]);;
            int m = Integer.parseInt(split[1]);;
            int x = Integer.parseInt(split[2]);;
            int y = Integer.parseInt(split[3]);;
            ArrayList<Integer>[] c = new ArrayList[n];
            for (int i = 0; i < n; i++) c[i] = new ArrayList<>();
            boolean visited[] = new boolean[n];
            for(int a1 = 0; a1 < m; a1++){
                split = in.readLine().split(" ");
                int city_1 = Integer.parseInt(split[0])-1;
                int city_2 = Integer.parseInt(split[1])-1;
                c[city_1].add(city_2);
                c[city_2].add(city_1);
            }

            if (x <= y) {
                System.out.println(((long) n) * x);
                continue;
            }

            int k = 0;
            int vN = 0;

            int first = 0;
            while (vN < n) {
                while (visited[first]) first++;

                LinkedList<Integer> q = new LinkedList<>();
                q.add(first);
                while (!q.isEmpty()) {
                    int city = q.pollFirst();
                    if (!visited[city]) {
                        vN++;
                        visited[city] = true;
                        for (int ch : c[city]) {
                            if (!visited[ch]) {
                                q.add(ch);
                            }
                        }
                    }
                }
                k++;
            }

            System.out.println(((long) y) * n + ((long) k) * (x - y));
        }
    }
}
