package query_of_death;

public class OldMain {

    public static void main(String[] args) {

        int myNodeId = message.MyNodeId();
        int numberOfNodes = message.NumberOfNodes();

        long start = myNodeId * query_of_death.GetLength() / numberOfNodes;
        long end = (myNodeId + 1) * query_of_death.GetLength() / numberOfNodes;
        
        long sum = 0;
        long brokenIndex = -1;

        summing:
        for (long i = start; i < end; i++) {
            long val = query_of_death.GetValue(i);
            sum += val;
            for (int x = 0; x < 10; x++) {
                if (val != query_of_death.GetValue(i)) {
                    brokenIndex = i;
                    break summing;
                }
            }
        }
        
        long totalSum = sum;
        
        if (myNodeId != 0) {
            int prevNode = myNodeId - 1;
            totalSum = addPrevSum(totalSum, prevNode);
        }
        
        // send next
        int nextNode = (myNodeId + 1) % numberOfNodes;
//        System.err.println("Sent to " + nextNode);
        message.PutLL(nextNode, totalSum);
        message.PutLL(nextNode, brokenIndex);
        message.Send(nextNode);
        
        if (myNodeId == 0) {
            int prevNode = numberOfNodes - 1;
            totalSum = addPrevSum(0, prevNode);

            System.out.println(totalSum);
        }
    }

    private static long addPrevSum(long totalSum, int prevNode) {
//        System.err.println("Wait for " + prevNode);
        message.Receive(prevNode);
        totalSum += message.GetLL(prevNode);
        long prevBroken = message.GetLL(prevNode);

        if (prevBroken != -1) {
            long prevEnd = (prevNode + 1) * query_of_death.GetLength() / message.NumberOfNodes();
            for (long i = prevBroken + 1; i < prevEnd; i++) {
                totalSum += query_of_death.GetValue(i);
            }
        }
        return totalSum;
    }
}
