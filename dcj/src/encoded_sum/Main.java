package encoded_sum;

import java.math.BigInteger;
import java.util.*;

public class Main {

    public static final int DIGIT_COUNT = 2;
    static int MOD = 1000000007;
    static BigInteger BIG_MOD = BigInteger.valueOf(MOD);
    public static final long INVERSE_TEN = BigInteger.TEN.modInverse(BIG_MOD).longValue();

    static class encoded_sum_TEST {
        public static long GetLength() {
            return 3L;
        }

        public static char GetScrollOne(long i) {
            switch ((int)i) {
                case 0: return 'B';
                case 1: return 'A';
                case 2: return 'A';
                default: throw new IllegalArgumentException("Invalid argument");
            }
        }

        public static char GetScrollTwo(long i) {
            switch ((int)i) {
                case 0: return 'A';
                case 1: return 'B';
                case 2: return 'A';
                default: throw new IllegalArgumentException("Invalid argument");
            }
        }
    }

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        
        if (myNodeId >= nodes) return;

        long L = encoded_sum.GetLength();
        long start = L * myNodeId / nodes;
        long end = L * (myNodeId + 1) / nodes;

        HashSet<Integer>[] s = new HashSet[DIGIT_COUNT];
        int[] pos = new int[DIGIT_COUNT];
        for (int i = 0; i < DIGIT_COUNT; i++) {
            s[i] = new HashSet<>();
            s[0].add(i);
            pos[i] = 0;
        }
        
        for (long i = start; i < end; i++) {
            int c1 = encoded_sum.GetScrollOne(i) - 'A';
            int c2 = encoded_sum.GetScrollTwo(i) - 'A';

            // move together
            if (c1 == c2) {
                move(s, pos, c1);
            } else if (pos[c1] == pos[c2]) {
                move(s, pos, c1, c2);
            } else {
                // move separately
                move(s, pos, c1);
                move(s, pos, c2);
            }
        }

        for (int dig = DIGIT_COUNT - 1; dig >= 0; dig--) {
            ArrayList<Integer> remaining;
            if (myNodeId == 0) {
                int locate = dig;
                while (s[locate].isEmpty()) {
                    locate--;
                }
                remaining = new ArrayList<>(s[locate]);

                if (nodes > 1) {
                    sendRemaining(remaining);
                    remaining = receiveRemaining();
                }

                int pick = remaining.get(0);
                move(s, pos, pick);
            } else {
                remaining = receiveRemaining();

                if (remaining.size() > 1) {
                    int maxPos = -1;
                    for (int c : remaining) {
                        if (pos[c] > maxPos) maxPos = pos[c];
                    }

                    for (Iterator<Integer> it = remaining.iterator(); it.hasNext(); ) {
                        int c = it.next();
                        if (pos[c] < maxPos) {
                            it.remove();
                        }
                    }
                }

                sendRemaining(remaining);
            }
        }

        // share mapping
        {
            final long all;
            
            // receive
            if (myNodeId != 0) {
                int nodeIdBits = myNodeId;
                int first1 = 0;
                while (nodeIdBits % 2 == 0) {
                    nodeIdBits /= 2;
                    first1++;
                }
                int source = myNodeId ^ (1 << first1);
                
                message.Receive(source);
                all = message.GetLL(source);
                long mutableAll = all;
                for (int i = DIGIT_COUNT - 1; i >= 0; i--) {
                    pos[i] = (int) (mutableAll % 10);
                    mutableAll /= 10;
                }
            } else {
                long mutableAll = 1;
                for (int i = 0; i < DIGIT_COUNT; i++) {
                    mutableAll = 10 * mutableAll + pos[i];
                }
                all = mutableAll;
            }
            
            // notify
            int zeroPos = 0;
            int nodeIdBits = myNodeId;
            while (nodeIdBits % 2 == 0) {
                int target = myNodeId | (1 << zeroPos);
                if (target < nodes) {
                    message.PutLL(target, all);
                    message.Send(target);
                } else {
                    break;
                }
                nodeIdBits /= 2;
                zeroPos++;
            }
        }

        // calculate the local sum
        long sum = 0;
        long rem = BigInteger.TEN.modPow(BigInteger.valueOf(L - 1 - start), BIG_MOD).longValue();
        for (long i = start; i < end; i++) {
            int c1 = encoded_sum.GetScrollOne(i) - 'A' + (10 - DIGIT_COUNT);
            int c2 = encoded_sum.GetScrollTwo(i) - 'A' + (10 - DIGIT_COUNT);

            sum += (pos[c1] + pos[c2]) * rem;
            sum %= MOD;

            rem *= INVERSE_TEN;
            rem %= MOD;
        }

        // sum
        {
            int nodeIdBits = myNodeId;
            int i = 0;
            while (nodeIdBits % 2 == 0) {
                int source = myNodeId | (1 << i);
                if (source < nodes) {
                    message.Receive(source);
                    sum += message.GetLL(source);
                } else {
                    break;
                }

                nodeIdBits /= 2;
                i++;
            }

            if (nodeIdBits == 0) {
                System.out.println(sum % MOD);
                return;
            }
            
            while (nodeIdBits % 2 != 1) {
                nodeIdBits /= 2;
                i++;
            }
            
            int target = myNodeId ^ (1 << i);
            message.PutLL(target, sum);
            message.Send(target);
        }
    }

    private static ArrayList<Integer> receiveRemaining() {
        ArrayList<Integer> remaining = new ArrayList<>();
        int prevNode = message.MyNodeId() == 0 ? message.NumberOfNodes() - 1 : message.MyNodeId() - 1;
        message.Receive(prevNode);
        int len = message.GetInt(prevNode);
        for (int i = 0; i < len; i++) {
            remaining.add(message.GetInt(prevNode));
        }
        return remaining;
    }

    private static void sendRemaining(Collection<Integer> remaining) {
        int nextNode = (message.MyNodeId() + 1) % message.NumberOfNodes();
        message.PutInt(nextNode, remaining.size());
        for (int c : remaining) {
            message.PutInt(nextNode, c);
        }
        message.Send(nextNode);
    }

    private static void move(HashSet<Integer>[] s, int[] pos, int... chars) {
        HashSet<Integer> oldPlace = s[pos[chars[0]]];
        int size = oldPlace.size();
        if (size > chars.length) {
            int newPos = pos[chars[0]] + size - chars.length;
            HashSet<Integer> newPlace = s[newPos];
            for (int c : chars) {
                oldPlace.remove(c);
                newPlace.add(c);
                pos[c] = newPos;
            }
        }
    }
}
