package iterarateit;
import java.io.*;
import java.util.*;

public class Princess {

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            sc.nextLine();
            int p_x = -1, p_y = -1, m_x = -1, m_y = -1;

            for (int i = 0; i < n; i++) {
                String line = sc.nextLine();
                if (line.contains("p")) {
                    p_x = line.indexOf("p");
                    p_y = i;
                }
                if (line.contains("m")) {
                    m_x = line.indexOf("m");
                    m_y = i;
                }
            }

            List<String> moves = new ArrayList<>();
            for (int i = 0; i < p_x - m_x; i++) moves.add("RIGHT");
            for (int i = 0; i < -p_x + m_x; i++) moves.add("LEFT");
            for (int i = 0; i < -p_y + m_y; i++) moves.add("UP");
            for (int i = 0; i < p_y - m_y; i++) moves.add("DOWN");
            if (moves.isEmpty()) return;
            System.out.println(moves.get(new Random().nextInt(moves.size())));
        }
    }

