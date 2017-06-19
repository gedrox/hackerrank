package w31;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SplitPlane {
    /*
    public static void mainTest(String[] args) throws IOException {
        // seed 0, n=30, should be 9, says 10
        Random r = new Random(1);
        int n = 30;

        ArrayList<VerticalSegment> vs = new ArrayList<>();
        ArrayList<HorizontalSegment> hs = new ArrayList<>();
        int bound = 20;
        for (int i = 0; i < n / 2; i++) {
            vs.add(new VerticalSegment(r.nextInt(bound), r.nextInt(bound), r.nextInt(bound)));
            hs.add(new HorizontalSegment(r.nextInt(bound), r.nextInt(bound), r.nextInt(bound)));
        }
        int res = solve(hs, vs);
        System.out.println(res);

        Runnable runnable = () -> {
            LineComponent lineComponent = new LineComponent(420, 420);
            for (HorizontalSegment h : hs) {
                lineComponent.addLine(20 * h.x1() + 10, 20 * h.y1() + 10, 20 * h.x2() + 10, 20 * h.y2() + 10);
            }
            for (VerticalSegment v : vs) {
                lineComponent.addLine(20 * v.x1() + 10, 20 * v.y1() + 10, 20 * v.x2() + 10, 20 * v.y2() + 10);
            }
            JOptionPane.showMessageDialog(null, lineComponent);
        };
        SwingUtilities.invokeLater(runnable);

    }
    */

    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("src/w31/big.in"));
        int q = Integer.parseInt(br.readLine());
        
        long t0 = System.currentTimeMillis();
        
        for (int a0 = 0; a0 < q; a0++) {
            int n = Integer.parseInt(br.readLine());

            ArrayList<HorizontalSegment> hs = new ArrayList<>(n / 2);
            ArrayList<VerticalSegment> vs = new ArrayList<>(n / 2);

            for (int a1 = 0; a1 < n; a1++) {
                String[] split = br.readLine().split(" ");
                int x1 = Integer.parseInt(split[0]);
                int y1 = Integer.parseInt(split[1]);
                int x2 = Integer.parseInt(split[2]);
                int y2 = Integer.parseInt(split[3]);

                Segment segment = Segment.create(x1, x2, y1, y2);
                if (segment instanceof VerticalSegment) {
                    vs.add((VerticalSegment) segment);
                } else {
                    hs.add((HorizontalSegment) segment);
                }
            }

            long result = solve(hs, vs);

            System.out.println(result);
        }

        System.err.println(System.currentTimeMillis() - t0);
    }

    static long solve(ArrayList<HorizontalSegment> hs, ArrayList<VerticalSegment> vs) {
        merge(vs);
        merge(hs);
        
        int hSize = hs.size();
        int vSize = vs.size();

        ArrayList<P> points = new ArrayList<>(2 * (hs.size() + vs.size()));
        for (VerticalSegment segment : vs) {
            points.add(new VS(segment.x1(), segment.y1(), segment.y2()));
        }
        for (int i = 0; i < hs.size(); i++) {
            HorizontalSegment segment = hs.get(i);
            points.add(new HP1(segment.x1(), segment.y1(), i));
            points.add(new HP2(segment.x2(), segment.y2(), i));
        }

        points.sort(Comparator.<P>comparingInt((p) -> p.x)
                .thenComparingInt((p) -> p instanceof HP1 ? 0 : (p instanceof VS ? 1 : 2)));
        
        ArrayList<Integer> whys = new ArrayList<>();
        
        TreeMap<Integer, Integer> hId = new TreeMap<>();
        int[] color = new int[hSize];
//        HashMap<Integer, TreeSet<Integer>> byColor = new HashMap<>();
        TreeSet<Integer>[] byColor = new TreeSet[hSize];
        int[] cSize = new int[hSize];
        for (int i = 0; i < hSize; i++) {
            color[i] = i;
            byColor[i] = new TreeSet<>(); 
        }
        
        int X = 0;
        int N = hSize + vSize;
        int colorCount = hSize;
        
        long s = 0;
        long s1 = 0;
        long s2 = 0;
        long s3 = 0;
        int c1 = 0, c2 = 0, c3 = 0;

        for (P point : points) {
            long t1 = System.currentTimeMillis();
            if (point instanceof HP1) {
                int Y = ((HP1) point).y;
                int ID = ((HP1) point).id;
                
                hId.put(Y, ID);
                whys.add(Y);
                int C = get(color, ID);
                byColor[C].add(Y);
                s1 += System.currentTimeMillis() - t1;
                c1++;
            }
            if (point instanceof HP2) {
                int Y = ((HP2) point).y;
                int ID = ((HP2) point).id;
                
                hId.remove(Y);
                whys.remove((Integer) Y);
                int C = get(color, ID);
                byColor[C].remove(Y);
                s2 += System.currentTimeMillis() - t1;
                c2++;
            }
            
            if (point instanceof VS) {
                int Y1 = ((VS) point).y1;
                int Y2 = ((VS) point).y2;

//                NavigableSet<Integer> crosses = hId.navigableKeySet().subSet(Y1, true, Y2, true);

                Collections.sort(whys);
                int fasterSize = Collections.binarySearch(whys, Y2) - Collections.binarySearch(whys, Y1);
                
                int A = Collections.binarySearch(whys, Y1);
                if (A < 0) A = -A - 1;
                int B = Collections.binarySearch(whys, Y2);
                if (B < 0) B = -B - 2;
                
                
//                int crossSize = crosses.size();
                
                // if no crosses, N--
                if (A > B) {
                    N--;
                    continue;
                }
                
                int crossSize = B - A + 1;
                List<Integer> crosses = whys.subList(A, B + 1);

//                s3 += System.currentTimeMillis() - t1;
//                c3++;


                X += crossSize;

                
                if (crossSize == 1) {
                    continue;
                }

//                long t0 = System.currentTimeMillis();
                int C = -1;
//                int maxSize = 0;

//                int C0 = get(color, hId.get(crosses.get(0)));
//                if (crossSize == byColor.get(C0).subSet(Y1, true, Y2, true).size()) {
//                    // nothing to repaint
////                    System.err.println("continue");
//                    
//                    continue;
//                }
                
//                ArrayList<Integer> colorsToMerge = new ArrayList<>();
//                for (int C1 : byColor.keySet()) {
//                    NavigableSet<Integer> subset = byColor.get(C1).subSet(Y1, true, Y2, true);
//                    if (!subset.isEmpty()) {
//                        colorsToMerge.add(C1);
//                        if (subset.size() > maxSize) {
//                            C = C1;
//                            maxSize = subset.size();
//                        }
//                    }
//                }

//                System.err.println(colorsToMerge.size());

//                for (int C1 : colorsToMerge) {
//                    if (C1 != C) {
//                        colorCount--;
//                        color[C1] = C;
//                        byColor.get(C).addAll(byColor.get(C1));
//                        byColor.remove(C1);
//                    }
//                }
//                System.err.println("OK");
                
//                System.err.println("crosses");
                for (Integer crossY : crosses) {
//                    System.err.println("crossY=" + crossY);
                    int id = hId.get(crossY);
                    int C1 = get(color, id);
                    if (C == -1) {
                        C = C1;
                        if (crosses.size() == byColor[C].subSet(Y1, true, Y2, true).size()) {
//                            System.err.println("could break");
                            break;
                        }
                    } else {
                        if (C1 != C) {
                            if (cSize[C] < cSize[C1]) {
                                int tmp = C;
                                C = C1;
                                C1 = tmp;
                            }
                            cSize[C] += cSize[C1] + 1;
                            color[C1] = C;
                            colorCount--;
//                            System.err.println("color count--");

                            byColor[C].addAll(byColor[C1]);
                            byColor[C1].clear();
                        }
                    }
                }
                s3 += System.currentTimeMillis() - t1;
                c3++;
            }
        }

        System.err.println(s1 + " " + c1);
        System.err.println(s2 + " " + c2);
        System.err.println(s3 + " " + c3);
        
        
        return X - N + colorCount + 1;
    }
    
    static int get(int[] color, int id) {
        if (color[id] == id) return id;
        int c = get(color, color[id]);
        color[id] = c;
        return c;
    }

    static void merge(ArrayList<? extends Segment> segments) {
        segments.sort(Comparator.<Segment, Integer>comparing((s) -> s.fixed).thenComparing((s) -> s.from));
        Segment prev = null;
        for (Iterator<? extends Segment> iterator = segments.iterator(); iterator.hasNext(); ) {
            Segment segment = iterator.next();
            if (prev != null) {
                if (prev.fixed == segment.fixed && prev.to >= segment.from) {
                    prev.to = Math.max(prev.to, segment.to);
                    iterator.remove();
                    continue;
                }
            }
            prev = segment;
        }
    }

    static abstract class P {
        int x;
    }

    static class HP1 extends P {
        int y;
        int id;

        public HP1(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }

    static class HP2 extends P {
        int y;
        int id;

        public HP2(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }

    static class VS extends P {
        int y1;
        int y2;

        public VS(int x, int y1, int y2) {
            this.x = x;
            this.y1 = y1;
            this.y2 = y2;
        }
    }

    public static abstract class Segment {
        int from;
        int to;
        int fixed;

        static Segment create(int x1, int x2, int y1, int y2) {
            if (x1 == x2) {
                return new VerticalSegment(y1, y2, x1);
            } else {
                return new HorizontalSegment(x1, x2, y1);
            }
        }

    }

    public static class VerticalSegment extends Segment {
        public VerticalSegment(int y1, int y2, int x) {
            this.from = Math.min(y1, y2);
            this.to = Math.max(y1, y2);
            this.fixed = x;
        }

        public int x1() {
            return fixed;
        }

        public int x2() {
            return fixed;
        }

        public int y1() {
            return from;
        }

        public int y2() {
            return to;
        }
    }

    public static class HorizontalSegment extends Segment {
        public HorizontalSegment(int x1, int x2, int y) {
            this.from = Math.min(x1, x2);
            this.to = Math.max(x1, x2);
            this.fixed = y;
        }

        public int x1() {
            return from;
        }

        public int x2() {
            return to;
        }

        public int y1() {
            return fixed;
        }

        public int y2() {
            return fixed;
        }
    }
}
