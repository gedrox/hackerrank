package froginmaze;

import java.util.*;

public class FrogInMaze {
    private static Cell[] cells;
    private static int n;
    private static int m;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        m = in.nextInt();
        int k = in.nextInt();

        cells = new Cell[n * m];
        int[] aleph = new int[2];

        for (int a0 = 0; a0 < n; a0++) {
            String row = in.next();
            for (int i = 0; i < m; i++) {
                char type = row.charAt(i);
                Cell c = Cell.create(type);
                if (type == 'A') aleph = new int[]{a0, i};
                c.x = a0;
                c.y = i;
                c.index = a0*m + i;
                cells[a0 * m + i] = c;
            }
        }
        for (int a0 = 0; a0 < k; a0++) {
            int i1 = in.nextInt() - 1;
            int j1 = in.nextInt() - 1;
            int i2 = in.nextInt() - 1;
            int j2 = in.nextInt() - 1;

            Cell c1 = cells[i1 * m + j1];
            Tunnel t1 = c1.toTunnel();
            Cell c2 = cells[i2 * m + j2];
            Tunnel t2 = c2.toTunnel();

            t1.otherSide = t2;
            t2.otherSide = t1;

            cells[i1 * m + j1] = t1;
            cells[i2 * m + j2] = t2;
        }

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                addNext(x, y, x - 1, y);
                addNext(x, y, x + 1, y);
                addNext(x, y, x, y - 1);
                addNext(x, y, x, y + 1);
            }
        }

        double alephProb = 0;

        HashMap<Cell, Double> state = new HashMap<>();
        for (Cell cell : cells) {
            if (cell instanceof Exit) {
                state.put(cell, 1d);
            }
        }
        
        while (true) {
            HashMap<Cell, Double> newState = new HashMap<>();
            double probSum = 0;
            for (Map.Entry<Cell, Double> cwp : state.entrySet()) {
                for (Cell prevCell : cwp.getKey().prev) {
                    double prob = cwp.getValue() / prevCell.next.size();
                    probSum += prob;
                    if (!newState.containsKey(prevCell)) {
                        newState.put(prevCell, prob);
                    } else {
                        newState.put(prevCell, newState.get(prevCell) + prob);
                    }
                    
                    if (prevCell.x == aleph[0] && prevCell.y == aleph[1]) alephProb += prob;
                }
            }
            
            if (probSum < 1e-10) break;
            state = newState;
        }

        System.out.println(alephProb);
    }

    private static void addNext(int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 >= n) return;
        if (y2 < 0 || y2 >= m) return;

        Cell s = cells[x1 * m + y1];
        Cell t = cells[x2 * m + y2];

        if (s instanceof Block) return;
        if (t instanceof Block) return;
        if (s instanceof Bomb) return;
        if (s instanceof Exit) return;

        if (t instanceof Tunnel) t = ((Tunnel) t).otherSide;

        s.next.add(t);
        t.prev.add(s);
    }

    static class Cell {
        int x, y, index;
        ArrayList<Cell> next = new ArrayList<>();
        ArrayList<Cell> prev = new ArrayList<>();

        static Cell create(char type) {
            switch (type) {
                case '#':
                    return new Block();
                case 'O':
                    return new Empty();
                case 'A':
                    return new Empty();
                case '%':
                    return new Exit();
                case '*':
                    return new Bomb();
                default:
                    throw new RuntimeException();
            }
        }

        Tunnel toTunnel() {
            Tunnel tunnel = new Tunnel();
            tunnel.x = this.x;
            tunnel.y = this.y;
            tunnel.index = this.index;
            return tunnel;
        }
    }

    static class Block extends Cell {
    }

    static class Exit extends Cell {
    }

    static class Bomb extends Cell {
    }

    static class Empty extends Cell {
    }

    static class Tunnel extends Cell {
        Tunnel otherSide;
    }
}
