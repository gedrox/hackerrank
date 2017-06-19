package almost_sorted;

import java.util.Arrays;

public class Main {

    static long MOD = 1 << 20;
    
    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();

        long files = almost_sorted.NumberOfFiles();
        long dist = almost_sorted.MaxDistance();
        
        long start = myNodeId * files / nodes;
        long end = (myNodeId + 1) * files / nodes;
        
        long originalStart = start;
        long len = end - start;
        
        start = Math.max(0, start - dist);
        end = Math.min(end + dist, files);
        
        long[] ids = new long[(int) (end - start)];
        for (long i = start; i < end; i++) {
            ids[(int) (i - start)] = almost_sorted.Identifier(i);
        }
        Arrays.sort(ids);
        
        long sum = 0;
        for (long i = originalStart; i < originalStart + len; i++) {
            sum += (i % MOD) * (ids[(int) (i - start)] % MOD);
            sum %= MOD;
        }
        
        if (myNodeId == 0) {
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                sum += message.GetLL(i);
                sum %= MOD;
            }
            System.out.println(sum);
        } else {
            message.PutLL(0, sum);
            message.Send(0);
        }
    }

}
