package number_bases;

public class Main {
    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        
        long L = number_bases.GetLength();
        
        long start = L * myNodeId / nodes;
        long end = L * (myNodeId + 1) / nodes;

        boolean[] possible = new boolean[] {true, true};
        long[] basis = new long[2];
        int[] endRem = new int[2];
        long max = 0;

        next:
        for (int initialOne : new int[]{0, 1}) {
            
            if (myNodeId == 0 && initialOne == 1) continue;
            int rem = initialOne;
            
            for (long i = start; i < end; i++) {
                long x = number_bases.GetDigitX(i);
                long y = number_bases.GetDigitY(i);
                long z = number_bases.GetDigitZ(i);

                max = Math.max(max, x);
                max = Math.max(max, y);
                max = Math.max(max, z);
                
                if (x + y + rem > z) {
                    long basisCandidate = x + y + rem - z;
                    if (basis[initialOne] != 0 && basis[initialOne] != basisCandidate) {
                        possible[initialOne] = false;
                        continue next;
                    } else {
                        basis[initialOne] = basisCandidate;
                        rem = 1;
                    }
                } else if (x + y + rem != z) {
                    possible[initialOne] = false;
                    continue next;
                } else {
                    rem = 0;
                }
            }
            
            endRem[initialOne] = rem;
        }
        
        if (myNodeId > 0) {
            message.PutInt(0, possible[0] ? 1 : 0);
            message.PutInt(0, possible[1] ? 1 : 0);
            message.PutLL(0, basis[0]);
            message.PutLL(0, basis[1]);
            message.PutInt(0, endRem[0]);
            message.PutInt(0, endRem[1]);
            message.PutLL(0, max);
            message.Send(0);
        } else {
            
            int rem = endRem[0];
            long currBasis = basis[0];
            boolean globalPossible = possible[0];
            
            for (int node = 1; node < nodes; node++) {
                message.Receive(node);
                int[] nextPoss = new int[]{message.GetInt(node), message.GetInt(node)};
                long[] nextBasis = new long[]{message.GetLL(node), message.GetLL(node)};
                int[] nextEndRem = new int[]{message.GetInt(node), message.GetInt(node)};
                long nextMax = message.GetLL(node);
                max = Math.max(max, nextMax);
                
                if (!globalPossible) continue;
                
                if (nextPoss[rem] == 0) {
                    globalPossible = false;
                    continue;
                }
                if (nextBasis[rem] != 0) {
                    if (currBasis == 0) {
                        currBasis = nextBasis[rem];
                    } else if (currBasis != nextBasis[rem]) {
                        globalPossible = false;
                        continue;
                    }
                }
                
                rem = nextEndRem[rem];
            }
            
            if (!globalPossible || rem != 0) {
                System.out.println("IMPOSSIBLE");
            } else if (currBasis == 0) {
                System.out.println("NON-UNIQUE");
            } else if (currBasis <= max) {
                System.out.println("IMPOSSIBLE");
            } else {
                System.out.println(currBasis);
            }
        }
    }
}
