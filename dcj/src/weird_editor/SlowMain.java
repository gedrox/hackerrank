package weird_editor;

import java.math.BigInteger;
import java.util.Arrays;

public class SlowMain {

    static int MOD = 1000000007;
    static BigInteger BIG_MOD = BigInteger.valueOf(MOD);
    public static final long INVERSE_TEN = BigInteger.TEN.modInverse(BIG_MOD).longValue();

    public static void main(String[] args) {

        long start = message.MyNodeId() * weird_editor.GetNumberLength() / message.NumberOfNodes();
        long end = (message.MyNodeId() + 1) * weird_editor.GetNumberLength() / message.NumberOfNodes();
        
        long offset = 0;
        
        long[] digitCount = new long[10];
        
        for (int digit = 9; digit > 0; digit--) {
            
            long count = 0;
            long lastPos = 0;
            
            if (offset < end) {
                for (long i = Math.max(start, offset); i < end; i++) {
                    if (weird_editor.GetDigit(i) == digit) {
                        count++;
                        lastPos = i;
                    }
                }
            }
            
            long countTotal;
            long lastPosTotal;
            if (message.MyNodeId() == 0) {
                countTotal = count;
                lastPosTotal = lastPos;
            } else {
                int prevNode = message.MyNodeId() - 1;
                message.Receive(prevNode);
                countTotal = message.GetLL(prevNode) + count;
                lastPosTotal = Math.max(message.GetLL(prevNode), lastPos);
            }

            if (message.MyNodeId() != message.NumberOfNodes() - 1) {
                int nextNode = message.MyNodeId() + 1;
                message.PutLL(nextNode, countTotal);
                message.PutLL(nextNode, lastPosTotal);
                message.Send(nextNode);
            } else {
                offset = Math.max(lastPosTotal, offset);
                digitCount[digit] = countTotal;
            }
            
            if (message.MyNodeId() != message.NumberOfNodes() - 1) {
                int nextNode = message.MyNodeId() + 1;
                message.Receive(nextNode);
                offset = message.GetLL(nextNode);
            }
            
            if (message.MyNodeId() != 0) {
                int prevNode = (message.MyNodeId() - 1);
                message.PutLL(prevNode, offset);
                message.Send(prevNode);
            }
        }

//        System.err.println(Arrays.toString(digitCount));

        // receive mapping
        if (message.MyNodeId() != message.NumberOfNodes() - 1) {
            int nextNode = message.MyNodeId() + 1;
            message.Receive(nextNode);
            for (int i = 1; i <= 9; i++) {
                digitCount[i] = message.GetLL(nextNode);
            }
        }
        
        // send mapping
        if (message.MyNodeId() != 0) {
            int prevNode = message.MyNodeId() - 1;
            for (int i = 1; i <= 9; i++) {
                message.PutLL(prevNode, digitCount[i]);
            }
            message.Send(prevNode);
        }

//        System.err.println(Arrays.toString(digitCount));
        
        long toRemove = start;
        int currDigit = 9;
        while (toRemove != 0 && currDigit > 0) {
            if (digitCount[currDigit] > 0) {
                long chunk = Math.min(digitCount[currDigit], toRemove);
                digitCount[currDigit] -= chunk;
                toRemove -= chunk;
            }
            while (currDigit > 0 && digitCount[currDigit] == 0) {
                currDigit--;
            }
        }
        
        long sum = 0;
        long rem = BigInteger.TEN.modPow(BigInteger.valueOf(weird_editor.GetNumberLength() - 1 - start), BIG_MOD).longValue();
        summing:
        for (long i = start; i < end && currDigit > 0; i++) {

            while (digitCount[currDigit] == 0) {
                currDigit--;
                if (currDigit == 0) {
                    break summing;
                }
            }
            
            sum += currDigit * rem;
            sum %= MOD;

            digitCount[currDigit]--;
            
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
