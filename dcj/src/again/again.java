package again;

// Sample input 3, in Java.
public class again {
  public again() {
  }

  public static long GetN() {
    return 1L;
  }

  public static long GetA(long i) {
    switch ((int)i) {
      case 0: return 1L;
      default: throw new IllegalArgumentException("Invalid argument");
    }
  }

  public static long GetB(long i) {
    switch ((int)i) {
      case 0: return 1L;
      default: throw new IllegalArgumentException("Invalid argument");
    }
  }
}