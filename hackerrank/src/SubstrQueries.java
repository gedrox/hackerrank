import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class SubstrQueries {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        StringBuilder out = solve(in);
        System.out.print(out.hashCode());
    }
    
    static StringBuilder solve(Scanner in) {

        
        int n = in.nextInt();
        int q = in.nextInt();
        String[] s = new String[n];
        long t0 = System.currentTimeMillis();
        for (int s_i = 0; s_i < n; s_i++) {
            s[s_i] = in.next();
        }

        System.out.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();

        Integer[][] a = new Integer[n][];
        long[][] hash = new long[n][];
        for (int i = 0; i < n; i++) {
            String S = s[i];
            int L = S.length();
            a[i] = new Integer[L];
            hash[i] = new long[L];
            long[] H = hash[i];
            Integer[] A = a[i];
            for (int j = 0; j < L; j++) {
                A[j] = j;
            }
            Arrays.sort(A, (x, y) -> {
                int len1 = L - x;
                int len2 = L - y;
                int lim = Math.min(len1, len2);

                int k = 0;
                while (k < lim) {
                    char c1 = S.charAt(x + k);
                    char c2 = S.charAt(y + k);
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                    k++;
                }
                return len2 - len1;
            });

            for (int j = 0; j < L; j++) {
                for (int k = 0; k < 12; k++) {
                    H[j] = 32 * H[j] + (A[j] + k < L ? S.charAt(A[j] + k) - 'a' : 26);
                }
            }
        }

        System.out.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();

        StringBuilder out = new StringBuilder();
        HashMap<String, Integer> cache = new HashMap<>();

        for (int a0 = 0; a0 < q; a0++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int max = 0;

            String cacheKey = Math.min(x, y) + " " + Math.max(x, y);
            if (x == y) {
                max = s[x].length();
            } else if (cache.containsKey(cacheKey)) {
                max = cache.get(cacheKey);
            } else {

                String X = s[x];
                String Y = s[y];

                Integer A[] = a[x];
                Integer B[] = a[y];

                int i = 0, j = 0;
                int common = 0;
                while (i < A.length && j < B.length) {
                    common = 0;
                    int X_len = X.length();
                    int Y_len = Y.length();

                    long hashXor = hash[x][i] ^ hash[y][j];
                    if (hashXor != 0) {

                        int zeroes = Long.numberOfLeadingZeros(hashXor);
                        common = (zeroes - 4) / 5;
                        common = Math.min(common, X_len - A[i]);
                        common = Math.min(common, Y_len - B[j]);
                        if (common > max) {
                            max = common;
                        }

                        if (hash[x][i] < hash[y][j]) {
                            int res = Arrays.binarySearch(hash[x], i + 1, X_len, hash[y][j]);
                            if (res >= 0) {
                                i = res;
                            } else {
                                i = Math.max(i + 1, - res - 2);
                            }
                        } else {
                            int res = Arrays.binarySearch(hash[y], j + 1, Y_len, hash[x][i]);
                            if (res >= 0) {
                                j = res;
                            } else {
                                j = Math.max(j + 1, - res - 2);
                            }
                        }

                        continue;
                    }

                    boolean b1, b2;
                    int cmp = 0;
                    common = Math.max(common, 12);
                    common = Math.min(common, X_len -A[i]);
                    common = Math.min(common, Y_len -B[j]);

                    while (true) {
                        b1 = A[i] + common < X_len;
                        b2 = B[j] + common < Y_len;
                        if (b1 && b2 && (cmp = (int) X.charAt(A[i] + common) - Y.charAt(B[j] + common)) == 0) {
                            common++;
                        } else {
                            break;
                        }
                    }
                    if (common > max) {
                        max = common;
                    }
                    if (!b1 && !b2) {
                        i++;
                        j++;
                    } else if (!b1) {
                        j++;
                    } else if (!b2) {
                        i++;
                    } else if (cmp < 0) {
                        i++;
                    } else {
                        j++;
                    }
                }
                cache.put(cacheKey, max);
            }

            out.append(max).append('\n');
        }

        System.out.println(System.currentTimeMillis() - t0);t0 = System.currentTimeMillis();
        return out;
    }
}
