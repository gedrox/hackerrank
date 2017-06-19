package todd_and_steven;

public class Main {

    static int MOD = 1000000007;

    public static void main(String[] args) {

        int myNodeId = message.MyNodeId();
        int numberOfNodes = message.NumberOfNodes();
        
        long T = todd_and_steven.GetToddLength();
        long S = todd_and_steven.GetStevenLength();

        // must find this end
        long start = myNodeId * (T + S) / numberOfNodes;
        long end = (myNodeId + 1) * (T + S) / numberOfNodes;
        
        long endT;
        long endS;
        if (myNodeId != numberOfNodes - 1) {
            
            long low = Math.max(0, end - S);
            long high = Math.min(end, T);
            
            long mid;
            while (true) {
                
                mid = (low + high) / 2;
                long midS = end - mid;
                
                long prevT = mid == 0 ? Long.MIN_VALUE : todd_and_steven.GetToddValue(mid - 1);
                long nextT = mid == T ? Long.MAX_VALUE : todd_and_steven.GetToddValue(mid);

                long prevS = midS == 0 ? Long.MIN_VALUE : todd_and_steven.GetStevenValue(midS - 1);
                long nextS = midS == S ? Long.MAX_VALUE : todd_and_steven.GetStevenValue(midS);
                
                if (nextS < prevT) {
                    high = mid - 1;
                } else if (nextT < prevS) {
                    low = mid + 1;
                } else {
                    break;
                }
            }

//            System.err.println(mid);

            endT = mid;
            endS = end - mid;
            
        } else {
            endT = T;
            endS = S;
        }
        
        if (myNodeId != numberOfNodes - 1) {
            int nextNode = myNodeId + 1;
            message.PutLL(nextNode, endT);
            message.PutLL(nextNode, endS);
            message.Send(nextNode);
        }
        
        long startT = 0;
        long startS = 0;
        
        if (myNodeId != 0) {
            int prevNode = myNodeId - 1;
            message.Receive(prevNode);
            startT = message.GetLL(prevNode);
            startS = message.GetLL(prevNode);
        }

//        System.err.printf("%d-%d and %d-%d%n", startT, endT, startS, endS);
        
        long iT = startT;
        long iS = startS;
        long sum = 0;
        for (long i = start; i < end; i++) {
            
            long valT = iT < endT ? todd_and_steven.GetToddValue(iT) : Long.MAX_VALUE;
            long valS = iS < endS ? todd_and_steven.GetStevenValue(iS) : Long.MAX_VALUE;
            
            if (valT < valS) {
                sum += i ^ valT;
                sum %= MOD;
                iT++;
            } else {
                sum += i ^ valS;
                sum %= MOD;
                iS++;
            }
        }
        
        // receive
        long totalSum = 0;
        if (myNodeId != 0) {
            int prevNode = myNodeId - 1;
            message.Receive(prevNode);
            totalSum = message.GetLL(prevNode);
        }
        totalSum += sum;
        totalSum %= MOD;
        
        if (myNodeId != numberOfNodes - 1) {
            int nextNode = myNodeId + 1;
            message.PutLL(nextNode, totalSum);
            message.Send(nextNode);
        } else {
            System.out.println(totalSum);
        }
    }
}
