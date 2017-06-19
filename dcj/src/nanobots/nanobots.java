package nanobots;

// Sample input 1, in Java.
public class nanobots {
  public nanobots() {
  }

  public static long GetRange() {
    return 10;
  }

  public static long GetNumNanobots() {
    return 4;
  }

  static long sizes[] = {8,6,2,5};
  static long speeds[] = {2,4,10,5};

  public static char Experiment(long size, long speed) {
    if (size < 1 || size > GetRange() || speed < 1 || speed > GetRange())
      throw new IllegalArgumentException("Invalid argument");
    for (int i = 0; i < 4; ++i)
      if (sizes[i] > size && speeds[i] > speed) return 'T';
    return 'E';
  }
}

