package air_show;

public class Main {

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();

        long N = air_show.GetNumSegments();
        
        long start = N * myNodeId / nodes;
        long end = N * (myNodeId + 1) / nodes;
        
        long[] times = new long[(int) (end - start)];
        long localZero = 0;
        long total = 0;
        for (long i = start; i < end; i++) {
            total += air_show.GetTime(0, i);
        }
        
        if (myNodeId == 0) {
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                long sum = message.GetLL(i);
                
                message.PutLL(i, total);
                message.Send(i);
                total += sum;
            }
        } else {
            message.PutLL(0, total);
            message.Send(0);
            
            message.Receive(0);
            localZero = message.GetLL(0);
        }
    }

}
