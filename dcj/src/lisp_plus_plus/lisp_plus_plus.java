package lisp_plus_plus;

// Sample input 1, in Java.
public class lisp_plus_plus {

  public static final String IN = "(()))";

  static {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1e6; i++) {
      sb.append("(");
    }
//    IN = sb.toString();
  }

  public static long GetLength() {
    return (long) IN.length();
  }

  public static char GetCharacter(long index) {
    return IN.charAt((int) index);
//    return index < 5e8-1 ? '(' : ')';
  }
}