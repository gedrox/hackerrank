package atm;

import org.junit.Test;

import java.util.*;

public class ATM {
    
    @Test
    public void test() {
        System.out.println(give(1, List.of(3, 5)));
        System.out.println(give(6, List.of(3, 5)));
        System.out.println(give(10, List.of(3, 5)));
        System.out.println(give(11, List.of(3, 5)));
        System.out.println(give(13, List.of(3, 5)));
        System.out.println(give(1000000, List.of(3, 5, 7)));
    }
    
    // recursion w/o state copy and w/o actual recursion
    public Map<Integer, Integer> give(int sum, Collection<Integer> avail2) {
        
        int val = sum;
        HashMap<Integer, Integer> answer = new HashMap<>();
        LinkedList<int[]> q = new LinkedList<>();

        // sort desc
        ArrayList<Integer> avail = new ArrayList<>(new HashSet<>(avail2));
        avail.sort((a, b) -> b - a);
        int[] nom = avail.stream().mapToInt(i -> i).toArray();

        q.add(new int[] {0, 0});
        
        while (!q.isEmpty()) {
            int[] op = q.pollFirst();
            
            if (op[0] != 0) {
                val += op[0] * nom[op[1]];
                answer.compute(nom[op[1]], (v, old) -> (old == null ? 0 : old) - op[0]);
            }
            
            // answer it is
            if (val == 0) {
                return answer;
            }
            
            if (op[0] != 1) {
                int i = 0;
                for (int i1 = op[1]; i1 < avail.size(); i1++) {
                    int av = avail.get(i1);
                    if (av <= val) {
                        // deduct operation
                        q.add(i++, new int[]{-1, i1});
                        // rollback operation
                        q.add(i++, new int[]{1, i1});
                    }
                }
            }
        }
        
        return null;
    }
}
