package weird_editor;

import java.math.BigInteger;

public class Main {

    static int MOD = 1000000007;
    static BigInteger BIG_MOD = BigInteger.valueOf(MOD);
    public static final long INVERSE_TEN = BigInteger.TEN.modInverse(BIG_MOD).longValue();

    public static void main(String[] args) {

        long start = message.MyNodeId() * weird_editor.GetNumberLength() / message.NumberOfNodes();
        long end = (message.MyNodeId() + 1) * weird_editor.GetNumberLength() / message.NumberOfNodes();
        
        long[] digitCount = new long[10];
        
        for (long i = start; i < end; i++) {
            int d = (int) weird_editor.GetDigit(i);
            digitCount[d]++;
            for (int j = 0; j < d; j++) {
                digitCount[j] = 0;
            }
        }

        long[] totalCount = new long[10];
        
        // receive
        if (message.MyNodeId() != 0) {
            int prevNode = message.MyNodeId() - 1;
            message.Receive(prevNode);
            for (int i = 9; i > 0; i--) {
                totalCount[i] = message.GetLL(prevNode);
            }

            for (int i = 9; i > 0; i--) {
                if (digitCount[i] > 0) {
                    totalCount[i] += digitCount[i];
                    for (int j = 0; j < i; j++) {
                        totalCount[j] = 0;
                    }
                }
            }
        } else {
            totalCount = digitCount;
        }
        
        // send
        if (message.MyNodeId() != message.NumberOfNodes() - 1) {
            int nextNode = message.MyNodeId() + 1;
            for (int i = 9; i > 0; i--) {
                message.PutLL(nextNode, totalCount[i]);
            }
            message.Send(nextNode);
        }

        // receive mapping
        if (message.MyNodeId() != message.NumberOfNodes() - 1) {
            int nextNode = message.MyNodeId() + 1;
            message.Receive(nextNode);
            for (int i = 1; i <= 9; i++) {
                totalCount[i] = message.GetLL(nextNode);
            }
        }
        
        // send mapping
        if (message.MyNodeId() != 0) {
            int prevNode = message.MyNodeId() - 1;
            for (int i = 1; i <= 9; i++) {
                message.PutLL(prevNode, totalCount[i]);
            }
            message.Send(prevNode);
        }

        long toRemove = start;
        int currDigit = 9;
        while (toRemove != 0 && currDigit > 0) {
            if (totalCount[currDigit] > 0) {
                long chunk = Math.min(totalCount[currDigit], toRemove);
                totalCount[currDigit] -= chunk;
                toRemove -= chunk;
            }
            while (currDigit > 0 && totalCount[currDigit] == 0) {
                currDigit--;
            }
        }
        
        long sum = 0;
        long rem = BigInteger.TEN.modPow(BigInteger.valueOf(weird_editor.GetNumberLength() - 1 - start), BIG_MOD).longValue();
        summing:
        for (long i = start; i < end && currDigit > 0; i++) {

            while (totalCount[currDigit] == 0) {
                currDigit--;
                if (currDigit == 0) {
                    break summing;
                }
            }
            
            sum += currDigit * rem;
            sum %= MOD;

            totalCount[currDigit]--;
            
            rem *= INVERSE_TEN;
            rem %= MOD;
        }

        long totalSum;
        if (message.MyNodeId() != message.NumberOfNodes() - 1) {
            int nextNode = message.MyNodeId() + 1;
            message.Receive(nextNode);
            totalSum = message.GetLL(nextNode) + sum;
            totalSum %= MOD;
        } else {
            totalSum = sum;
        }
        
        if (message.MyNodeId() != 0) {
            int prevNode = message.MyNodeId() - 1;
            message.PutLL(prevNode, totalSum);
            message.Send(prevNode);
        }

        if (message.MyNodeId() == 0) {
            System.out.println(totalSum);
        }
    }
}
