package stonediv;

import java.util.HashMap;

public class Solution {
    static long[] moves;

    public static void main(String[] args) {
        long n = 81;
        long[] s = {3, 9};

//        Scanner sc = new Scanner(System.in);
//        long n = sc.nextLong();
//        int m = sc.nextInt();
//        long[] s = new long[m];
//        for (int i = 0; i < m; i++) {
//            s[i] = sc.nextLong();
//        }

        boolean firstCanWin = solve(n, s);

        System.out.println(firstCanWin ? "First" : "Second");
    }

    private static boolean solve(long n, long[] s) {

        for (long s_i : s) {
            if (((s_i % 2) == 0) && ((n % s_i) == 0)) {
                return true;
            }
        }

        moves = s;
        Game game = new Game();
        game.stacks.put(n, 1L);
        return winExists(game, 1);
    }

    static boolean winExists(Game game, int depth) {
        for (Long stones : game.stacks.keySet()) {
            for (long move : moves) {
                if (stones % move == 0) {
                    Game newGame = game.move(stones, move);
                    if (!winExists(newGame, depth + 1)) {
                        if (depth == 1) System.err.println(stones + " " + move);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static class Game {
        HashMap<Long, Long> stacks = new HashMap<>();

        public Game() {

        }

        public Game(HashMap<Long, Long> stacks) {
            this.stacks = new HashMap<>(stacks);
        }

        Game move(long stones, long move) {
            Game newGame = new Game(stacks);
            newGame.add(stones, -1);
            newGame.add(stones / move, move);
            return newGame;
        }

        private void add(long stones, long count) {
            count = (stacks.containsKey(stones) ? stacks.get(stones) : 0) + count;
            if (count == 0) stacks.remove(stones);
            else stacks.put(stones, count);
        }
    }

}
