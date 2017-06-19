package winning_move;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        long n = winning_move.GetNumPlayers();

        long start = myNodeId * n / nodes;
        long end = (myNodeId + 1) * n / nodes;
        
        log(start + " " + end);
        
        // todo: order locally
        ArrayList<Long> data = new ArrayList<>((int) (end - start));
        for (int i = 0; i < end - start; i++) {
            data.add(winning_move.GetSubmission(start + i));
        }
        Collections.sort(data);
        
        for (int i = data.size() - 1; i > 0; i--) {
            if (Math.abs(data.get(i)) == Math.abs(data.get(i - 1))) {
                data.remove(i);
                data.set(i - 1, -data.get(i - 1));
            }
        }

        int rem = myNodeId;
        int step = 1;
        
        int bucketCount = 1;
        
        // receive
        while (rem % 2 == 1) {
            int source = myNodeId - step;
            log("receive: " + source);
            message.Receive(source);
            bucketCount += message.GetInt(source);

            data = Receive(source, data);
            
            rem /= 2;
            step *= 2;
        }
        
        // send
        int target = myNodeId + step;
        if (target >= nodes) {
            target = nodes - 1;
        }
        if (target != myNodeId) {
            log("send: " + target);
            message.PutInt(target, bucketCount);
            message.PutInt(target, data.size());
            for (Long datum : data) {
                message.PutLL(target, datum);
            }
            message.Send(target);
        }
        
        // last node receives everything
        if (myNodeId == nodes - 1) {
            while (bucketCount != nodes) {
                log("receive: any");
                int source = message.Receive(-1);
                log("receive: " + source);
                bucketCount += message.GetInt(source);
                data = Receive(source, data);
            }

            for (Long datum : data) {
                if (datum > 0) {
                    System.out.println(datum);
                    return;
                }
            }
            System.out.println(0);
        }
    }

    private static ArrayList<Long> Receive(int source, ArrayList<Long> data) {
        int ind = 0;
        int len = message.GetInt(source);
        ArrayList<Long> newData = new ArrayList<>(len + data.size());
        for (int i = 0; i < len; i++) {
            long next = message.GetLL(source);
            while (ind < data.size() && Math.abs(next) > Math.abs(data.get(ind))) {
                newData.add(data.get(ind));
                ind++;
            }
            
            if (ind < data.size() && Math.abs(next) == Math.abs(data.get(ind))) {
                newData.add(-Math.abs(data.get(ind)));
                ind++;
            } else {
                newData.add(next);
            }
        }

        while (ind < data.size()) {
            newData.add(data.get(ind));
            ind++;
        }

        data = newData;
        return data;
    }


    private static void log(Object x) {
//        if (message.MyNodeId() == 0) 
        System.err.println(x);
    }
}

