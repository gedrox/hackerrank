package oops;

public class Main {

    public static void main(String[] args) {
        long start = message.MyNodeId() * oops.GetN() / message.NumberOfNodes();
        long end = (message.MyNodeId() + 1) * oops.GetN() / message.NumberOfNodes();
        
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (long i = start; i < end; i++) {
            long number = oops.GetNumber(i);
            if (number < min) {
                min = number;
            }
            if (number > max) {
                max = number;
            }
        }
        
        if (message.MyNodeId() == 0) {
            for (int i = 1; i < message.NumberOfNodes(); i++) {
                message.Receive(i);
                long minCand = message.GetLL(i);
                long maxCand = message.GetLL(i);
                
                if (minCand < min) {
                    min = minCand;
                }
                if (maxCand > max) {
                    max = maxCand;
                }
            }

            System.out.println(max - min);
        } else {
            message.PutLL(0, min);
            message.PutLL(0, max);
            message.Send(0);
        }
    }
}
