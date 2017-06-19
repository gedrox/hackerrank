package gcj3.a;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SolutionA {
    
    static long[] POW10 = new long[11];
    static {
        POW10[0] = 1;
        for (int i = 1; i < POW10.length; i++) {
            POW10[i] = 10 * POW10[i - 1];
        }
    }
    
    static long C[][] = new long[10][10];
    static {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (j > i) {
                    C[i][j] = 0;
                } else if (j == i) {
                    C[i][j] = 1;
                } else {
                    long comb = 1;
                    for (int i1 = 0; i1 < j; i1++) {
                        comb *= (i - i1);
                    }
                    for (int i1 = 0; i1 < j; i1++) {
                        comb /= (i1 + 1);
                    }
                    C[i][j] = comb;
                }
            }
        }
    }
    
    public static final String TEST_NAME = "large";

    public static void main(String[] args) throws IOException {
        long t0 = System.currentTimeMillis();
        
        String folder = "src/gcj3/a";

        BufferedReader br = new BufferedReader(new FileReader(folder + "/" + TEST_NAME + ".in"));

        int maxL = 9;

        HashMap<Integer, ArrayList<Integer>>[] back = new HashMap[maxL + 1];

        for (int i = 1; i <= maxL; i++) {
            back[i] = new HashMap<>();
            nextOne:
            for (int source = 1; source < POW10[i]; source++) {
                int x = source;
                int[] c = new int[i + 1];
                int sum = 0;
                while (x != 0) {
                    if (x % 10 > i) {
                        continue nextOne;
                    }
                    sum += x % 10;
                    if (sum > i) {
                        continue nextOne;
                    }
                    c[x % 10]++;
                    x /= 10;
                }
                int target = 0;
                for (int k = 1; k <= i; k++) {
                    target = 10 * target + c[k];
                }
                if (!back[i].containsKey(target)) {
                    back[i].put(target, new ArrayList<>());
                }
                back[i].get(target).add(source);
            }
        }

        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        for (int t_i = 1; t_i <= T; t_i++) {
            
            long additional = 0;
            
            HashSet<Integer> source = new HashSet<>();
            String s = br.readLine();
            int L = s.length();
            int val = Integer.parseInt(s);
            LinkedList<Integer> q = new LinkedList<>();
            q.add(val);
            nextOne2:
            while (!q.isEmpty()) {
                Integer next = q.pollFirst();
                if (!source.contains(next)) {
                    source.add(next);
                    if (back[L].containsKey(next)) {
                        q.addAll(back[L].get(next));
                    } else {
                        // possible combinations
                        int x = next;
                        int[] c = new int[L + 1];
                        int sum = 0;
                        int d_i = L;
                        while (x != 0) {
                            if (x % 10 > L) {
                                continue nextOne2;
                            }
                            sum += x % 10;
                            if (sum > L) {
                                continue nextOne2;
                            }
                            c[d_i] = x % 10;
                            x /= 10;
                            d_i--;
                        }
                        
                        long add = 1;
                        int places = L;
                        for (int count : c) {
                            add *= C[places][count];
                            places -= count;
                        }
                        
                        additional += add;
                    }
                }
            }

            sb.append("Case #").append(t_i).append(": ").append(source.size() + additional).append("\n");
        }

        Files.write(Paths.get(folder + "/" + TEST_NAME + ".out"), sb.toString().getBytes());
        System.out.println(sb.toString());

        System.out.println(System.currentTimeMillis() - t0 + "ms");
    }
}
