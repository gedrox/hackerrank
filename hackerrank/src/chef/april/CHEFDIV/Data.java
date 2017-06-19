package chef.april.CHEFDIV;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Data {
    public static void main(String[] args) throws IOException {
        long t0 = System.currentTimeMillis();
//        Scanner sc = new Scanner(System.in);
        System.out.println("buff");
        Scanner sc = new Scanner(new BufferedInputStream(System.in));
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < 10000000; i++) {
            sc.nextInt();
//            br.readLine();
        }
        System.out.println(System.currentTimeMillis() - t0);
    }

    @Test
    public void fill() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000000; i++) {
            sb.append(1000).append("\n");
        }
        Files.write(Paths.get("test.test"), sb.toString().getBytes());
    }

    @Test
    public void scanner() throws FileNotFoundException {
        // 5.5s
        Scanner sc = new Scanner(new File("test.test"));
        for (int i = 0; i < 10000000; i++) {
            sc.nextInt();
        }
    }

    @Test
    public void br() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("test.test"));
        String[] split = br.readLine().split(" ");
        for (String s : split) {
            Integer.parseInt(s);
        }
    }


}
