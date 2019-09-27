package vector;

public class KeyValue {
    private int key;
    private double value;

    public KeyValue(int key, double value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
