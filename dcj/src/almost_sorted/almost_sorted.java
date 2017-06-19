package almost_sorted;

// Sample input 3, in Java.
public class almost_sorted {
  public almost_sorted() {
  }

  public static long NumberOfFiles() {
    return 5L;
  }

  public static long MaxDistance() {
    return 4L;
  }

  public static long Identifier(long i) {
    switch ((int)i) {
      case 0: return 1000000L;
      case 1: return 2000000L;
      case 2: return 4000000L;
      case 3: return 3000000L;
      case 4: return 5000000L;
      default: throw new IllegalArgumentException("Invalid argument");
    }
  }
}