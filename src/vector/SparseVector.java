package vector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class SparseVector implements Vector {
  private static final long serialVersionUID = 8591745505666264662L;
  private static double c = 0;
  public double sum = 0.0;
  private List<Double> vec;



  /*
   * public SparseVector(int length) { this.vec = new ArrayList(); }
   */
  public SparseVector(double[] arr) {
    this.vec = DoubleStream.of(arr).boxed().collect(Collectors.toList());

  }

  @Override
  public int length() {
    return 0;
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

  public void printVector() {
    System.out.println(this.vec.toString());
  }

  public void addConstant(double C) {
    this.vec = this.vec.parallelStream().map(item -> item + C).collect(Collectors.toList());
  }
  /*
  private static void mapKahanSum(){
    var y = input[i] - c;         // c is zero the first time around.
    var t = sum + y;              // Alas, sum is big, y small, so low-order digits of y are lost.
    c = (t - sum) - y;
  }
  */
  /*
  private void kahanSum() {
    this.vec.stream().map(Double::valueOf)
    .reduce(0, (sum, e) -> {
      double y = e - c;
      double t = sum + y;
      c = t - sum - y; 
      sum = t;
    });
  }
  */
  public void calculateSum() {
    this.sum = this.vec.parallelStream()
                       .reduce(Double::sum).get();
  }
}
