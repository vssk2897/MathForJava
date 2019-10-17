package vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


public class DenseVector implements Vector {

  private static final long serialVersionUID = 85516547517878769L;
  private List<Double> vec;
  private int size;
  public Double mean;
  public Double sum;
  public Double median;
  public Double mode;
  public Double variance;
  public Double sum_corrected;

  public DenseVector(double[] arr) {
    this.vec = DoubleStream.of(arr)
                          .boxed()
                          .collect(Collectors.toList());
    this.size=arr.length;
  }

  public DenseVector(Double[] arr) {
   
  }
 
  public DenseVector(List<Double> vec) {
    this.vec = List.copyOf(vec);
    this.size = vec.size();
  }

  @Override
  public int length() {
    return this.size;
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
    return true;
  }


  @Override
  public Double get(int index) {
    return this.vec.get(index);
  }

  @Override
  public void set(int index, double value) {
    if(index > this.size - 1 || index < 0)
      throw new IndexOutOfBoundsException(index + " does not fit in [0," + this.size + ")");
    this.vec.set(index, Double.valueOf(value));
  }

  @Override
  public void delete(int index) {
    this.vec.remove(index);
  }

  @Override
  public DenseVector sortedVector() {
    Collections.sort(this.vec, (a, b) -> a < b ? -1 : a == b ? 0 : 1);
    return this;
  }

  @Override
  public void printVector() {
    System.out.println(this.vec.toString());
  }

  public void addConstant(double C) {
    this.vec = this.vec.parallelStream()
                      .map(item -> item + C)
                      .collect(Collectors.toList());
  }


  public void kahanSum() {
    /*
    this.vec.stream()
            .map(Double::valueOf)
            .forEach((e) -> {
            double y = e - c;
            double t = sum + y;
            c = t - sum - y; 
            sum = t;
          });
    */
    Double c = 0.0;
    Double sum = 0.0;
    for(int i = 0; i < this.length(); i++) {
      Double y = this.get(i) - c;
      Double t = sum + y;
      c = t - sum - y; 
      sum = t;
    }
    this.sum_corrected = sum;
  }
  
  @Override
  public void calculateSum() {
    this.sum = this.vec.parallelStream()
                       .reduce(Double::sum)
                       .get();
  }

  @Override
  public void calculateMean() {
    this.mean = this.sum/this.size;
  }
  
  @Override
  public void calculateVariance() {
    this.variance = (this.vec.parallelStream()
                            .map(item -> Math.pow(item - this.mean, 2))
                            .reduce(0.0, Double::sum)) / this.size;
  }

  @Override
  public Double calculateMedian() {
    DenseVector vec = sortedVector();
    if(vec.size % 2 != 0)
        return vec.get(vec.size / 2);
    else
        return vec.get(vec.size / 2) / 2 + vec.get(vec.size / 2 + 1) / 2;
  }

  @Override
  public void generateStatistics() {
    this.calculateSum();
    this.calculateMean();
    this.median = this.calculateMedian();
    this.calculateVariance();
  }
}
