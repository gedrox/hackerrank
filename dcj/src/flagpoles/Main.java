package flagpoles;

public class Main {
    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        
        long L = flagpoles.GetNumFlagpoles();
        
        if (L == 1) {
            if (myNodeId == 0) {
                System.out.println(1);
            }
            return;
        }
        
        if (L == 2) {
            if (myNodeId == 0) {
                System.out.println(2);
            }
            return;
        }
        
        if (nodes > L / 2) {
            nodes = (int) L / 2;
        }
        
        if (myNodeId >= nodes) {
            return;
        }
                
        long start = L * myNodeId / nodes;
        long end = L * (myNodeId + 1) / nodes;

//        System.err.println("start: " + start);
//        System.err.println("end: " + end);
        
        long prevH = flagpoles.GetHeight(start + 1);
        long currD = prevH - flagpoles.GetHeight(start);
        long count = 2;
        long maxCount = 2;
        
        long diffFront = 0, diffBack, countFront = 0, countBack;
        
        for (long i = start + 2; i < end; i++) {
            long nextH = flagpoles.GetHeight(i);
            if (nextH - prevH == currD) {
                count++;
            } else {
                if (countFront == 0) {
                    countFront = count;
                    diffFront = currD;
                }
                maxCount = Math.max(count, maxCount);
                
                // reset
                currD = nextH - prevH;
                count = 2;
            }
            prevH = nextH;
        }
        
        diffBack = currD;
        countBack = count;

        if (countFront == 0) {
            countFront = count;
            diffFront = currD;
        }
        maxCount = Math.max(count, maxCount);
        
        if (myNodeId > 0) {
            message.PutLL(0, maxCount);
            message.PutLL(0, countFront);
            message.PutLL(0, diffFront);
            message.PutLL(0, countBack);
            message.PutLL(0, diffBack);
            message.Send(0);
        } else {
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                long maxCount2 = message.GetLL(i);       
                long countFront2 = message.GetLL(i);       
                long diffFront2 = message.GetLL(i);       
                long countBack2 = message.GetLL(i);       
                long diffBack2 = message.GetLL(i);

                long start2 = L * i / nodes;
                long end2 = L * (i + 1) / nodes;

                long diffAtCon = flagpoles.GetHeight(start2) - flagpoles.GetHeight(start2 - 1);
                
                maxCount = Math.max(maxCount, maxCount2);
                if (diffBack == diffFront2 && diffAtCon == diffBack) {
                    maxCount = Math.max(maxCount, countBack + countFront2);
                } else {
                    if (diffAtCon == diffFront2) {
                        maxCount = Math.max(maxCount, countFront2 + 1);
                    }
                    if (diffAtCon == diffBack) {
                        maxCount = Math.max(maxCount, countBack + 1);
                    }
                }

                if (end2 - start2 == maxCount2) {
                    if (diffBack == diffFront2 && diffAtCon == diffBack) {
                        countBack = countBack2 + countBack;
                    } else if (diffAtCon == diffBack2) {
                        countBack = countBack2 + 1;
                    } else {
                        countBack = countBack2;
                        diffBack = diffBack2;
                    }
                } else {
                    countBack = countBack2;
                    diffBack = diffBack2;
                }
                
                maxCount = Math.max(maxCount, maxCount2);

//                System.err.printf("%d\t%d\t%d\t%d\t%d%n", maxCount, countFront, diffFront, countBack, diffBack);
            }

            System.out.println(maxCount);
        }
    }
}
