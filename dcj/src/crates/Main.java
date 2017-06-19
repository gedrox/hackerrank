package crates;

import java.math.BigInteger;
import java.util.ArrayList;

public class Main {
    public static final BigInteger MAX_LL = BigInteger.valueOf(Long.MAX_VALUE);
    public static int MASTER = 0;
    private static final int MOD = 1000000007;
//    private static final BigInteger BIG_MOD = BigInteger.valueOf(1000000007);

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        long n = crates.GetNumStacks();
        
        if (n < 2) {
            if (myNodeId == MASTER) {
                System.out.println(0);
            }
            return;
        }
        
        long start = myNodeId * n / nodes;
        long end = (myNodeId + 1) * n / nodes;
        
        // sum
        long localSum = 0;
        for (long i = start; i < end; i++) {
            localSum += crates.GetStackHeight(i + 1);
        }
        log("Sum: " + localSum);
        
        if (myNodeId != MASTER) {
            message.PutLL(MASTER, localSum);
            message.Send(MASTER);
        }

        long small;
        long smallStarts;
        
        // total sum
        if (myNodeId == MASTER) {
            long s = localSum;
            for (int nodeId = 1; nodeId < nodes; nodeId++) {
                message.Receive(nodeId);
                s += message.GetLL(nodeId);
            }

            small = s / n;
            smallStarts = s % n;

            log("Small: " + small);
            log("Small starts: " + smallStarts);

            for (int nodeId = 1; nodeId < nodes; nodeId++) {
                message.PutLL(nodeId, small);
                message.PutLL(nodeId, smallStarts);
                message.Send(nodeId);
            }
        } else {
            message.Receive(MASTER);
            small = message.GetLL(MASTER);
            smallStarts = message.GetLL(MASTER);
        }

        {
            long remainder = 0;
            if (myNodeId > 0) {
                message.Receive(myNodeId - 1);
                remainder = message.GetLL(myNodeId - 1);
            }
            
            long newRemainder = remainder + localSum;
            if (smallStarts >= end) {
                newRemainder -= (small + 1) * (end - start);
            } else {
                newRemainder -= small * (end - start);
                if (smallStarts > start) {
                    newRemainder -= smallStarts - start;
                }
            }

            log("Rem: " + newRemainder);

            if (myNodeId + 1 < nodes) {
                message.PutLL(myNodeId + 1, newRemainder);
                message.Send(myNodeId + 1);
            } else {
                if (newRemainder != 0) {
                    throw new RuntimeException("PROBLEM!");
                }
            }

            ArrayList<Items> pos = new ArrayList<>();
            ArrayList<Items> neg = new ArrayList<>();
            
            for (long i = start; i < end; i++) {
                long count = crates.GetStackHeight(i + 1);
                if (i == start) {
                    count += remainder;
                }
                count -= i >= smallStarts ? small : small + 1;

                if (count < 0) {
                    neg.add(new Items(i, count));
                } else if (count > 0) {
                    pos.add(new Items(i, count));
                }
            }
            
            if (newRemainder < 0) {
                pos.add(new Items(end, -newRemainder));
            } else if (newRemainder > 0) {
                neg.add(new Items(end, -newRemainder));
            }

            log(pos);
            log(neg);
            
            int posIdx = 0, negIdx = 0;
            long moves = 0;
            while (posIdx < pos.size() || negIdx < neg.size()) {
                Items posObj = pos.get(posIdx);
                Items negObj = neg.get(negIdx);
                
                long min = Math.min(posObj.count, -negObj.count);
                long dist = Math.abs(posObj.i - negObj.i);
                
                moves += ((min % MOD) * (dist % MOD)) % MOD;
                moves %= MOD;
                
                posObj.count -= min;
                if (posObj.count == 0) {
                    posIdx++;
                }
                negObj.count += min;
                if (negObj.count == 0) {
                    negIdx++;
                }
            }

            log("Moves: " + moves);

            message.PutLL(MASTER, moves);
            message.Send(MASTER);
        }
        
        if (myNodeId == MASTER) {
            long totalMoves = 0;
            for (int nodeId = 0; nodeId < nodes; nodeId++) {
                message.Receive(nodeId);
                long nodeMoves = message.GetLL(nodeId);
                
                totalMoves += nodeMoves;
                totalMoves %= MOD;
            }
            System.out.println(totalMoves);
        }
    }

    private static void log(Object x) {
        //System.err.println(x);
    }

    private static void PutBigInteger(int nodeId, BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.ZERO) < 0) {
            bigInteger = bigInteger.abs();
            message.PutChar(nodeId, '-');
        } else {
            message.PutChar(nodeId, '+');
        }
        message.PutLL(nodeId, bigInteger.divide(MAX_LL).longValue());
        message.PutLL(nodeId, bigInteger.mod(MAX_LL).longValue());
        message.Send(nodeId);
    }

    private static BigInteger GetBigInteger(int nodeId) {
        message.Receive(nodeId);
        char sign = message.GetChar(nodeId);
        long a = message.GetLL(nodeId);
        long b = message.GetLL(nodeId);
        return BigInteger.valueOf(a).multiply(MAX_LL).add(BigInteger.valueOf(b)).multiply(sign == '+' ? BigInteger.ONE : BigInteger.valueOf(-1));
    }
    
    static class Items {
        long i;
        long count;

        public Items(long i, long count) {
            this.i = i;
            this.count = count;
        }

        @Override
        public String toString() {
            return "@" + i + "=" + count;
        }
    }
}
