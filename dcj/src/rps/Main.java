package rps;

public class Main {
    public static final Function<Long, Character> PROVIDER = new Function<Long, Character>() {
        @Override
        public Character apply(Long aLong) {
            return rps.GetFavoriteMove(aLong);
        }
    };
    public static int MASTER = 0;

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        long n = rps.GetN();
        
        int limit = 9;
        int finalCount = 1 << limit;
        
        if (n <= limit) {
            if (myNodeId != MASTER) {
                log("leaving");
                return;
            }

            Player w = play(0, 1 << n, PROVIDER);
            System.out.println(w.i);
        } else {
            
            long chunk = 1 << (n - limit);
            
            for (int i = myNodeId; i < finalCount; i += nodes) {
                long start = chunk * i;
                long end = start + chunk;
                
                Player winner = play(start, end, PROVIDER);
                
                message.PutInt(MASTER, i);
                message.PutLL(MASTER, winner.i);
                message.PutChar(MASTER, winner.move);
                message.Send(MASTER);
                
                log("winner for " + start + "-" + end + ": " + winner.i + " (" + winner.move + ")");
            }
            
            if (myNodeId == MASTER) {
                final Player[] winners = new Player[finalCount];
                for (int i = 0; i < finalCount; i++) {
                    int nodeId = message.Receive(-1);
                    int ind = message.GetInt(nodeId);
                    long playerId = message.GetLL(nodeId);
                    char move = message.GetChar(nodeId);
                    
                    winners[ind] = new Player(playerId, move);
                }

                Player w = play(0, finalCount, new Function<Long, Character>() {
                    @Override
                    public Character apply(Long aLong) {
                        return winners[aLong.intValue()].move;
                    }
                });
                System.out.println(winners[(int) w.i].i);
            }
        }
    }

    private static Player play(long start, long end, Function<Long, Character> provider) {
        long count = end - start;
        if (count == 1) {
            return new Player(start, provider.apply(start));
        } else {
            Player w1 = play(start, start + count / 2, provider);
            Player w2 = play(start + count / 2, end, provider);
            
            Player winner;
            
            if (w2.move == 'S' && w1.move == 'P') winner = w2;
            else if (w2.move == 'P' && w1.move == 'R') winner = w2;
            else if (w2.move == 'R' && w1.move == 'S') winner = w2;
            else winner = w1;
            
            if (end <= 4) log("winner for " + start + "-" + end + ": " + winner.i + " (" + winner.move + ")" + w1 + w2);
            
            return winner;
        }
    }

    private static void log(Object x) {
//        if (message.MyNodeId() == 0) System.err.println(x);
    }

    static class Player {
        long i;
        char move;

        public Player(long i, char move) {
            this.i = i;
            this.move = move;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "i=" + i +
                    ", move=" + move +
                    '}';
        }
    }
    
    interface Function<T, U> {
        U apply(T val);
    }
}
