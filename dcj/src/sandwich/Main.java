package sandwich;

import java.util.TreeSet;

public class Main {
    
    static class sandwich2 {
        public static long GetN() {
            return (long) 1e7;
        }
        public static long GetTaste(long index) {
            return (long) 1e9;
        }
    }
    
    public static void main(String[] args) {
        int nodes = Math.min(4, message.NumberOfNodes());
        int myNodeId = message.MyNodeId();
        if (myNodeId >= nodes) return;

        long N = sandwich.GetN();
        long start = myNodeId * N / nodes;
        long end = (myNodeId + 1) * N / nodes;
        
        long x = 0, min = 0, max = 0, min2 = 0, max2 = 0;
        for (long i = start; i < end; i++) {
            long taste = sandwich.GetTaste(i);
            x += taste;
            if (x < min2) {
                min2 = x;
            }
            if (x < min) {
                min = x;
                max2 = max;
            }
            if (x > max) {
                max = x;
                min2 = max;
            }
        }
        
        long bestDrop = 0;

//        System.err.printf("x: %d, min: %d, max: %d, min2: %d, max2: %d%n", x, min, max, min2, max2);
        
        // receive
        if (myNodeId != 0) {
            message.Receive(myNodeId - 1);
            long prevX = message.GetLL(myNodeId - 1);
            long prevMin = message.GetLL(myNodeId - 1);
            long prevMax = message.GetLL(myNodeId - 1);
            long prevMin2 = message.GetLL(myNodeId - 1);
            long prevMax2 = message.GetLL(myNodeId - 1);
            
            x = prevX + x;
            if (prevX + min2 < prevMin2) {
                min2 = prevX + min2;
            } else {
                min2 = prevMin2;
            }
            if (prevX + min < prevMin) {
                min = min2 = prevX + min;
                max2 = Math.max(prevMax2, prevX + max2);
            } else {
                min = prevMin;
            }
            
            if (prevX + max > prevMax) {
                max = prevX + max;
                min2 = max;
            } else {
                max = prevMax;
            }

//            System.err.printf("x: %d, min: %d, max: %d, min2: %d, max2: %d%n", x, min, max, min2, max2);
        } else {
//            bestDrop = 
        }
        
        if (myNodeId != nodes - 1) {
            message.PutLL(myNodeId + 1, x);
            message.PutLL(myNodeId + 1, min);
            message.PutLL(myNodeId + 1, max);
            message.PutLL(myNodeId + 1, min2);
            message.PutLL(myNodeId + 1, max2);
            message.Send(myNodeId + 1);
        } else {

            TreeSet<Long> candidates = new TreeSet<>();
            // don't eat at all
            candidates.add(0L);
            // eat all
            candidates.add(x);
            
            candidates.add(max);
            candidates.add(max + (x - min2));
            
            candidates.add(x - min);
            candidates.add(x - min + (max2));

            System.out.println(candidates.last());
        }
    }
}