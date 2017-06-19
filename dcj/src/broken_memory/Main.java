package broken_memory;

public class Main {
    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        
        int prevNode = myNodeId == 0 ? nodes - 1 : myNodeId - 1;
        int nextNode = (myNodeId + 1) % nodes;
        
        long[] candidates = new long[2];
        int c_i = 0;
        
        long answer = 0;
        
        long L = broken_memory.GetLength();
        
        for (long i = 0; i < L; i++) {
            message.PutLL(nextNode, broken_memory.GetValue(i));
        }
        message.Send(nextNode);
        
        message.Receive(prevNode);
        for (long i = 0; i < L; i++) {
            long v = message.GetLL(prevNode);
            if (v != broken_memory.GetValue(i)) {
                candidates[c_i++] = i;
            }
        }


        for (long i = 0; i < L; i++) {
            message.PutLL(prevNode, broken_memory.GetValue(i));
        }
        message.Send(prevNode);

        message.Receive(nextNode);
        for (long i = 0; i < L; i++) {
            long v = message.GetLL(nextNode);
            if (v != broken_memory.GetValue(i)) {
                if (i == candidates[0] || i == candidates[1]) {
                    answer = i;
                }
            }
        }
        
        if (myNodeId != 0) {
            message.PutLL(0, answer);
            message.Send(0);
        } else {
            
            long[] answers = new long[nodes];
            answers[0] = answer;
            
            for (int node = 1; node < nodes; node++) {
                message.Receive(node);
                answers[node] = message.GetLL(node);
            }

            for (int i = 0; i < answers.length; i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(answers[i]);
            }
            System.out.println();
        }
    }
}
