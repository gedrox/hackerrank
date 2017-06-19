package winning_move;

import java.util.Random;

// Sample input 1, in Java.
public class winning_move {

    public static final Random RANDOM = new Random();

    public winning_move() {
    }

    public static long GetNumPlayers() {
        return (long) 4e6;
    }

    public static long GetSubmission(long playernum) {
        return RANDOM.nextInt(80000);
    }
}