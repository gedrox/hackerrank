package lisp_plus_plus;

public class Main {

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();

        long start = myNodeId * lisp_plus_plus.GetLength() / nodes;
        long end = (myNodeId + 1) * lisp_plus_plus.GetLength() / nodes;
        
        long res = 0;
        long min = 0;
        
//        long[] pos = new long[(int) (end - start)];
//        int pos_i = 0;
        for (long i = start; i < end; i++) {
            char c = lisp_plus_plus.GetCharacter(i);
            if (c == '(') {
                res++;
            } else {
                res--;
                if (res < min) {
                    min = res;
//                    pos[pos_i++] = i;
                }
            }
        }

        long prevRes = 0;
        if (myNodeId != 0) {
            int source = myNodeId - 1;
            message.Receive(source);
            prevRes = message.GetLL(source);
            
            // stop
            if (prevRes == Long.MIN_VALUE) {
                sendValue(Long.MIN_VALUE);
                return;
            }
            
        }
        
        if (prevRes + min < 0) {

            for (long i = start; i < end; i++) {
                char c = lisp_plus_plus.GetCharacter(i);
                if (c == '(') {
                    prevRes++;
                } else {
                    prevRes--;
                    if (prevRes == -1) {
                        System.out.println(i);
                        sendValue(Long.MIN_VALUE);
                        return;
                    }
                }
            }

//            System.out.println(pos[(int) prevRes]);
//            sendValue(Long.MIN_VALUE);
//            return;
            
        } else {
            res += prevRes;
        }
        
        if (myNodeId == nodes - 1) {
            System.out.println(res == 0 ? -1 : end);
        } else {
            sendValue(res);
        }
    }

    private static void sendValue(long res) {
        if (message.MyNodeId() != message.NumberOfNodes() - 1) {
            message.PutLL(message.MyNodeId() + 1, res);
            message.Send(message.MyNodeId() + 1);
        }
    }

}
