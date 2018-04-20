package hackerrank;

@SuppressWarnings("unused")
public class Oid implements Comparable<Oid> {

    private final long id;

    public Oid(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Oid that) {
        return (int) (this.id - that.id);
    }

    @Override
    public boolean equals(Object that) {
        return this == that;
    }

    @Override
    public int hashCode() {
        return (int) (id % 1000);
    }
}
