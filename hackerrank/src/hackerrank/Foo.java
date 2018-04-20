package hackerrank;

import org.junit.Test;

import java.util.*;

// How to unit test this class?
public class Foo {
    @Autowired
    private Bar bar;
    
    public int solve(int input) {
        int result = 0;
        //...
        // some logic here
        //...
        int preliminaryData = bar.externalCall();
        //...
        // some more logic here modifying
        //...
        return result + preliminaryData;
    }

    @Test
    public void speed1() {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);

        for (int i = 0; i < 100000; i++) {
            a.add(5);
        }

        int x = 0;
        for (Integer integer : a) {
            x ^= integer;
        }
        System.out.println(x);

        System.out.println(a.size());
    }

    @Test
    public void speed2() {
        LinkedList<Integer> a = new LinkedList<>();
        a.add(1);
        a.add(2);
        a.add(3);

        for (int i = 0; i < 100000; i++) {
            a.add(1, 5);
        }

        int x = 0;
        for (Integer integer : a) {
            x ^= integer;
        }
        System.out.println(x);

        System.out.println(a.size());
    }

    public static void main2(String[] args) {
        HashSet<Calendar> set = new HashSet<>();
        Calendar today = Calendar.getInstance();
        set.add(today);
        today.add(Calendar.DATE, 1);
        set.add(today);
        System.out.println(set.size());
    }

    @SuppressWarnings({"MismatchedReadAndWriteOfArray", "ComparatorMethodParameterNotUsed"})
    // shuffle
    public static void main(String[] args) {
        Integer[] a = new Integer[1000];
        for (int i = 0; i < 1000; i++) a[i] = i;
        
        Random r = new Random();
        Arrays.sort(a, (x, y) -> r.nextInt(3) - 1);
        
        // Great! Our array is sorted. Oh, wait... 
    }
}

@interface Autowired {
    
}

interface Bar {
    public int externalCall();
};

@SuppressWarnings("ComparatorCombinators")
class WeEnjoyTyping {
    public static void main(String[] args) {
        Integer[] data = {-100, 2147483647, 0, -100};
        
        Arrays.sort(data, (a, b) -> a - b);
        
        // outputs
        // [-100, 0, 2147483647, -100]
        System.out.println(Arrays.toString(data));
    }
}

class Stuff {
    
}

class IterateIt {
    public static void main(String[] args) {
        
    }
}