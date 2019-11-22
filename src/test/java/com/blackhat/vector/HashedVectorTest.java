package com.blackhat.vector;

import java.lang.annotation.Annotation;
import java.security.InvalidParameterException;
import com.blackhat.lib.Log;
import com.blackhat.lib.StopWatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sparse Vector Test Suite")
public class HashedVectorTest implements VectorTest {

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
      this.arr1[i] = i+1;
      this.arr2[i] = i+1;
    }
  }

  @Test
  public void dotProduct() {
  try {  
    HashedVector vec1 = new HashedVector(arr1);
    HashedVector vec2 = new HashedVector(arr2);
    StopWatch sw = StopWatch.start();
    vec1.dotProduct(vec2);
    System.out.println("Time taken for dot product of vector of length 100 is " + (sw.time()/1000000.0));
    assertEquals(this.multiply(arr1, arr2), vec1.dotProduct(vec2));
  } catch(Exception ex) {
    log.error("Error in testing" , ex);
  }
    
  }

  /* This is calculated for arithmetic sequences arrays with
    commom difference 1
  */
  private Double reverseMultiply() {
    double sum = 0.0;
    for(int i = 1;i <= 100; ++i) {
      sum += i*(101-i);//Math.pow(i, 2) + 100 * i;
    }
    return Double.valueOf(sum);
  }

  void reverseArray(double arr[], int start, int end) 
  { 
      while (start < end) 
      { 
          double temp = arr[start];  
          arr[start] = arr[end]; 
          arr[end] = temp; 
          start++; 
          end--; 
      }  
  }  

  @Test
  public void reverseDotProduct() {
    StopWatch sw = StopWatch.start();
    //System.out.println("-----" + Arrays.toString(arr2) );
    reverseArray(arr2, 0, arr2.length - 1);

    //System.out.println("-----" + Arrays.toString(arr2) );
    //System.out.println("------------- " + Arrays.compare(arr2, t));
    HashedVector vec1 = new HashedVector(this.arr1);
    HashedVector vec2 = new HashedVector(arr2);
    vec1.dotProduct(vec2);
    System.out.println("Time taken for dot product of vector of length 100 is " + (sw.time()/1000000.0));
    double pa = this.reverseMultiply();
    assertEquals( Double.valueOf(pa), vec1.dotProduct(vec2));
  }
  
}