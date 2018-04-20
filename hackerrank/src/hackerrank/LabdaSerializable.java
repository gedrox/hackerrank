package hackerrank;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

public class LabdaSerializable {

    public static void main(String[] args) throws IOException {
        TreeSet<Integer> set = new TreeSet<>((Comparator<Integer> & Serializable) (a, b) -> a - b);
        FileOutputStream fileOut =
                new FileOutputStream("employee.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(set);
        out.close();
        fileOut.close();
    }
}
