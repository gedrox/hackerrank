package query_of_death;

import java.util.HashSet;

public class Main {

    public static final int MEMORY_CHECK = 20;

    public static void main(String[] args) {

        HashSet<Integer> brokenNodes = new HashSet<>();
        brokenNodes.add(0);

        int myNodeId = message.MyNodeId();
        int numberOfNodes = message.NumberOfNodes();
        int okNodes = numberOfNodes - 1;
        int myNodeIdChunk = myNodeId - 1;

        long chunkStart = 0;
        long chunkEnd = query_of_death.GetLength();
        long provenSum = 0;

        Request[] requests = new Request[100];

        int requestId = 0;

        if (myNodeId == 0) {
            long totalSum = 0;
            for (int node = 1; node < numberOfNodes; node++) {
                message.Receive(node);
                totalSum += message.GetLL(node);
            }
            System.out.println(totalSum);
            return;
        }

        while (true) {

            long chunkSize = chunkEnd - chunkStart;
            long start = chunkStart + myNodeIdChunk * chunkSize / okNodes;
            long end = chunkStart + (myNodeIdChunk + 1) * chunkSize / okNodes;

//            System.err.println("SUM: " + start + ", " + end);

            long memory = -1;

            long sum = 0;
            for (long i = start; i < end; i++) {
                long val = query_of_death.GetValue(i);
                if (i == start) memory = val;
                sum += val;
            }

            if (chunkSize <= okNodes) {
                provenSum += sum;
                break;
            }

            boolean broken = false;

            if (end > start) {
                for (int x = 0; x < MEMORY_CHECK; x++) {
                    long val = query_of_death.GetValue(start);
                    if (val != memory) {
                        broken = true;
                        break;
                    }
                }
            }

            if (broken) {
                brokenNodes.add(myNodeId);

//                System.err.println("Broken, sending " + start + ", " + end);

                for (int node = 0; node < numberOfNodes; node++) {
                    // inform
                    if (!brokenNodes.contains(node)) {
                        message.PutInt(node, requestId);
                        message.PutLL(node, start);
                        message.PutLL(node, end);
                        message.Send(node);
                    }
                }
                // die
                break;
            } else {

                provenSum += sum;

                while (requests[requestId] == null) {
                    Request req = new Request();

                    int brokenOne = req.broken = message.Receive(-1);
                    int req_id = message.GetInt(brokenOne);
//                    System.err.println("Received req ID " + req_id);
                    requests[req_id] = req;
                    req.start = message.GetLL(brokenOne);
                    req.end = message.GetLL(brokenOne);
                }

                okNodes--;
                if (requests[requestId].broken < myNodeId) {
                    myNodeIdChunk--;
                }
                brokenNodes.add(requests[requestId].broken);
                chunkStart = requests[requestId].start;
                chunkEnd = requests[requestId].end;
            }

            requestId++;
        }

//        System.err.println("Put 0 " + provenSum);
        message.PutLL(0, provenSum);
        message.Send(0);
    }

    static class Request {
        int broken;
        long start;
        long end;
    }

}
