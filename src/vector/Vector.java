package vector;

import java.io.Serializable;

public interface Vector extends Cloneable, Serializable {

    public int length();

    public boolean isTuple();

    public boolean isSparse();

    public boolean isDense();

    /*
    public default Vector getSuitableVector(Vector vec) throws CloneNotSupportedException {
        if(this.isTuple()){
            return (Vector) this.clone();
        }
        if(vec == null && this.isSparse()){
            return new SparseVector(vec.vec);
        }else if(this.isDense()){
            return new DenseVector(vec);
        }
        return vec;
    }
    */
    
    public Vector sortedVector();

    public Double get(int index);

    public void set(int index, double value);

    public void delete(int index);

    public void printVector();

    public void generateStatistics();

    public void calculateSum();

    public void calculateVariance();

    public void calculateMean();

    public Double calculateMedian();

    public Double calculateSkewness();

    public Double calculateKurtosis();

    public Double min();

    public Double max();

    public Vector dot(Vector v);

}
