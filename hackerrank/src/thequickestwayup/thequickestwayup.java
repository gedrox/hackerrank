package thequickestwayup;

import java.util.LinkedList;
import java.util.Scanner;

public class thequickestwayup {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t_i = 0; t_i < T; t_i++) {
            int[] target = new int[100];
            for (int i = 0; i < 100; i++) {
                target[i] = i;
            }
            int ladders = sc.nextInt();
            for (int i = 0; i < ladders; i++) {
                int from = sc.nextInt() - 1;
                int to = sc.nextInt() - 1;
                target[from] = to;
            }
            int snakes = sc.nextInt();
            for (int i = 0; i < snakes; i++) {
                int from = sc.nextInt() - 1;
                int to = sc.nextInt() - 1;
                target[from] = to;
            }
            
            int[] min = new int[100];
            for (int i = 0; i < 100; i++) {
                min[i] = Integer.MAX_VALUE;
            }

            LinkedList<Integer> q = new LinkedList<>();
            min[0] = 0;
            q.add(0);
            
            while (!q.isEmpty()) {
                int next = q.poll();
                for (int st = 1; st <= 6; st++) {
                    int where = next + st;
                    if (where > 99) break;
                    where = target[where];
                    if (min[next] + 1 < min[where]) {
                        min[where] = min[next] + 1;
                        q.add(where);
                    }
                }
            }

            System.out.println(min[99] == Integer.MAX_VALUE ? -1 : min[99]);
        }
    }
}
