package query_of_death;

// Sample input 1, in Java.
public class query_of_death {

  static int L = 1000;
  public static final int BROKEN_NODE_ID = L-1;
  
  public query_of_death() {
  }

  static int isthenodebroken = 0;
  static int testvs[] = new int[L];
  
  static {
    for (int i = 0; i < L; i++) {
      testvs[i] = i % 2;
    }
  }

  public static long GetLength() {
    return L;
  }

  public static long GetValue(long i) {
    if (i < 0L || i >= GetLength())
      throw new IllegalArgumentException("Invalid argument");
    int val = (int)(testvs[(int)i]^((int)(Math.random() * 2 + 1) % (isthenodebroken + 1)));
    if (i == BROKEN_NODE_ID)
      isthenodebroken = 1;
    return val;
  }
}
