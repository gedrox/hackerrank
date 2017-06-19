package again;

public class Main {

    private static final long MOD = 1000000007;

    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        long N = again.GetN();

        //sum b
        long A = 0, B = 0;
        for (int i = myNodeId; i < N; i += nodes) {
            A = (A + again.GetA(i)) % MOD;
            B = (B + again.GetB(i)) % MOD;
        }
        
        long[] a = new long[20];
        long[] b = new long[20];
        if (myNodeId != 0) {
            message.PutLL(0, A);
            message.PutLL(0, B);
            message.Send(0);
        } else {
            a[0] = A;
            b[0] = B;
            for (int i = 1; i < nodes; i++) {
                message.Receive(i);
                a[i] = message.GetLL(i);
                b[i] = message.GetLL(i);
                A = (A + a[i]) % MOD;
                B = (B + b[i]) % MOD;
            }
            
            long sum = (A * B) % MOD;
            for (int i = 0; i < nodes; i++) {
                sum = (sum - a[i] * b[(nodes - i) % nodes]) % MOD;
            }
            if (sum < 0) sum += MOD;
            System.out.println(sum);
        }
        
        
    }

}
