package gcj3.b;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SolutionB {

    public static final String TEST_NAME = "sample";

    public static void main(String[] args) throws IOException {
        String folder = "src/gcj3/b";

        BufferedReader br = new BufferedReader(new FileReader(folder + "/" + TEST_NAME + ".in"));

        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        for (int t_i = 1; t_i <= T; t_i++) {
            int answer = 0;
            
            // TODO: code here
            
            sb.append("Case #").append(t_i).append(": ").append(answer).append("\n");
        }

        Files.write(Paths.get(folder + "/" + TEST_NAME + ".out"), sb.toString().getBytes());
        System.out.println(sb.toString());
    }
}
