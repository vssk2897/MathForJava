package com.blackhat.vector;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import com.blackhat.lib.*;
public class SparseVector implements Vector {
  
  private static final long serialVersionUID = 8551945475136264769L;
  public Double sum = 0.0;
  public Double sum_corrected = 0.0;
  protected List<Double> vec;
  public int size;
  public Double mean;
  public Double median;
  public Double mode;
  public Double variance;
  public Double skew;
  public Double kurtosis;
  protected Log log;
  /*
  public SparseVector(int length) { 
    this.vec = new ArrayList<Double>(); 
  }
  */

  public SparseVector(List<Double> vec) {
    this.log = new Log(SparseVector.class.getName());
    if(vec.size() == 0) {
      this.log.fatal(Error.VECTOR_INITIALIZATION_ERROR, new InvalidParameterException());
    }
    this.vec = List.copyOf(vec);
    this.size = vec.size();
  }

  public SparseVector(double[] arr) {
    this.log = new Log(SparseVector.class.getName());
    if(arr.length == 0) {
      this.log.fatal(Error.VECTOR_INITIALIZATION_ERROR, new InvalidParameterException());
    }
    this.vec = DoubleStream.of(arr)
                          .boxed()
                          .collect(Collectors.toList());
    this.size=arr.length;
  }

  public SparseVector(Double[] arr) {
    this.log = new Log(SparseVector.class.getName());
    if(arr.length == 0) {
      this.log.fatal(Error.VECTOR_INITIALIZATION_ERROR, new InvalidParameterException());
    }
    this.vec = Arrays.asList(arr);
    this.size = this.vec.size();
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
    if(index > this.size - 1 || index < 0)
      this.log.error(Error.INDEX_OUT_OF_BOUND, new IndexOutOfBoundsException(index));
    return this.vec.get(index);
  }
    
  @Override
  public void set(int index, double value) {
    if(index > this.size - 1 || index < 0)
      this.log.error(Error.INDEX_OUT_OF_BOUND, new IndexOutOfBoundsException(index));
    this.vec.set(index, Double.valueOf(value));
  }

  @Override
  public void delete(int index) {
    if(index > this.size - 1 || index < 0)
      this.log.error(Error.INDEX_OUT_OF_BOUND, new IndexOutOfBoundsException(index));
    this.vec.remove(index);
  }

  @Override
  public SparseVector sortedVector() {
    Collections.sort(this.vec, (a, b) -> a < b ? -1 : a == b ? 0 : 1);
    return this;
  }

  @Override
  public void printVector() {
    log.info(this.vec.toString());
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
    SparseVector vec = sortedVector();
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

  public Double dotProduct(SparseVector vec2) {
    return IntStream.range(0, this.size)
                    .asDoubleStream()
                    .parallel()
                    .map(item -> this.get((int) item) * vec2.get((int)item))
                    .reduce(0.0, Double::sum);
  }
  
}
