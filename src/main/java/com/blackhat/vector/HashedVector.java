package com.blackhat.vector;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.blackhat.lib.*;
public class HashedVector extends SparseVector {
  
  /**
   *
   */
  private static final long serialVersionUID = -8427617770183942084L;
  private Map<Integer, Double> hmap;

  public HashedVector(double[] arr) {
    super(arr);
    this.log = new Log(HashedVector.class.getName());
    if(arr.length == 0) {
      this.log.fatal(Error.VECTOR_INITIALIZATION_ERROR, new InvalidParameterException());
    }
    this.hmap = new HashMap<Integer, Double>();
  }

  public HashedVector(List<Double> vec) {
    super(vec);
    this.log = new Log(HashedVector.class.getName());
    if(vec.size() == 0) {
      this.log.fatal(Error.VECTOR_INITIALIZATION_ERROR, new InvalidParameterException());
    }
    this.hmap = new HashMap<Integer, Double>();

  }

  public HashedVector(Double[] arr) {
    super(arr);
    this.log = new Log(HashedVector.class.getName());
    if(arr.length == 0) {
      this.log.fatal(Error.VECTOR_INITIALIZATION_ERROR, new InvalidParameterException());
    }
    this.hmap = new HashMap<Integer, Double>();
  }

  public Double get(Integer index) {
    if(this.hmap.containsKey(index))
      return this.hmap.get(index);
    this.log.warn(Error.HASHED_VECTOR_GET_ERROR);
    return null;
  }

  public void set(Integer index, Double value) {
    try{
    this.hmap.put(index, value);
    } catch(Exception exception) {
      this.log.error(Error.HASHED_VECTOR_SET_ERROR, exception);
    }
  }

  public Set<Integer> getIndices() {
    return this.hmap.keySet(); 
  }

  public boolean doesContain(Integer index){
    return this.hmap.containsKey(index);
  }

  public int lengthSparse() {
    return this.size;
  }
  
  public void printHashedVector() {
    log.info(this.hmap.toString());
  }

  public void createHashedVector() {
    // Have to implement a faster version with streams
    Double val;
    for(int i=0; i < this.size; i++) {
      val = this.get(i);
      if(val !=0) {
        this.hmap.put(i, val);
      }
    }    
  }

  
  public Double dotProduct(HashedVector vec1, HashedVector vec2) {
    if(vec1.lengthSparse() < vec2.lengthSparse())
      return multiply(vec1, vec2);
    else 
      return multiply(vec2, vec1);
  }

  private Double multiply(HashedVector vec1, HashedVector vec2) {
    return vec1.getIndices().parallelStream()
                    .filter(index -> vec2.doesContain(index))
                    .mapToDouble(item -> vec1.get(item) * vec2.get(item))
                    .sum();
  }
}