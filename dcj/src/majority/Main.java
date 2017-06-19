package majority;

public class Main {

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();

        long N = majority.GetN();
        long start = myNodeId * N / nodes;
        long end = (myNodeId + 1) * N / nodes;

        int x = -1, cnt = 0;
        for (long i = start; i < end; i++) {
            int vote = (int) majority.GetVote(i);
            if (cnt == 0) {
                x = vote;
                cnt = 1;
            } else if (vote == x) cnt++; 
            else cnt--;
        }
        
        if (myNodeId > 0) {
            message.Receive(myNodeId - 1);
            int prevX = message.GetInt(myNodeId - 1);
            int prevCnt = message.GetInt(myNodeId - 1);

            if (x == prevX) cnt += prevCnt;
            else if (prevCnt > cnt) {
                x = prevX;
                cnt = prevCnt - cnt;
            } else {
                cnt -= prevCnt;
            }
        }
        
        if (myNodeId != nodes - 1) {
            message.PutInt(myNodeId + 1, x);
            message.PutInt(myNodeId + 1, cnt);
            message.Send(myNodeId + 1);
            
            message.Receive(nodes - 1);
            x = message.GetInt(nodes - 1);
        } else {
            for (int i = 0; i < nodes - 1; i++) {
                message.PutInt(i, x);
                message.Send(i);
            }
        }

        int sum = 0;
        for (long i = start; i < end; i++) {
            int vote = (int) majority.GetVote(i);
            if (vote == x) sum++;
        }

        if (myNodeId == 0) {
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                sum += message.GetInt(i);
            }
            
            if (sum > N / 2) {
                System.out.println(x);
            } else {
                System.out.println("NO WINNER");
            }
        } else {
            message.PutInt(0, sum);
            message.Send(0);
        }
    }

}
