package pancakes;

public class Main {

    public static void main(String[] args) {
        
        long start = message.MyNodeId() * pancakes.GetStackSize() / message.NumberOfNodes();
        long end = (message.MyNodeId() + 1) * pancakes.GetStackSize() / message.NumberOfNodes();
        
        long rounds = 0;
        long prev = start == 0 ? -1 : pancakes.GetStackItem(start - 1);
        for (long i = start; i < end; i++) {
            long number = pancakes.GetStackItem(i);
            if (number < prev) {
                rounds++;
            }
            prev = number;
        }
        
        if (message.MyNodeId() == 0) {
            for (int i = 1; i < message.NumberOfNodes(); i++) {
                message.Receive(i);
                rounds += message.GetLL(i);
            }

            System.out.println(rounds + 1);
        } else {
            message.PutLL(0, rounds);
            message.Send(0);
        }
    }
}
