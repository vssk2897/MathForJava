package vector;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomVector implements Vector {
  private static final long serialVersionUID = 5698565322324L;
  private int size;
  private List<Double> vec;
  private Double sum;
  private Double mean;
  private Double median;
  private Double mode;
  private Double variance;
  private Double skew;
  private Double kurtosis;
  
  public RandomVector(int length) {
    this.vec = ThreadLocalRandom.current()
                    .doubles((long) length)
                    .boxed()
                    .collect(Collectors.toList());
    this.size = length;
  }

  public RandomVector(int length, double origin, double bound) {
    this.vec = ThreadLocalRandom.current()
                                .doubles((long) length, origin, bound)
                                .boxed()
                                .collect(Collectors.toList());
    this.size =  length;                    
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
    return true;
  }

  @Override
  public boolean isDense() {
    return false;
  }


  @Override
  public Double get(int index) {
    return this.vec.get(index);
  }

  @Override
  public void set(int index, double value) {
    throw new UnsupportedOperationException("Set operation is not permittable on Random Vector");
  }

  @Override
  public void delete(int index) {
    throw new UnsupportedOperationException("delete operation is not permittable on Random Vector");
  }

  @Override
  public RandomVector sortedVector() {
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

  @Override
  public Double min() {
      return this.vec.stream()
                      .reduce(Double.MAX_VALUE,Double::min);
  }

  @Override
  public Double max()
  {   
      return this.vec.stream()
                      .reduce(Double.MIN_VALUE,Double::max);
  }

  @Override
  public void calculateSum() {
    this.sum = this.vec.parallelStream()
                       .reduce(Double::sum)
                       .get();
  }

  @Override
  public void calculateMean() {
    this.mean = this.sum / this.size;
  }
  
  @Override
  public void calculateVariance() {
    this.variance = (this.vec.parallelStream()
                            .map(item -> Math.pow(item - this.mean, 2))
                            .reduce(0.0, Double::sum)) / this.size;
  }

  @Override
  public Double calculateMedian() {
    RandomVector vec = sortedVector();
    if(vec.size % 2 != 0)
        return vec.get(vec.size / 2);
    else
        return vec.get(vec.size / 2) / 2 + vec.get(vec.size / 2 + 1) / 2;
  }

  @Override
  public Double calculateSkewness() {
    Double numerator = this.vec.stream()
                              .parallel()
                              .map(item -> Math.pow(item - this.mean, 3))
                              .reduce(0.0, Double::sum) / this.size;
    Double denominator = this.vec.stream()
                                .parallel()
                                .map(item -> Math.pow(item - this.mean, 2))
                                .reduce(0.0, Double::sum) / this.size;
    Double skew = numerator / Math.pow(denominator,1.5);
    if(this.size > 3) {
      skew = Math.sqrt((this.size * (this.size - 1)) / (this.size - 2) * skew);
    }
    return skew;  
  }

  @Override
    public Double calculateKurtosis() {
      Double numerator = this.vec.parallelStream()
                                .map(item -> Math.pow(item - this.mean, 4))
                                .reduce(0.0, Double::sum) / this.size ;
      
      Double denominator = this.vec.parallelStream()
                                  .map(item -> Math.pow(item - this.mean, 2))
                                  .reduce(0.0, Double::sum) / this.size;
      
      return numerator / Math.pow(denominator, 2) - 3;
    }

  @Override
  public void generateStatistics() {
    this.calculateSum();
    this.calculateMean();
    this.median = this.calculateMedian();
    this.calculateVariance();
    this.skew = this.calculateSkewness();
    this.kurtosis = this.calculateKurtosis();
  }
}