package asteroids;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int nodes = message.NumberOfNodes();
        int myNodeId = message.MyNodeId();
        int w = (int) asteroids.GetWidth();
        int h = (int) asteroids.GetHeight();
        
        if (nodes > w) {
            nodes = w;
        }
        
        if (myNodeId >= nodes) {
            System.err.println("leaving");
            return;
        }

        int step = h / 499 + 1;
        
        if (step > w/nodes) {
            nodes = Math.max(1, w / step);
        }

        if (myNodeId >= nodes) {
            System.err.println("leaving");
            return;
        }
        
//        if (myNodeId == 0) {
//            System.err.println("step: " + step);
//        }
        
        int start = w * myNodeId / nodes;
        int end = w * (myNodeId + 1) / nodes;
        int len = end - start;

        
        int[] max = new int[len + 2 * step];
        for (int i = 0; i < step; i++) {
            max[i] = -1;
            max[len + step + i] = -1;
        }

        
        
        int[] nextMax = new int[len + 2 * step];
        for (int j = 0; j < h; j++) {
            log("Row: " + j);
            char prev = '#', c = 0;
            int LEFT = Math.max(0, start - step);
            int RIGHT = Math.min(end + step, w);

            for (int i = 0; i < nextMax.length; i++) {
                nextMax[i] = -1;
            }

            log("Before: " + Arrays.toString(max));
            
            for (int i = LEFT; i < RIGHT; i++) {
                int ind = i - start + step;
//                nextMax[ind] = -1;
                if (c == 0) c = asteroids.GetPosition(j, i);
                char next = (i != RIGHT - 1) ? asteroids.GetPosition(j, i + 1) : '#';
                
                if (c != '#') {
                    if (prev != '#' && max[ind - 1] != -1) {
                        int check = max[ind - 1] + (prev - '0') + (c - '0');
                        if (check > nextMax[ind]) {
                            log("Set from left: " + nextMax[ind] + "->" + check + " @" + i);
                            nextMax[ind] = check;
                        }
                    }
                    if (max[ind] != -1) {
                        int check = max[ind] + (c - '0');
                        if (check > nextMax[ind]) {
                            log("Set direct: " + nextMax[ind] + "->" + check + " @" + i);
                            nextMax[ind] = check;
                        }
                    }
                    if (next != '#' && max[ind + 1] != -1) {
                        int check = max[ind + 1] + (next - '0') + (c - '0');
                        if (check > nextMax[ind]) {
                            log("Set from right: " + nextMax[ind] + "->" + check + " @" + i);
                            nextMax[ind] = check;
                        }
                    }
                }
                
                prev = c;
                c = next;
            }

            // swap
            int[] tmp = max;
            max = nextMax;
            nextMax = tmp;

            log("After: " + Arrays.toString(max));

            // sync
            if (j % step == step - 1) {

                if (myNodeId != 0) {
                    int leftNode = myNodeId - 1;
                    
                    // send left
                    for (int i = 0; i < 2*step; i++) {
                        message.PutInt(leftNode, max[i]);
                    }
                    message.Send(leftNode);
                    
                    // receive left
                    message.Receive(leftNode);
                    for (int i = 0; i < 2*step; i++) {
                        max[i] = Math.max(max[i], message.GetInt(leftNode));
                        
                    }
                }
                if (myNodeId != nodes - 1) {
                    int rightNode = myNodeId + 1;
                    // sent right
                    for (int i = 0; i < 2*step; i++) {
                        message.PutInt(rightNode, max[len + i]);
                    }
                    message.Send(rightNode);
                    
                    // receive right
                    message.Receive(rightNode);
                    for (int i = 0; i < 2*step; i++) {
                        max[len + i] = Math.max(max[len + i], message.GetInt(rightNode));
                    }
                }
            }
        }
        
        int greatest = -1;
        for (int val : max) {
            if (val > greatest) {
                greatest = val;
            }
        }
        
        if (myNodeId != 0) {
            message.PutInt(0, greatest);
            message.Send(0);
        } else {
            for (int node_i = 1; node_i < nodes; node_i++) {
                message.Receive(node_i);
                int greatest_i = message.GetInt(node_i);
                if (greatest_i > greatest) {
                    greatest = greatest_i;
                }
            }

            System.out.println(greatest);
        }
    }

    private static void log(Object o) {
//        if (message.MyNodeId() == 1) 
//         System.err.println(o);
    }

}
