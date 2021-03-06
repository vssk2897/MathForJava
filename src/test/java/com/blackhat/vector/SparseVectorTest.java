package com.blackhat.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.annotation.Annotation;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

import com.blackhat.lib.Log;
import com.blackhat.lib.StopWatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sparse Vector Test Suite")
public class SparseVectorTest implements VectorTest {

  Log log = new Log(SparseVectorTest.class.getName());
  double[] arr1;
  double[] arr2;

  @Override
  public String myFastTest() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    // TODO Auto-generated method stub
    return Test.class;
  }

 
  public Double multiply(double[] arr1, double[] arr2) {
    if(arr1.length != arr2.length) {
        throw new InvalidParameterException("Length of the two arrays should be same");
    }
    double sum = 0.0;
    for(int i=0; i< arr1.length; ++i) {
      sum += arr1[i] * arr2[i];
    }
    return Double.valueOf(sum);
  }

  @BeforeAll
  public void initVectors() {
    this.arr1 = new double[100];
    this.arr2 = new double[100];
    for(int i=0;i < 100 ;++i) {
      this.arr1[i] = i;
      this.arr2[i] = i;
    }
  }

  @Test
  public void dotProduct() {
  try {  
    SparseVector vec1 = new SparseVector(arr1);
    SparseVector vec2 = new SparseVector(arr2);
    StopWatch sw = StopWatch.start();
    vec1.dotProduct(vec2);
    System.out.println("Time taken for dot product of vector of length 100 is " + (sw.time()/1000000));
    assertEquals(this.multiply(arr1, arr2), vec1.dotProduct(vec2));
  } catch(Exception ex) {
    log.error("Error in testing" , ex);
  }
    
  }
  
}