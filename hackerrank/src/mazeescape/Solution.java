package mazeescape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Solution {

    public static final char N = '#';
    public static final char Y = '-';
    public static final char E = 'e';

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();

        String line1 = sc.nextLine();
        String line2 = sc.nextLine();
        String line3 = sc.nextLine();

        char[] maze = {
                N, N, N, N, N, N, N,
                N, Y, Y, N, Y, Y, N,
                N, Y, Y, N, Y, Y, N,
                N, Y, Y, N, Y, Y, N,
                E, Y, Y, Y, Y, Y, N,
                N, Y, Y, Y, Y, Y, N,
                N, N, N, N, N, N, N
        };

        State state = new State();

        // todo: load from file

        state.register(1, line1);
        state.register(0, line2);
        state.register(-1, line3);


    }

    static class State {
        int x = 0;
        int y = 0;
        char dir = 'u';
        HashMap<String, Character> info = new HashMap<>();

        public void register(int yDiff, String line1) {

        }
    }
}
