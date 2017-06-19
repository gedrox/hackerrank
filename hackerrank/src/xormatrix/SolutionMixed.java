package xormatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class SolutionMixed {

    static Map<String, Long> memory = new HashMap<>();
    public static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {

        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] split = bi.readLine().split(" ");
        int N = Integer.parseInt(split[0]);
        long M = Long.parseLong(split[1]) - 1;

        int[] line = new int[N];

        split = bi.readLine().split(" ");
        for (int i = 0; i < N; i++) {
            line[i] = Integer.parseInt(split[i]);
        }

//        int N = 10;
//        int[] line = new int[] {101, 50, 96, 2, 66, 7, 15, 19, 80, 81};
//        long M = 6L;
        String out = solve2(N, M, line);
        assert out.equals(solveTrivial(N, M, line));

        System.out.println(out);
        System.out.println(solveTrivial(N, M, line));


//        for (int i = 1; i < 100; i++) {
//            solve(i);
//            memory.clear();
//        }
//        int N = 20;
//        solve(N);
    }

    private static String solve2(int n, long m, int[] line) {
        long pow2 = 576460752303423488L;

        while (m > 0) {
            while (m < pow2) {
                pow2 /= 2;
            }

            m -= pow2;

            int[] newLine = new int[n];
            for (int i = 0; i < n; i++) {
                newLine[i] = line[i] ^ line[(int) ((i + pow2) % n)];
            }

            line = newLine;
        }

        return Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

    private static String solveTrivial(int n, long m, int[] line) {
        for (long i = 0; i < m; i++) {
            int[] nextLine = new int[n];
            for (int i1 = 0; i1 < n; i1++) {
                nextLine[i1] = line[i1] ^ line[(i1+1)% n];
            }
            line = nextLine;
        }
        return Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

    private static void solve(int n) {
        long[] line = new long[n];
        for (int i1 = 0; i1 < n; i1++) {
            line[i1] = RANDOM.nextInt(1_000_000_001);
        }


//        int[] line = {6, 7, 1, 4, 5, 101};
//        System.out.println(Arrays.toString(line));
//        int N = line.length;

        long i = 0;
        do {
            i++;
            long[] nextLine = new long[n];
            for (int i1 = 0; i1 < n; i1++) {
                nextLine[i1] = line[i1] ^ line[(i1+1)% n];
            }
            String string = Arrays.toString(nextLine);
//            System.out.println(string);

            if (memory.containsKey(string)) {
                System.out.println(n + ": Found repetition on " + i + " of line " + memory.get(string));
                break;
            } else {
                memory.put(string, i);
            }

            line = nextLine;
        } while (true);
    }
}
