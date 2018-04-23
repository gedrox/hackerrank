import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Check extends SolutionWithReplace {
    public static void main(String[] args) throws FileNotFoundException {

        Check sol = new Check();
        sol.readInput(new Scanner(new File("in/d_metropolis.in")));
        sol.init();
        sol.doCheck();
    }
    
    void doCheck() throws FileNotFoundException {
        File f = new File("d_metropolis_12359757.out");
        Scanner sc = new Scanner(f);
        for (int f_i = 0; f_i < F; f_i++) {
            int c = sc.nextInt();
            
            for (int i = 0; i < c; i++) {
                int r_i = sc.nextInt();
                Option o = new Option(f_i, r_i);
                System.out.printf("We're going from %dx%d to %dx%d, distance %d, can start at %d, finish at %d%n",
                        f_pos[f_i].x, f_pos[f_i].y, ab[r_i].x, ab[r_i].y, o.distToStart, o.canStart, o.canFinish);
                o.take();
                
            }
            System.out.println("DRIVER TOTAL: " + POINTS_BY_CAR[f_i]);
            new Scanner(System.in).nextLine();
        }
    }
}
