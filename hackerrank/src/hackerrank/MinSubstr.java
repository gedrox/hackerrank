package hackerrank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MinSubstr {
    public int minimumLength(String text, ArrayList<String> keys) {
        int keysLen = keys.size();
        
        String[] split = text.split(" ");

        int smallestPossible = -1;
        int[] pos = new int[keysLen];
        int[] len = new int[keysLen];
        HashMap<String, Integer> where = new HashMap<>();
        for (int i = 0; i < keysLen; i++) {
            pos[i] = - i - 1;
            len[i] = keys.get(i).length();
            where.put(keys.get(i), i);
            smallestPossible += len[i] + 1;
        }
        int minPos = -keysLen;
        int minLen = Integer.MAX_VALUE;

        int p = 0;
        for (String s : split) {
            if (where.containsKey(s)) {
                int x = where.get(s);
                if (pos[x] == minPos) {
                    pos[x] = p;
                    minPos = Integer.MAX_VALUE;
                    for (int po : pos) {
                        if (po < minPos) {
                            minPos = po;
                        }
                    }
                } else {
                    pos[x] = p;
                }
                
                if (minPos >= 0) {
                    int newLen = p + len[x] - minPos;
                    if (newLen < minLen) {
                        minLen = newLen;
                        if (minLen == smallestPossible) return minLen;
                    }
                }
            }
            
            p += s.length() + 1;
        }
        
        return minLen == Integer.MAX_VALUE ? -1 : minLen;
    }

    public static void main(String[] args) {
        MinSubstr s = new MinSubstr();
        String text, buf;
        ArrayList < String > keys = new ArrayList < String >();
        Scanner in = new Scanner(System.in);
        text = in.nextLine();
        int keyWords = in.nextInt();
        in.nextLine();
        for(int i = 0; i < keyWords; i++) {
            buf = in.nextLine();
            keys.add(buf);
        }

        System.out.println(s.minimumLength(text, keys));
    }
}
