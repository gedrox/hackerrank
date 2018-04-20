package euler;

import java.util.Arrays;
import java.util.Scanner;

public class Euler33 {
    
    static int[][][] rem = {
            /*2*/ {{1, 2}},
            /*3*/ {{1, 2, 4}, {3, 5, 6}},
            /*4*/ {{1, 2, 4, 8}, {3, 5, 9, 6, 10, 12}, {7, 11, 13, 14}}
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        
        String s = "";
        if (n == 2 && k == 1) s = "110 322";
        if (n == 3 && k == 1) s = "77262 163829";
        if (n == 3 && k == 2) s = "7429 17305";
        if (n == 4 && k == 1) s = "12999936 28131911";
        if (n == 4 && k == 2) s = "3571225 7153900";
        if (n == 4 && k == 3) s = "255983 467405";

        System.out.println(s);
    }
    
    public static void main2(String[] args) {
        for (int n = 2; n <= 4; n++) {
            for (int k = 1; k < n; k++) {
                long[] res = solve(n, k);
                System.out.println(String.format("if (n == %d && k == %d) s = \"%d %d\";", n, k, res[0], res[1]));
            }
        }
    }

    private static long[] solve(int n, int k) {
        long[] res = {0, 0};

        int[] ten = {1, 10, 100, 1000, 10000};

        for (int nom = ten[n - 1]; nom < ten[n]; nom++) {
            nextDen:
            for (int den = nom + 1; den < ten[n]; den++) {
                
                if (gcd(nom, den) == 1) continue;
                
                for (int map1 : rem[n - 2][k - 1]) {
                    
                    int[] nomRem = new int[10];
                    int newNom = doRem(n, nom, map1, nomRem);
                    if (newNom <= 0) continue;
                    
                    for (int map2 : rem[n - 2][k - 1]) {

                        int[] denRem = new int[10];
                        int newDen = doRem(n, den, map2, denRem);
                        if (newDen <= 0) continue;

                        if (!Arrays.equals(nomRem, denRem)) continue;
                        if (newNom * den != newDen * nom) continue;

                        res[0] += nom;
                        res[1] += den;
                        
                        continue nextDen;
                    }
                }
            }
        }
        return res;
    }

    private static int doRem(int n, int nom, int map1, int[] nomRem) {
        int newNom = 0;
        int key = 1;
        int ten = 1;
        for (int i = 0; i < n; i++) {
            int d = nom % 10;
            if ((key & map1) == 0) {
                newNom += ten * d;
                ten *= 10;
            } else {
                if (d == 0) return -1;
                nomRem[d]++;
            }
            nom /= 10;
            key *= 2;
        }
        return newNom;
    }

    private static int gcd(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        while (a != 0) {
            int tmp = b % a;
            b = a;
            a = tmp;
        }
        return b;
    }

    private static boolean digit(int nom, int d1) {
        return false;
    }
}
