package nanobots;

public class Main {
    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        
        if (myNodeId != 0) return;

        long R = nanobots.GetRange();
        long c = 0;
        long sum = 0;
        
        for (long r = R - 1; r >= 0; r--) {
            long left = c;
            long right = R;
            
            while (left != right) {
                long mid = (left + right) / 2;
                if (nanobots.Experiment(r + 1, mid + 1) == 'T') {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            sum += left;
//            System.err.println("+ " + left);
            c = left;
        }

        System.out.println(sum);
    }
}
