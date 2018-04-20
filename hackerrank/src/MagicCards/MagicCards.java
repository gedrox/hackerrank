package MagicCards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class MagicCards {

    static long[] SQ = new long[1000001];
    static {
        for (int i = 0; i < SQ.length; i++) {
            SQ[i] = (long) i * i;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] split = bi.readLine().split(" ");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int q = Integer.parseInt(split[2]);
        
        boolean[][] side = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            split = bi.readLine().split(" ");
            int c = Integer.parseInt(split[0]);
            for (int j = 0; j < c; j++) {
                side[i][Integer.parseInt(split[j + 1]) - 1] = true;
            }
        }

        long allSum = (long) m * (m + 1) * (2*m + 1) / 6;
        
        StringBuilder sb = new StringBuilder();
        
        Query[] queries = new Query[q];
        ArrayList<Query> qSolve = new ArrayList<Query>();
        
        for (int q_i = 0; q_i < q; q_i++) {
            split = bi.readLine().split(" ");
            int l = Integer.parseInt(split[0]) - 1;
            int r = Integer.parseInt(split[1]) - 1;

            queries[q_i] = new Query(l, r);

            if (r - l + 1 >= 20 || (1 << (r - l + 1)) > m) {
                queries[q_i].answer = allSum;
            } else {
                qSolve.add(queries[q_i]);
            }
        }

        for (int start = 0; start < n; start++) {
            for (int len = 1; len <= 20; len++) {
                
            }
        }
        
        if (false) {
            qSolve.sort(Comparator.<Query, Integer>comparing(Q -> Q.l).thenComparing(Q -> Q.r));
            State prevState = null;     
            
            for (Query Q : qSolve) {
                
                int[] hash;
    
                if (prevState == null || Q.l > prevState.l) {
                    hash = new int[m];
    
                    int bit = 1;
                    for (int x = Q.l; x <= Q.r; x++) {
                        for (int y = 0; y < m; y++) {
                            if (side[x][y]) {
                                hash[y] += bit;
                            }
                        }
                        bit <<= 1;
                    }
                    
                    prevState = new State();
                    prevState.l = Q.l;
                    prevState.r = Q.r;
                    prevState.bit = bit;
                    prevState.hash = hash;
                    
                } else {
    
                    hash = prevState.hash;
                    
                    if (prevState.l < Q.l) {
                        prevState.bit >>= Q.l - prevState.l;
                        for (int i = 0; i < hash.length; i++) {
                            hash[i] >>= Q.l - prevState.l;
                        }
                    }
                    
                    if (prevState.r < Q.r) {
                        for (int x = prevState.r + 1; x <= Q.r; x++) {
                            for (int y = 0; y < m; y++) {
                                if (side[x][y]) {
                                    hash[y] += prevState.bit;
                                }
                            }
                            prevState.bit <<= 1;
                        }
                    }
                    
                    if (prevState.r > Q.r) {
                        prevState.bit >>= prevState.r - Q.r;
                        for (int i = 0; i < hash.length; i++) {
                            hash[i] &= prevState.bit - 1;
                        }
                    }
                    
                    prevState.l = Q.l;
                    prevState.r = Q.r;
                }
                
                int newM = 1 << (Q.r - Q.l + 1);
                long[] cnt = new long[newM];
                for (int y = 0; y < hash.length; y++) {
                    cnt[hash[y]] += SQ[y + 1];
                }
                long min = allSum;
                for (long l : cnt) {
                    if (l < min) {
                        min = l;
                        if (min == 0) break;
                    }
                }
    
                Q.answer = allSum - min;
            }
    
            for (Query query : queries) {
                sb.append(query.answer).append("\n");
            }
        }
        
        System.out.print(sb);
    }
    
    static class Query {
        int l, r;
        long answer;

        public Query(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }
    
    static class State {
        int l, r, bit;
        int[] hash;
    }
}
