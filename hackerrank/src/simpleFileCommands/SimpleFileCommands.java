package simpleFileCommands;

import org.junit.Test;

import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class SimpleFileCommands {
    
    static HashMap<String, Integer> len = new HashMap<>();
    static HashMap<String, TreeSet<Integer>> gaps = new HashMap<>();
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        
        StringBuilder sb = new StringBuilder();
        // Process each command
        for(int a0 = 0; a0 < q; a0++){
            // Read the first string denoting the command type
            String command = in.next();
            // Write additional code to read the command's file name(s) and process the command
            // Print the output string appropriate to the command
            
            if (command.equals("crt")) {
                String fileName = create(in.next());
                sb.append("+ ").append(fileName).append('\n');
            }
            if (command.equals("del")) {
                String fileName = remove(in.next());
                sb.append("- ").append(fileName).append('\n');
            }
            if (command.equals("rnm")) {
                String oldName = remove(in.next());
                String newName = create(in.next());
                sb.append("r ").append(oldName).append(" -> ").append(newName).appendCodePoint('\n');
            }
        }

        System.out.print(sb.toString());
    }

    private static String remove(String next) {
        int end = next.indexOf('(');
        String base;
        int ind;
        if (end != -1) {
            base = next.substring(0, end);
            ind = Integer.parseInt(next.substring(end + 1, next.length() - 1));
        } else {
            base = next;
            ind = 0;
        }
        gaps.get(base).add(ind);
        return next;
    }

    private static String create(String name) {
        if (!len.containsKey(name)) {
            len.put(name, 1);
            gaps.put(name, new TreeSet<>());
            return name;
        }
        int ind;
        if (!gaps.get(name).isEmpty()) {
            ind = gaps.get(name).pollFirst();
        } else {
            ind = len.get(name);
            len.put(name, ind + 1);
        }
        return name + (ind == 0 ? "" : "(" + ind + ")");
    }
    
    @Test
    public void test() {
        for (int i = 0; i < 100000; i++) {
            create("base");
        }
        for (int i = 1; i < 100000; i++) {
            remove("base(" + i + ")");
        }
        for (int i = 0; i < 100000; i++) {
            create("base");
        }
        for (int i = 0; i < 100000; i++) {
            remove("base(" + i + ")");
            create("new");
        }
    }
    
}
