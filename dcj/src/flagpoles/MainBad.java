package flagpoles;

public class MainBad {
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
        
        boolean allGood = (maxCount == end - start);

//        System.err.println("diffFront: " + diffFront);
//        System.err.println("diffBack: " + diffBack);
//        System.err.println("countFront: " + countFront);
//        System.err.println("countBack: " + countBack);
//        System.err.println("maxCount: " + maxCount);

        if (myNodeId > 0) {
            message.Receive(myNodeId - 1);
            long maxCount2 = message.GetLL(myNodeId - 1);
            long diffBack2 = message.GetLL(myNodeId - 1);
            long countBack2 = message.GetLL(myNodeId - 1);
            
            long diffAtConn = flagpoles.GetHeight(start) - flagpoles.GetHeight(start - 1);
            
            if (allGood) {
                if (diffAtConn == diffBack) {
                    if (diffAtConn == diffBack2) {
                        countBack = countBack2 + end - start;
                    } else {
                        countBack += 1;
                    }
                }
            }
            
            if (diffFront == diffBack2 && diffAtConn == diffFront) {
                maxCount = Math.max(maxCount, countFront + countBack2);
//                if (allGood) countBack = countFront + countBack2;
//                System.err.println("2 Increase maxCount " + maxCount + " --> " + countFront + " + " + countBack2);
            } else {
                if (diffAtConn == diffFront) {
                    maxCount = Math.max(maxCount, countFront + 1);
//                    if (allGood) countBack = countFront + 1;
//                        System.err.println("4 Increase maxCount " + maxCount + " --> " + countFront + " + 1");
                }
                if (diffAtConn == diffBack2) {
                    maxCount = Math.max(maxCount, countBack2 + 1);
//                        allGood = false;
//                        System.err.println("3 Increase maxCount " + maxCount + " --> " + countBack2 + " + 1");
                }
            }

            if (maxCount2 > maxCount) {
                maxCount = maxCount2;
//                System.err.println("1 Increase maxCount " + maxCount + " --> " + maxCount2);
            }
        }

        if (myNodeId < nodes - 1) {
//            System.err.println("all good? " + allGood);
//            if (allGood) {
//                countBack = maxCount;
//            }
            
            message.PutLL(myNodeId + 1, maxCount);
            message.PutLL(myNodeId + 1, diffBack);
            message.PutLL(myNodeId + 1, countBack);
            message.Send(myNodeId + 1);

//            System.err.println("Put maxCount = " + maxCount);
//            System.err.println("Put diffBack = " + diffBack);
//            System.err.println("Put countBack = " + countBack);
        } else {
            System.out.println(maxCount);
        }
    }
}
