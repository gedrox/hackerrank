package kickstart2018.C;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CScrambleEng {

    static final int MOD = 1_000_000_007;
    static int[] seed = new int[26];
    static Random r = new Random(0);

    static {
        for (var i = 0; i < seed.length; i++) {
            seed[i] = r.nextInt(MOD) + 1;
        }
    }

    public static void main(String[] args) throws Exception {

        var fn = new Scanner(System.in).nextLine();

        var t0 = System.currentTimeMillis();
        
        fn = fn.replace("\"", "").replace("\\", "/");
        var f = new Scanner(new File(fn));
        var out = fn + ".out";
        var sb = new StringBuilder();
        var q = f.nextInt();

        var matches = new int[q];
//        ArrayList<Future<Void>> futures = new ArrayList<>();
//        ArrayList<Callable<Void>> callables = new ArrayList<>();
//        AtomicInteger done = new AtomicInteger();
        var falsePositives = new AtomicInteger();

        var ex = Executors.newFixedThreadPool(8);

        for (var q_i = 0; q_i < q; q_i++) {

            System.err.println("Start #" + (q_i + 1));

            var L = f.nextInt();
            f.nextLine();
            var W = f.nextLine().split(" ");
            assert W.length == L;

            var S1 = f.next().charAt(0);
            var S2 = f.next().charAt(0);
            var N = f.nextInt();
            var A = f.nextLong();
            var B = f.nextLong();
            var C = f.nextLong();
            var D = f.nextLong();

            var S = new char[N];
            S[0] = S1;
            S[1] = S2;
            var x1 = (long) S1;
            var x2 = (long) S2;
            for (var i = 2; i < N; i++) {
                var newX = (A * x2 + B * x1 + C) % D;
                x1 = x2;
                x2 = newX;
                S[i] = (char) (97 + newX % 26);
            }

            // letter count
            var W_let = new int[W.length][26];
            for (var i = 0; i < W.length; i++) {
                for (var j = 0; j < W[i].length(); j++) {
                    W_let[i][W[i].charAt(j) - 'a']++;
                }
            }

            var S_let = new int[S.length + 1][];
            S_let[0] = new int[26];
            for (var i = 0; i < S.length; i++) {
                S_let[i + 1] = S_let[i].clone();
                S_let[i + 1][S[i] - 'a']++;
            }

            // hash till...
            var SH = new int[N + 1];
            SH[0] = 0;
            for (var i = 0; i < N; i++) {
                SH[i + 1] = (SH[i] + seed[S[i] - 'a']) % MOD;
            }
            
            var lengths = new HashMap<Integer, ArrayList<Integer>>();
            for (var w_i = 0; w_i < W.length; w_i++) {
                lengths.computeIfAbsent(W[w_i].length(), (i) -> new ArrayList<>()).add(w_i);
            }

            var finalQ_i = q_i;

            for (var len : lengths.keySet()) {

                Callable<Void> solver = () -> {
                    
                    var wordByHash = new HashMap<Integer, ArrayList<Integer>>();
                    for (var w_i : lengths.get(len)) {
                        wordByHash.computeIfAbsent(hashOf(W[w_i]), $ -> new ArrayList<>()).add(w_i);
                    }
                    
                    for (var po = 0; po <= N - len && !wordByHash.isEmpty(); po++) {
                        var hash = SH[po + len] - SH[po];
                        if (hash < 0) hash += MOD;
                        
                        if (wordByHash.containsKey(hash)) {
                            nextPos:
                            for (var it = wordByHash.get(hash).iterator(); it.hasNext(); ) {
                                var w_i = it.next();
                                if (W[w_i].charAt(0) == S[po] && W[w_i].charAt(len - 1) == S[po + len - 1]) {

                                    for (var j = 0; j < 26; j++) {
                                        if (W_let[w_i][j] != S_let[po + len][j] - S_let[po][j]) {
                                            falsePositives.incrementAndGet();
                                            continue nextPos;
                                        }
                                    }

                                    matches[finalQ_i]++;
                                    it.remove();
                                }
                            }
                            if (wordByHash.get(hash).isEmpty()) {
                                wordByHash.remove(hash);
                            }
                        }
                    }

//                    System.err.println(new Date() + " Finished #" + finalQ_i);// + ", remaining " + (callables.size() - done.incrementAndGet()));
                    return null;
                };

//                callables.add(solver);
                ex.submit(solver);
            }
        }
        
//        futures.addAll(ex.invokeAll(callables));
//        for (Future future : futures) future.get();
        
        ex.shutdown();
        
        System.err.println("Took " + (System.currentTimeMillis() - t0) / 1000 + "s");

        for (var q_i = 0; q_i < q; q_i++) {
            var answer = matches[q_i];
            sb.append("Case #").append(q_i + 1).append(": ").append(answer).append('\n');
        }

//        System.out.println(sb.toString());
        Files.write(Paths.get(out), sb.toString().getBytes());

        System.err.println("False positives: " + falsePositives.get());
    }

    private static int hashOf(String s) {
        var h = 0L;
        for (var j = 0; j < s.length(); j++) {
            h += seed[s.charAt(j) - 'a'];
            h %= MOD;
        }
        return (int) h;
    }
}
