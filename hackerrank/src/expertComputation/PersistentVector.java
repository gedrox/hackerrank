package expertComputation;

import org.junit.Test;

public class PersistentVector {
    
    static final int BITS = 2;
    static final int LEVEL = 1 << BITS;
    Node root;
    int capacity = 0;
    int size;
    int depth = -1;

    public PersistentVector() {
    }

    public PersistentVector(PersistentVector src) {
        this.root = src.root;
        this.capacity = src.capacity;
        this.size = src.size;
        this.depth = src.depth;
    }

    public int get(int k) {
        Node n = root;
        for (int i = depth; i > 0; i--) {
            n = ((Branch) n).child[(k >> (BITS * i)) % LEVEL];
        }
        return ((Leaf) n).values[k % LEVEL];
    }
    
    public PersistentVector add(int v) {
        PersistentVector newVector = new PersistentVector(this);
        
        if (size == capacity) {
            if (root == null) {
                root = new Leaf();
            } else {
                Branch newRoot = new Branch();
                if (root instanceof Leaf) {
                    newRoot.child = new Leaf[LEVEL];
                } else {
                    newRoot.child = new Branch[LEVEL];
                }
                newRoot.child[0] = root;
                newVector.root = newRoot;
            }
            newVector.capacity = capacity == 0 ? LEVEL : capacity * LEVEL;
            newVector.depth++;
        } else {
            newVector.root = newVector.root.clone();
        }

        Node n = newVector.root;
        for (int i = depth; i > 0; i--) {
            int pos = (size >> (BITS * i)) % LEVEL;
            if (((Branch) n).child == null) {
                ((Branch) n).child = (i == 1 ? new Leaf[LEVEL] : new Branch[LEVEL]);
            }
            if (((Branch) n).child[pos] == null) {
                ((Branch) n).child[pos] = (i == 1 ? new Leaf() : new Branch());
            }
            n = ((Branch) n).child[pos];
        }
        ((Leaf) n).values[size % LEVEL] = v;
        
        size++;
        
        return newVector;
    }
    
    public void set(int k, int v) {
        if (k == size) {
            add(v);
            return;
        }
        assert k < size;
        Node n = root;
        for (int i = depth; i > 0; i--) {
            n = ((Branch) n).child[(k >> (BITS * i)) % LEVEL];
        }
        ((Leaf) n).values[k % LEVEL] = v;
    }
    
    public int size() {
        return size;
    }

    interface Node extends Cloneable {
        Node clone();
    }
    
    private static class Leaf implements Node {
        int[] values = new int[LEVEL];

        public Leaf clone() {
            try {
                return (Leaf) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private static class Branch implements Node {
        Node[] child;

        public Branch clone() {
            try {
                return (Branch) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Test
    public void test() {
        PersistentVector obj = new PersistentVector();
        for (int i = 0; i < 100; i++) {
            obj.add(100 - i);
        }
        System.out.println(obj.get(0));
        System.out.println(obj.get(55));
        
        obj.set(55, Integer.MAX_VALUE);

        System.out.println(obj.get(55));
    }
}
