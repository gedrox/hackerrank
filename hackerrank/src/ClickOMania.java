//import org.junit.Test;

import java.util.LinkedList;
import java.util.Scanner;

public class ClickOMania {

    static int lastRemovedCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int R = sc.nextInt();
        int C = sc.nextInt();
        int colorCount = sc.nextInt();
        
        String[] str = new String[R];
        for (int i = 0; i < R; i++) {
            str[i] = sc.next();
        }

        char[][] grid = toGrid(str);

        int currLonely = lonelyCount(grid);

        int minLonely = Integer.MAX_VALUE;
        int maxRemoved = 0;
        int[] bestHit = null;

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                char firstHitColor = grid[r][c];
                char[][] result = hit(grid, r, c);
                int removed1 = lastRemovedCount;
                if (result != null && lastRemovedCount > 0) {
                    
                    if (countColor(result, firstHitColor) == 1) {
                        if (minLonely == Integer.MAX_VALUE) {
                            bestHit = new int[]{r, c};
                        }
                        continue;
                    }
                    
                    if (bestHit == null) {
                        bestHit = new int[]{r, c};
                    }

                    for (int r2 = 0; r2 < R; r2++) {
                        for (int c2 = 0; c2 < C; c2++) {

                            char secondHitColor = result[r2][c2];
                            char[][] result2 = hit(result, r2, c2);
                            int removed2 = lastRemovedCount;
                            if (result2 != null && lastRemovedCount > 0) {

                                int nowLonely = lonelyCount(result2);
                                if (nowLonely <= minLonely) {
                                    // try not leaving one
                                    if (countColor(result2, secondHitColor) == 1) {
                                        if (minLonely == Integer.MAX_VALUE) {
                                            bestHit = new int[]{r, c};
                                        }
                                        continue;
                                    } else if (nowLonely < minLonely || (removed1 + removed2) > maxRemoved) {
                                        minLonely = nowLonely;
                                        maxRemoved = (removed1 + removed2);
                                        bestHit = new int[]{r, c};
                                    }
                                }

                                for (int r3 = 0; r3 < R; r3++) {
                                    for (int c3 = 0; c3 < C; c3++) {

                                        char thirdHitColor = result2[r3][c3];
                                        char[][] result3 = hit(result2, r3, c3);
                                        int removed3 = lastRemovedCount;
                                        if (result3 != null && lastRemovedCount > 0) {

                                            nowLonely = lonelyCount(result3);
                                            if (nowLonely > minLonely) continue;

                                            // try not leaving one
                                            if (countColor(result3, thirdHitColor) == 1) {
                                                if (minLonely == Integer.MAX_VALUE) {
                                                    bestHit = new int[]{r, c};
                                                }
                                            } else if (nowLonely < minLonely || (removed1 + removed2 + removed3) > maxRemoved) {
                                                minLonely = nowLonely;
                                                maxRemoved = removed1 + removed2 + removed3;
                                                bestHit = new int[]{r, c};
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(bestHit[0] + " " + bestHit[1]);
    }

    private static int countColor(char[][] result, char c) {
        int count = 0;
        for (char[] chars : result) {
            for (char aChar : chars) {
                if (aChar == c) count++;
            }
        }
        return count;
    }
    
    private static char[][] toGrid(String[] str) {
        char[][] grid = new char[str.length][];
        for (int i = 0; i < str.length; i++) {
            grid[i] = str[i].toCharArray();
        }
        return grid;
    }

    static int lonelyCount(char[][] grid) {
        int cnt = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (!isEmpty(grid, r, c) && isLonely(grid, r, c)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private static boolean isEmpty(char[][] grid, int r, int c) {
        return grid[r][c] == '-';
    }

    private static boolean isLonely(char[][] grid, int r, int c) {
        char ch = grid[r][c];
        if (r > 0 && grid[r - 1][c] == ch) return false;
        if (c > 0 && grid[r][c - 1] == ch) return false;
        if (r < grid.length - 1 && grid[r + 1][c] == ch) return false;
        if (c < grid[r].length - 1 && grid[r][c + 1] == ch) return false;
        return true;
    }

    static char[][] hit(char[][] grid, int r, int c) {

        lastRemovedCount = 0;

        if (isEmpty(grid, r, c)) return null;
        if (isLonely(grid, r, c)) return null;

        grid = copyGrid(grid);
        LinkedList<int[]> q = new LinkedList<>();
        q.add(new int[]{r, c});

        while (!q.isEmpty()) {
            int[] cell = q.pollFirst();
            r = cell[0];
            c = cell[1];
            char ch = grid[r][c];
            if (ch == '-') continue;

            if (r > 0 && grid[r - 1][c] == ch) q.add(new int[]{r - 1, c});
            if (c > 0 && grid[r][c - 1] == ch) q.add(new int[]{r, c - 1});
            if (r < grid.length - 1 && grid[r + 1][c] == ch) q.add(new int[]{r + 1, c});
            if (c < grid[r].length - 1 && grid[r][c + 1] == ch) q.add(new int[]{r, c + 1});

            grid[r][c] = '-';
            lastRemovedCount++;
        }

        int c2 = 0;
        for (c = 0; c < grid[0].length; c++) {
            boolean emptyColumn = true;
            int r2 = grid.length - 1;
            for (r = grid.length - 1; r >= 0; r--) {
                if (grid[r][c] != '-') {
                    grid[r2][c2] = grid[r][c];
                    r2--;
                    emptyColumn = false;
                }
            }
            for (r = r2; r >= 0; r--) {
                grid[r][c] = '-';
            }
            if (!emptyColumn) {
                c2++;
            }
        }
        for (c = c2; c < grid[0].length; c++) {
            for (r = 0; r < grid.length; r++) {
                grid[r][c] = '-';
            }
        }

        return grid;
    }

    private static char[][] copyGrid(char[][] grid) {
        char[][] newGrid = new char[grid.length][];
        for (int i = 0; i < newGrid.length; i++) {
            newGrid[i] = grid[i].clone();
        }
        grid = newGrid;
        return grid;
    }
    
    static String out(char[][] grid) {
        StringBuilder out = new StringBuilder();
        for (char[] chars : grid) {
            out.append(chars).append('\n');
        }
        return out.toString();
    }
    
//    @Test
    public void test() {
        String[] str = (
                "BBRBRBRBBB\n" +
                "RBRBRBBRRR\n" +
                "RRRBBRBRRR\n" +
                "RBRBRRRBBB\n" +
                "RBRBRRRRBB\n" +
                "RBBRBRRRRR\n" +
                "BBRBRRBRBR\n" +
                "BRBRBBRBBB\n" +
                "RBBRRRRRRB\n" +
                "BBRBRRBBRB\n" +
                "BBBRBRRRBB\n" +
                "BRBRRBRRBB\n" +
                "BRRBBBBBRB\n" +
                "RRBBRRBRRR\n" +
                "RRRBRRRBBB\n" +
                "RRRRRBBBRR\n" +
                "BRRRBRRRBB\n" +
                "BBBBRBRRRB\n" +
                "BRBBBBBRBB\n" +
                "RRRRRBBRRR").split("\n");
        char[][] grid = toGrid(str);

        System.out.println(lonelyCount(grid));
        System.out.println(out(hit(grid, 1, 0)));

        str = (
                "BAC\n" +
                "BBA\n" +
                "BAA").split("\n");
        grid = toGrid(str);

        System.out.println(lonelyCount(grid));
        System.out.println(out(hit(grid, 0, 0)));
    }
}
