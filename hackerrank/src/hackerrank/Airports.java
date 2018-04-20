package hackerrank;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

public class Airports {

    static int n;
    static int d;
    static int[] x;
    static int index = 0;
    static int[] color;
    static Integer[] sort;
    static int colorCount = 0;
    static int minIndex;
    static int maxIndex;
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine());
        StringBuilder sb = new StringBuilder();
        for (int t_i = 0; t_i < t; t_i++) {
            String[] split = in.readLine().split(" ");
            n = Integer.parseInt(split[0]);
            d = Integer.parseInt(split[1]);
            x = new int[n];
            split = in.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                x[i] = Integer.parseInt(split[i]);
            }
            prepare();
            for (int i = 0; i < n; i++) {
                int res = solveNext();
                sb.append(res).append(' ');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
    
    @Test
    public void test() {
        d = 1;
        x = new int[] {0, 0, 0};
        
//        d = 4;
//        x = new int[] {1, -1, 2, -1, 1};
        
        prepare();

        for (int i = 0; i < n; i++) {
            int res = solveNext();
            System.out.println(res);
        }
    }
    
    static void prepare() {
        minIndex = 0;
        maxIndex = 0;
        n = x.length;
        index = 0;
        colorCount = 0;
        color = new int[n];
        sort = new Integer[n];
        for (int j = 0; j < n; j++) {
            color[j] = j;
            sort[j] = j;
        }
    }
    
    static int solveNext() {
        int newX = x[index];
        int newI = index;
        index++;
        colorCount++;
        
        if (newX < x[minIndex]) minIndex = newI;
        if (newX > x[maxIndex]) maxIndex = newI;
        
        // in asc order
//        Arrays.sort(sort, 0, index, Comparator.comparing(i -> x[i]));
        
        // let's color the graph...
        for (int minMaxIndex : new int[] {minIndex, maxIndex}) {
            if (Math.abs(x[minMaxIndex] - newX) >= d) {
                int newColor = getColor(newI);
                int minMaxColor = getColor(minMaxIndex);

                if (newColor != minMaxColor) {
                    colorCount--;
                    color[newColor] = minMaxColor;
                }
            }
        }
//        if (x[minIndex] - newX <= d) {
//            int newColor = getColor(newI);
//            int maxIndColor = getColor(maxIndex);
//
//            if (newColor != maxIndColor) {
//                colorCount--;
//                color[newColor] = maxIndColor;
//            }
//        }
            
//        for (int i = 0; newX - x[sort[i]] >= d; i++) {
//            if (getColor(newI) != getColor(sort[i])) {
//                colorCount--;
//                color[getColor(newI)] = getColor(sort[i]);
//            }
//        }
//        for (int i = index - 1; x[sort[i]] - newX >= d; i--) {
//            if (getColor(newI) != getColor(sort[i])) {
//                colorCount--;
//                color[getColor(newI)] = getColor(sort[i]);
//            }
//        }
        
        if (colorCount == 1) return 0;

        int bestCost;
        
        Integer nearestColorX = null;
        int minColor = getColor(sort[0]);
        for (int i = 1; i < index; i++) {
            if (getColor(sort[i]) != minColor) {
                nearestColorX = x[sort[i]];
                break;
            }
        }
        
        assert nearestColorX != null;
        bestCost = x[sort[0]] - (nearestColorX - d);

        nearestColorX = null;
        int maxColor = getColor(sort[index - 1]);
        for (int i = index - 1; i >= 0; i--) {
            if (getColor(sort[i]) != maxColor) {
                nearestColorX = x[sort[i]];
                break;
            }
        }

        assert nearestColorX != null;
        bestCost = Math.min(bestCost, nearestColorX + d - x[sort[index - 1]]);
        
        return bestCost;
    }
    
    static int getColor(int i) {
        if (color[i] == i) {
            return i;
        }
        return (color[i] = getColor(color[i]));
    }
    
}
