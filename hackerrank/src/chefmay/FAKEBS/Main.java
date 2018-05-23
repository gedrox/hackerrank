package chefmay.FAKEBS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int n;
    private static int q;
    private static int[] a;
    private static int[] x;
    static HashMap<Integer, Integer> answers = new HashMap<>();
    private static int[] sorted;

    static int readInt() throws IOException {
        return (int) readLong();
    }

    private static int[] readIntArray(int n) throws IOException {
        int[] V = new int[n];
        for (int i = 0; i < n; i++) V[i] = readInt();
        return V;
    }

    static long readLong() throws IOException {
        long res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }

    static char readChar() throws IOException {
        while (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') ch = br.read();
        char oneChar = (char) ch;
        ch = br.read();
        return oneChar;
    }

    public static void main(String[] args) throws IOException {
        int t = readInt();
        for (int i = 0; i < t; i++) {
            readInput();
            int[] ans = solve();
            for (int an : ans) {
                System.out.println(an);
            }
        }
    }

    private static int[] solve() {
        
        int[] out = new int[x.length];

        sorted = a.clone();
        Arrays.sort(sorted);
        
        TreeSet<Integer> left = new TreeSet<>();
        TreeSet<Integer> right = new TreeSet<>();

        LinkedList<Action> q = new LinkedList<>();
        q.add(new Split(q, left, right, 0, n - 1));
        
        while (!q.isEmpty()) {
            Action f = q.pollFirst();
            f.exec();
        }

        for (int i = 0; i < x.length; i++) {
            int anX = x[i];
            out[i] = answers.get(anX);
        }
        
        return out;
    }

    private static void readInput() throws IOException {
        n = readInt();
        q = readInt();
        a = readIntArray(n);
        x = readIntArray(q);
    }

    interface Action {
        void exec();
    }
    
    static class Split implements Action {

        private final TreeSet<Integer> left;
        private final TreeSet<Integer> right;
        int low;
        int high;
        LinkedList<Action> q;

        public Split(LinkedList<Action> q, 
                     TreeSet<Integer> left,
                     TreeSet<Integer> right,
                     int low, int high) {
            this.q = q;
            this.left = left;
            this.right = right;
            this.low = low;
            this.high = high;
        }

        @Override
        public void exec() {
            
            if (low > high) return;
            
            int mid = (low + high) / 2;
            int midVal = a[mid];
            
            // calculations for mid
            int tooBig = left.tailSet(midVal).size();
            int tooSmall = right.headSet(midVal).size();
            
            int pos = Arrays.binarySearch(sorted, midVal);
            
            if (left.size() > pos || right.size() > n - 1 - pos) {
                answers.put(midVal, -1);
            } else {
                answers.put(midVal, Math.max(tooBig, tooSmall));
            }
            
            ArrayList<Action> actions = new ArrayList<>();
            
            // add mid as right and do split for left
            {
                Action add = new Add(mid, right);
                Action split = new Split(q, left, right, low, mid - 1);
                Action undo = new Undo(add);
                actions.add(add);
                actions.add(split);
                actions.add(undo);
            }
            
            // add mid as left, do split for right
            {
                Action add = new Add(mid, left);
                Action split = new Split(q, left, right, mid + 1, high);
                Action undo = new Undo(add);
                actions.add(add);
                actions.add(split);
                actions.add(undo);
            }

            for (int i = actions.size() - 1; i >= 0; i--) {
                q.addFirst(actions.get(i));
            }
        }
    }
    
    static class Undo implements Action {
        Action action;

        public Undo(Action action) {
            this.action = action;
        }

        @Override
        public void exec() {
            if (action instanceof Add) {
                ((Add) action).collection.remove(a[((Add) action).index]);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    static class Add implements Action {
        int index;
        Collection<Integer> collection;

        public Add(int index, Collection<Integer> collection) {
            this.index = index;
            this.collection = collection;
        }
        
        public void exec() {
            collection.add(a[index]);
        }
    }
    
    
}
