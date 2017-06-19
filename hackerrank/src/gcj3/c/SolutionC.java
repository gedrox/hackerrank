package gcj3.c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SolutionC {
    public static final String TEST_NAME = "small1";

    public static void main(String[] args) throws IOException {
        String folder = "src/gcj3/c";

        BufferedReader br = new BufferedReader(new FileReader(folder + "/" + TEST_NAME + ".in"));

        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        for (int t_i = 1; t_i <= T; t_i++) {

//            System.err.println(" !!! Case " + t_i);

            int N = Integer.parseInt(br.readLine());
            
            Path[] first = new Path[N];
            Path[] second = new Path[N];
            
            for (int i = 0; i < N; i++) {
                first[i] = parsePath(br);
                second[i] = parsePath(br);
            }
            
            long minHours = Long.MAX_VALUE;

            nextBrute:
            for (int brute = 0; brute < (1 << N); brute++) {

                int[] used = new int[N];
//                System.err.println(" ! Next path " + brute);
                
                int curr = 0;
                long currHour = 0;
//                long days = 0;

                for (int i = 0; i < 2 * N; i++) {
                    Path nextPath;
                    if (used[curr] == 3) {
                        continue nextBrute;
                    }
                    if (used[curr] != 0) {
                        nextPath = used[curr] == 1 ? second[curr] : first[curr];
//                        System.err.println("1) Picking " + (used[curr] == 1 ? "second" : "first") + " at " + curr);
                        used[curr] = 3;
                    } else {
                        boolean pickFirst = (brute & (1 << curr)) == 0;
                        nextPath = pickFirst ? first[curr] : second[curr];
                        used[curr] = pickFirst ? 1 : 2;
//                        System.err.println("2) Picking " + (!pickFirst ? "second" : "first") + " at " + curr);
                    }
                    
                    while (currHour % 24 != nextPath.clock) {
//                        currHour += 24 - ((24 + currHour - nextPath.clock) % 24);
                        currHour++;
                    }
                    
//                    if (nextPath.clock < currClock) {
////                        System.err.println("We are late, need to wait next day");
//                        days++;
//                    }
                    
                    curr = nextPath.to;
                    currHour += nextPath.hours;
//                    System.err.println("Will be @destination " + currClock + " hours");
                }

//                System.err.println("Answer would be " + (24 * days + currClock));
             
                if (curr != 0) throw new RuntimeException();
                
                if (currHour < minHours) {
                    minHours = currHour;
                }
            }
            
            sb.append("Case #").append(t_i).append(": ").append(minHours).append("\n");
        }

        Files.write(Paths.get(folder + "/" + TEST_NAME + ".out"), sb.toString().getBytes());
        System.out.println(sb.toString());
    }

    private static Path parsePath(BufferedReader br) throws IOException {
        String[] split = br.readLine().split(" ");
        Path path = new Path();
        path.to = Integer.parseInt(split[0]) - 1;
        path.clock = Integer.parseInt(split[1]);
        path.hours = Integer.parseInt(split[2]);
        return path;
    }

    static class Path {
        int to;
        int clock;
        long hours;
    }
}
