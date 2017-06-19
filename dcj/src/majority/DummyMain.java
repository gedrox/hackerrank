package majority;

import java.util.HashMap;
import java.util.HashSet;

public class DummyMain {

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();

        long N = majority.GetN();
        long start = myNodeId * N / nodes;
        long end = (myNodeId + 1) * N / nodes;
        long C = end - start;

        HashMap<Integer, Integer> count = new HashMap<>();
        for (long i = start; i < end; i++) {
            int vote = (int) majority.GetVote(i);
            Integer old = count.get(vote);
            count.put(vote, old == null ? 1 : old + 1);
        }
        
        int majority = -1;
        for (int vote : count.keySet()) {
            if (count.get(vote) > C / 2) {
                majority = vote;
                break;
            }
        }
        
        if (myNodeId == 0) {

            HashSet<Integer> majors = new HashSet<>();
            if (majority != -1) {
                majors.add(majority);
            }
            
            // receive majorities
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                int maj_i = message.GetInt(i);
                if (maj_i != -1) {
                    majors.add(maj_i);
                }
            }
            
            Integer[] m = majors.toArray(new Integer[majors.size()]);
            int[] total = new int[m.length];
            
            // publish possible majorities
            for (int i = 1; i < nodes; i++) {
                message.PutInt(i, m.length);
                for (int major : m) {
                    message.PutInt(i, major);
                }
                message.Send(i);
            }

            for (int i = 0; i < m.length; i++) {
                Integer val = count.get(m[i]);
                total[i] = val == null ? 0 : val;
            }
            
            // get counts
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                for (int i1 = 0; i1 < m.length; i1++) {
                    total[i1] += message.GetInt(i);
                }
            }

            for (int i = 0; i < total.length; i++) {
                if (total[i] > N / 2) {
                    System.out.println(m[i]);
                    return;
                }
            }

            System.out.println("NO WINNER");
            
        } else {
            // send majority
            message.PutInt(0, majority);
            message.Send(0);
            
            // get majorities
            message.Receive(0);
            int majCount = message.GetInt(0);
            int[] m = new int[majCount];
            for (int i = 0; i < majCount; i++) {
                m[i] = message.GetInt(0);
                Integer value = count.get(m[i]);
                message.PutInt(0, value == null ? 0 : value);
            }
            
            // send counts
            message.Send(0);
        }

    }

}
