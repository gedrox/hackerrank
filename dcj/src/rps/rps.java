package rps;

public class rps {
    public rps() {
    }

    public static long GetN() {
        return 28;
    }

    public static char GetFavoriteMove(long id) {
        return new char[]{'R', 'P', 'S'}[(int) (id % 3)];
//        switch (id) {
//            case 0: return 'S';
//            case 1: return 'R';
//            case 2: return 'P';
//            case 3: return 'P';
//            default: return new char[]{'R', 'P', 'S'}[id % 3];
//        }
    }
}
