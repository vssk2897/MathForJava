package vector;

import java.security.InvalidParameterException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HashedVector extends SparseVector {
  
  /**
   *
   */
  private static final long serialVersionUID = -8427617770183942084L;
  private Map<Integer, Double> hmap;

  public HashedVector(double[] arr) {
    super(arr);
    if(arr.length == 0) {
      throw new InvalidParameterException(Error.VECTOR_INITIALIZATION_ERROR);
    }
    this.hmap = new HashMap<Integer, Double>();
  }

  public HashedVector(List<Double> vec) {
    super(vec);
    if(vec.size() == 0) {
      throw new InvalidParameterException(Error.VECTOR_INITIALIZATION_ERROR);
    }
    this.hmap = new HashMap<Integer, Double>();
  }

  public HashedVector(Double[] arr) {
    super(arr);
    if(arr.length == 0) {
      throw new InvalidParameterException(Error.VECTOR_INITIALIZATION_ERROR);
    }
    this.hmap = new HashMap<Integer, Double>();
  }

  public Double get(Integer index) {
    return this.hmap.get(index);
  }

  public void set(Integer index, Double value) {
    this.hmap.put(index, value);
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
    System.out.println(this.hmap.toString());
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

  @Override
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