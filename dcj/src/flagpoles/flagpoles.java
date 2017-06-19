package flagpoles;

import java.util.Random;

// Sample input 1, in Java.
public class flagpoles {
  
  static Random r = new Random(1);
  static long L = 100L;
  static long[] H = new long[(int) L];
  static {
    for (long i = 0; i < L; i++) {
      H[(int) i] = r.nextInt(3);
    }
  }
  
  public flagpoles() {
  }

  public static long GetNumFlagpoles() {
    return L;
  }

  public static long GetHeight(long i) {
//    return H[(int) i];
    return (i + 25) % 50;
  }
}