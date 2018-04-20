package expertComputation;

import org.junit.Test;

import static org.junit.Assert.*;

public class AngleMemoryTest {

    @Test
    public void test() {
        AngleMemory a = new AngleMemory();
        AngleMemory b = a.put(1, 1, 1);
        assertEquals(1, b.size);
        b = b.put(2, 2, 3);
        assertEquals(2, b.size);
        b = b.put(3, 4, 1);
        assertEquals(1, b.size);
        b = b.put(4, 6, 6);
        assertEquals(2, b.size);
        assertEquals(6, b.find(1, 10).x);
        assertEquals(4, b.find(1, 1).x);
        assertEquals(4, b.find(10, 1).x);
    }
    
    @Test
    public void test2() {
        AngleMemory put = new AngleMemory()
                .put(1, 1, 1)
                .put(2, 2, 4)
                .put(3, 3, 9)
                .put(4, 4, 16)
                .put(5, 5, 25);
        System.out.println(put);
        
        assertEquals(5, put.size);
        assertEquals(1, put.level);
        
        int lastX = 5;
        
        for (int i = 6; i <= 11; i++) {
            
            int toReplace = i - 6;
            // that means we base on the previous
            int prevX = toReplace;
            int prevY = toReplace * toReplace;
            
            int addX = 1;
            int addY = 2 * toReplace + 1;
            
            int newX = prevX, newY = prevY;
            
            while (newX <= lastX) {
                newX += addX;
                newY += addY;
            }
//            lastX = newX;
            
            AngleMemory add = put.put(i, newX, newY);
            System.out.println("adding " + i + " x " + ((i - 5) * (i - 5) + (i - 1)));
            System.out.println(add.size);
        }
    }
}
