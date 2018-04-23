import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SolutionKiril {

    static int POINTS = 0;

    static String[] files = {
            "a_example.in",
            "b_should_be_easy.in",
            "c_no_hurry.in",
            "d_metropolis.in",
            "e_high_bonus.in"
    };

    public static void main(String[] args) throws FileNotFoundException {
        for (String f : files) {
            POINTS = 0;
            Scanner scanner = new Scanner(new FileInputStream("in/" + f));
            run(scanner);
        }
    }

    public static void run(Scanner scanner) {
        int r = scanner.nextInt(); // rows
        int c = scanner.nextInt(); // columns
        int f = scanner.nextInt(); // fleet
        int n = scanner.nextInt(); // rides
        int b = scanner.nextInt(); // bonus
        int t = scanner.nextInt(); // max steps

        Ride[] rides = new Ride[n];
        for (int i = 0; i < n; i++) {
            rides[i] = readRide(scanner);
            rides[i].index = i;
        }

        Arrays.sort(rides, (r1, r2) -> {
            return Integer.compare(r1.s, r2.s);
        });

//        List<List<Ride>> groupedRides = groupRides(rides);

        Car[] cars = new Car[f];
        for (int i = 0; i < f; i++) {
            cars[i] = new Car();
        }

//		for (List<Ride> line : groupedRides) {
//			for (Ride ride : line) {
//				moveCar(ride, cars, b);
//			}
//		}

        for (int i = 0; i < n; i++) {
            moveCar(rides[i], cars, b);
        }

        System.out.println(POINTS);
        
//        printSolution(cars);
    }

    private static List<List<Ride>> groupRides(Ride[] rides) {
        List<List<Ride>> groups = new ArrayList<>();

        List<Ride> group = new ArrayList<>();
        group.add(rides[0]);
        groups.add(group);

        int dist;
        int time;
        int minDist;
        Ride ride;
        List<Ride> best;
        for (int i = 1; i < rides.length; i++) {
            ride = rides[i];
            best = null;
            minDist = Integer.MAX_VALUE;
            for (List<Ride> line : groups) {
                Ride last = line.get(line.size() - 1);
                time = ride.s - ride.f;
                if (time >= 0) {
                    dist = Math.abs(last.x - ride.a) + Math.abs(last.y - ride.b);

                    if (minDist > dist) {
                        minDist = dist;
                        best = line;
                    }
                }
            }
            if (best == null) {
                group = new ArrayList<>();
                group.add(ride);
                groups.add(group);
            } else {
                best.add(ride);
            }
        }

        return groups;
    }

    private static void printSolution(Car[] cars) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cars.length; i++) {
            Car car = cars[i];
            builder.append(car.rides.size());
            for (int k : car.rides) {
                builder.append(' ');
                builder.append(k);
            }
            builder.append('\n');
        }
        System.out.println(builder.toString());
    }

    private static void moveCar(Ride ride, Car[] cars, int bonus) {
        Car bestBonus = null;
        Car bestEnd = null;
        int minWait = Integer.MAX_VALUE;
        int minEndTime = Integer.MAX_VALUE;
        int maxBonus = Integer.MIN_VALUE;

        int distance;
        int time;

        for (Car car : cars) {
            if (car.step <= ride.minStart) {
                time = ride.s - car.step; // time to start
                distance = Math.abs(car.x - ride.a) + Math.abs(car.y - ride.b); // distance to start

                // missing IF
                if (car.step + Math.max(time, distance) + ride.points > ride.f) continue;

                if (time >= distance) {
                    if (maxBonus < ride.points + bonus) {
                        maxBonus = ride.points + bonus;
                        bestBonus = car;
                    }
                } else if (maxBonus < ride.points) {
                    maxBonus = ride.points;
                    bestBonus = car;
                }

                int end = car.step + Math.max(time, distance) + ride.points;
                if (minEndTime > end) {
                    bestEnd = car;
                    minEndTime = end;
                }
            }
        }

        Car theBest = null;

        if (bestBonus != null && bestEnd != null) {
            time = ride.s - bestEnd.step; // time to start
            distance = Math.abs(bestEnd.x - ride.a) + Math.abs(bestEnd.y - ride.b); // distance to start

            int bonuseEnd;
            if (time >= distance) {
                bonuseEnd = ride.points + bonus;
            } else {
                bonuseEnd = ride.points;
            }

            time = ride.s - bestBonus.step; // time to start
            distance = Math.abs(bestBonus.x - ride.a) + Math.abs(bestBonus.y - ride.b); // distance to start

            int endTime = bestBonus.step + Math.max(time, distance) + ride.points;

            int timeDelta = Math.abs(endTime - minEndTime);
            int bonusDelta = Math.abs(bonuseEnd - maxBonus);
            if (bonusDelta < timeDelta) {
                theBest = bestEnd;
            } else {
                theBest = bestBonus;
            }
        } else if (bestEnd != null) {
            theBest = bestEnd;
        } else if (bestBonus != null) {
            theBest = bestBonus;
        }

        if (theBest != null) {
            time = ride.s - theBest.step;
            distance = Math.abs(theBest.x - ride.a) + Math.abs(theBest.y - ride.b);
            
            POINTS += ride.points;
            if (theBest.step + distance <= ride.s) POINTS += bonus;
            
            if (theBest.step + distance + ride.points > ride.f) System.err.println("BAD");

            theBest.x = ride.x;
            theBest.y = ride.y;
            theBest.step += Math.max(time, distance) + ride.points;
            theBest.rides.add(ride.index);
            
        }

        // if (best != null) {
        // time = ride.s - best.step;
        // distance = Math.abs(best.x - ride.a) + Math.abs(best.y - ride.b);
        //
        // best.x = ride.x;
        // best.y = ride.y;
        // best.step += Math.max(time, distance) + ride.bonus;
        // best.rides.add(ride.index);
        // }
    }

    private static Ride readRide(Scanner scanner) {
        Ride ride = new Ride();
        ride.a = scanner.nextInt();
        ride.b = scanner.nextInt();
        ride.x = scanner.nextInt();
        ride.y = scanner.nextInt();
        ride.s = scanner.nextInt();
        ride.f = scanner.nextInt();

        ride.points = Math.abs(ride.a - ride.x) + Math.abs(ride.b - ride.y);
        ride.minStart = ride.f - ride.points;
        return ride;
    }

    private static class Ride {
        int a;
        int b;
        int x;
        int y;
        int s;
        int f;

        int points;
        int minStart;
        int index;

        @Override
        public String toString() {
            return "Ride [a=" + a + ", b=" + b + ", x=" + x + ", y=" + y + ", s=" + s + ", f=" + f + ", bonnus=" + points
                    + "]";
        }
    }

    private static class Car {
        int x;
        int y;
        int step;

        List<Integer> rides = new ArrayList<>();
    }

}
