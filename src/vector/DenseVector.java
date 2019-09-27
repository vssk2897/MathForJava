package vector;

public class DenseVector implements Vector{
    public DenseVector(Vector vector) {
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public boolean isTuple() {
        return false;
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public boolean isDense() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public KeyValue next() {
        return null;
    }
}
