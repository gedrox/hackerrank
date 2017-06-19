package todd_and_steven;

import java.util.Arrays;
import java.util.Random;

// Sample input 1, in Java.
public class todd_and_steven {

  static Random R = new Random(0);
  
  public static final int T = 100;
  public static final int S = 100;
  
  static long[] t = new long[T];
  static long[] s = new long[S];

  static {
    for (int i = 0; i < T; i++) {
      t[i] = R.nextInt(10) * 2 + (i == 0 ? 0 : t[i - 1]);
    }
    for (int i = 0; i < S; i++) {
      s[i] = R.nextInt(10) * 2 + (i == 0 ? 1 : s[i - 1]);
    }

//    System.err.println(Arrays.toString(t));
//    System.err.println(Arrays.toString(s));
  }

  public static long GetToddLength() {
    return T;
  }

  public static long GetStevenLength() {
    return S;
  }

  public static long GetToddValue(long i) {
    return t[(int) i];
  }

  public static long GetStevenValue(long i) {
    return s[(int) i];
  }
}