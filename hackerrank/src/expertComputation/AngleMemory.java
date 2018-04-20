package expertComputation;

import org.junit.Test;

public class AngleMemory {
    
    final static int BITS = 2;
    final static int LVL = 1 << BITS;
    
    final static Vector LAST = new Vector(0, 0, 0, 1);

    // coordinates, shared...
    public static int[] h = new int[1_000_001];
    public static int[] c = new int[1_000_001];
    
    AngleMemory[] ch;
    int[] val;
//    int[] tanX = new int[LVL];
//    int[] tanY = new int[LVL];
    
    Vector[] vec;
    
    int size;
    int level;

    public AngleMemory() {
        this(0);
    }

    public AngleMemory(int level) {
        this.level = level;
        if (level == 0) {
            this.val = new int[LVL];
            this.vec = new Vector[LVL];
        } else {
            this.ch = new AngleMemory[LVL];
            this.vec = new Vector[LVL];
        }
    }

    public AngleMemory(AngleMemory that) {
        if (that.ch != null) ch = that.ch.clone();
        vec = that.vec.clone();
        if (that.val != null) val = that.val.clone();
        size = that.size;
        level = that.level;
    }

    public AngleMemory put(int i, int findX, int findY) {

        h[i] = findX;
        c[i] = findY;
        
        AngleMemory clone = new AngleMemory(this);

        int pos = clone.binarySearch(findX, findY, false);
        
        if (level != 0 && pos != 0 && pos == localSize() && ch[pos - 1].localSize() != LVL) {
            pos--;
        }
        
        // need to add
        if (pos == LVL) {

            AngleMemory newRoot = new AngleMemory(this.level + 1);
            newRoot.ch[0] = this;
            newRoot.vec[0] = this.vec[LVL - 1];

            newRoot.size = this.size;
            
            return newRoot.put(i, findX, findY);
        }

        if (level == 0) {
            clone.val[pos] = i;
            if (pos == 0) {
                //todo: wrong..
                clone.vec[pos] = new Vector(findX, findY, findX, findY);
            } else {
                clone.vec[pos] = new Vector(findX, findY, findX - clone.vec[pos - 1].x, findY - clone.vec[pos - 1].y);
            }
            clone.size = pos + 1;
        } else {
            if (clone.ch[pos] == null) {
                clone.ch[pos] = new AngleMemory(clone.level - 1);
            }
            AngleMemory chClone = clone.ch[pos].put(i, findX, findY);
            clone.ch[pos] = chClone;
            clone.size = chClone.size + pos * (1 << (BITS * level));
            if (chClone.size < LVL) {
                clone.vec[pos] = LAST;
            } else {
                clone.vec[pos] = chClone.vec[LVL - 1];
            }
        }
        
        return clone;
    }

    private int localSize() {
        if (size == 0) return 0;
        int lastElPos = size - 1;
        return (lastElPos >> (level * BITS)) + 1;
    }

    public Vector find(int findX, int findY) {
        int a = binarySearch(findX, findY, true) - 1;
        
        if (level == 0) {
            if (a == size) {
                return vec[a - 1];
            } else if (a == -1) {
                return vec[0];
            }
            return vec[a];
        } else {
            return ch[a].find(findX, findY);
        }
    }

    private int binarySearch(int findX, int findY, boolean zeroStart) {
        for (int c = 0; c < localSize(); c++) {
            boolean stop = (long) vec[c].vectorX * (findY - (zeroStart ? 0 : vec[c].y)) <= (long) (findX - (zeroStart ? 0 : vec[c].x)) * vec[c].vectorY;
            if (stop) {
                return c;
            }
        }
        return localSize();
    }
    
    private int realBinarySearch(int findX, int findY, boolean zeroStart) {
        if (size == 0) return -1;
        int a = -1, b = localSize();
        assert b > 0 && b <= LVL;

        while (b - a > 1) {
            int c = (a + b) / 2;
            if ((long) vec[c].vectorX * (findY - (zeroStart ? 0 : vec[c].y)) <= (long) (findX - (zeroStart ? 0 : vec[c].x)) * vec[c].vectorY) {
                b = c;
            } else {
                a = c;
            }
        }
        
        return a;
//        if ((long) vec[a].vectorX * (findY - (zeroStart ? 0 : vec[a].y)) <= (long) (findX - (zeroStart ? 0 : vec[a].x)) * vec[a].vectorY) {
//            return a;
//        } else {
//            return a;
//        }
    }
    
    static class Vector {
        int x, y, vectorX, vectorY;

        public Vector(int x, int y, int vectorX, int vectorY) {
            this.x = x;
            this.y = y;
            this.vectorX = vectorX;
            this.vectorY = vectorY;
        }

        @Override
        public String toString() {
            return "Vector{" +
                    "x=" + x +
                    ", y=" + y +
                    ", vectorX=" + vectorX +
                    ", vectorY=" + vectorY +
                    '}';
        }
    }
}
