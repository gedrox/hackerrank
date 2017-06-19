package tollcostdigits;

import java.util.Arrays;

public class Population {
    public int solve(int[] birth, int[] death) {
        TimePoint[] tp = new TimePoint[birth.length + death.length];
        int i = 0;
        for (int y : birth) {
            tp[i++] = new TimePoint(y, TimePoint.BIRTH);
        }
        for (int y : death) {
            tp[i++] = new TimePoint(y, TimePoint.DEATH);
        }
        Arrays.sort(tp);
        int alive = 0;
        int max = 0;
        for (TimePoint t : tp) {
            if (t.type == TimePoint.BIRTH) alive++;
            else alive--;
            max = Math.max(max, alive);
        }
        return max;
    }
    
    private static class TimePoint implements Comparable<TimePoint> {
        public static final int BIRTH = 0, DEATH = 1;
        int year;
        int type;
        
        TimePoint(int year, int type) {
            this.year = year;
            this.type = type;
        }
        
        @Override
        public int compareTo(TimePoint that) {
            int res = this.year - that.year;
            if (res != 0) return res;
            return this.type - that.type;
        }
    }
}
