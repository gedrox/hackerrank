import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] unsorted = new String[n];
        Integer[] i = new Integer[n];
        int[] L = new int[n];
        String[] S = new String[n];
        for(int unsorted_i=0; unsorted_i < n; unsorted_i++){
            unsorted[unsorted_i] = in.next();
            String s = unsorted[unsorted_i];
            i[unsorted_i] = unsorted_i;
            int x = 0;
            while (x < s.length() && s.charAt(x) == '0') {
                x++;
            }
            L[unsorted_i] = s.length() - x;
            S[unsorted_i] = s.substring(x);
        }

        //System.err.println(Arrays.toString(L));

        Arrays.sort(i, (a, b) -> {
            if (L[a] > L[b]) return 1;
            if (L[a] < L[b]) return -1;
            return S[a].compareTo(S[b]);
        });

        for (int j : i) {
            System.out.println(unsorted[j]);
        }
    }
}
