package com.blackhat.vector;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;


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
  public Double skew;
  public Double kurtosis;

  public DenseVector(double[] arr) {
    if(arr.length ==0) {
      throw new InvalidParameterException(Error.VECTOR_INITIALIZATION_ERROR);
    }
    this.vec = DoubleStream.of(arr)
                          .boxed()
                          .collect(Collectors.toList());
    this.size=arr.length;
  }

  public DenseVector(Double[] arr) {
    if(arr.length ==0) {
      throw new InvalidParameterException(Error.VECTOR_INITIALIZATION_ERROR);
    }
    this.vec = Arrays.asList(arr);
    this.size = arr.length;
  }
 
  public DenseVector(List<Double> vec) {
    if(vec.size()==0) {
      throw new InvalidParameterException(Error.VECTOR_INITIALIZATION_ERROR);
    }
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
    if(index > this.size - 1 || index < 0)
      throw new IndexOutOfBoundsException(Error.INDEX_OUT_OF_BOUND + index);
    return this.vec.get(index);
  }

  @Override
  public void set(int index, double value) {
    if(index > this.size - 1 || index < 0)
      throw new IndexOutOfBoundsException(Error.INDEX_OUT_OF_BOUND + index);
    this.vec.set(index, Double.valueOf(value));
  }

  @Override
  public void delete(int index) {
    if(index > this.size - 1 || index < 0)
      throw new IndexOutOfBoundsException(Error.INDEX_OUT_OF_BOUND + index);
    this.vec.remove(index);
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

  public Double dotProduct(DenseVector vec2) {
    return IntStream.range(0, this.size)
                    .asDoubleStream()
                    .parallel()
                    .map(item -> this.get((int) item) * vec2.get((int)item))
                    .reduce(0.0, Double::sum);
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
