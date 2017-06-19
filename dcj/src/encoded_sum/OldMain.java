package encoded_sum;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class OldMain {

    static int MOD = 1000000007;
    static BigInteger BIG_MOD = BigInteger.valueOf(MOD);
    public static final long INVERSE_TEN = BigInteger.TEN.modInverse(BIG_MOD).longValue();

    static class encoded_sum {
        public static long GetLength() { return (long) 1e9; }
        public static char GetScrollOne(long i) { return "AB".charAt((int) (i % 2)); }
        public static char GetScrollTwo(long i) { return "BA".charAt((int) (i % 2)); }
    }

    private static int nodeCount() {
        return Math.min(1000, message.NumberOfNodes());
    }

    public static void main(String[] args) {
        
//        long t0 = System.currentTimeMillis();
        
        int nodes = nodeCount();
        int myNodeId = message.MyNodeId();
        
        if (myNodeId >= nodes) return;

        long L = encoded_sum.GetLength();
        long start = L * myNodeId / nodes;
        long end = L * (myNodeId + 1) / nodes;

        HashSet<Integer>[] s = new HashSet[10];
        int[] pos = new int[10];
        for (int i = 0; i < 10; i++) {
            s[i] = new HashSet<>();
            s[0].add(i);
            pos[i] = 0;
            
            // TODO: wrong!!!
            pos[i] = i;
        }
        
        /*for (long i = start; i < end; i++) {
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
        }*/

//        System.err.println("init: " + (System.currentTimeMillis() - t0)); t0 = System.currentTimeMillis();

//        System.err.println(Arrays.toString(pos));

        /*for (int dig = 9; dig >= 0; dig--) {
//            System.err.println("dig: " + dig);
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

//        System.err.println("rem: " + (System.currentTimeMillis() - t0)); 
//        t0 = System.currentTimeMillis();

        // share mapping
        {
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
                long all = message.GetLL(source);
                for (int i = 9; i >= 0; i--) {
                    pos[i] = (int) (all % 10);
                    all /= 10;
                }
            }

            long all = 1;
            for (int i = 0; i < 10; i++) {
                all = 10 * all + pos[i];
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
        }*/
        
//        if (myNodeId == 0) {
//            long all = 1;
//            for (int i = 0; i < 10; i++) {
//                all = 10 * all + pos[i];
//            }
//            
//            // notify on mapping
//            for (int target = 1; target < nodes; target++) {
//                message.PutLL(target, all);
//                message.Send(target);
//            }
//        } else {
//            // read mapping
//            message.Receive(0);
//            long all = message.GetLL(0);
//            for (int i = 9; i >= 0; i--) {
//                pos[i] = (int) (all % 10);
//                all /= 10;
//            }
//        }

//        System.err.println("map: " + (System.currentTimeMillis() - t0)); t0 = System.currentTimeMillis();

        // calculate the local sum
        long sum = 0;
        long rem = BigInteger.TEN.modPow(BigInteger.valueOf(L - 1 - start), BIG_MOD).longValue();
        for (long i = start; i < end; i++) {
            int c1 = encoded_sum.GetScrollOne(i) - 'A';
            int c2 = encoded_sum.GetScrollTwo(i) - 'A';

            sum += (pos[c1] + pos[c2]) * rem;
            sum %= MOD;

            rem *= INVERSE_TEN;
            rem %= MOD;
        }

//        if (true) {
//            if (myNodeId == message.NumberOfNodes() - 1) System.out.println(0);
//            return;
//        }
        
//        if (true) return;

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

            if (nodeIdBits % 2 == 1) {
                int target = myNodeId ^ (1 << i);
                message.PutLL(target, sum);
                message.Send(target);
            } else {
                System.out.println(sum % MOD);
            }
        }
        
        
//        if (myNodeId == 0) {
//            // final sum
//            for (int source = 1; source < nodes; source++) {
//                message.Receive(source);
//                sum += message.GetLL(source);
//            }
////            System.err.println("sum " + (System.currentTimeMillis() - t0)); t0 = System.currentTimeMillis();
//
//            System.out.println(sum % MOD);
//        } else {
//            message.PutLL(0, sum);
//            message.Send(0);
////            System.err.println("sum " + (System.currentTimeMillis() - t0)); t0 = System.currentTimeMillis();
//        }

    }

    private static ArrayList<Integer> receiveRemaining() {
        ArrayList<Integer> remaining = new ArrayList<>();
        int prevNode = message.MyNodeId() == 0 ? nodeCount() - 1 : message.MyNodeId() - 1;
        message.Receive(prevNode);
        int len = message.GetInt(prevNode);
        for (int i = 0; i < len; i++) {
            remaining.add(message.GetInt(prevNode));
        }
//        System.err.println("Receive: " + remaining);
        return remaining;
    }

    private static void sendRemaining(Collection<Integer> remaining) {
        int nextNode = (message.MyNodeId() + 1) % nodeCount();
        message.PutInt(nextNode, remaining.size());
        for (int c : remaining) {
            message.PutInt(nextNode, c);
        }
        message.Send(nextNode);
//        System.err.println("Send: " + remaining);
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
