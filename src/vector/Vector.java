package vector;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public interface Vector extends Cloneable, Serializable {

    public int length();

    public boolean isTuple();

    public boolean isSparse();

    public boolean isDense();

    public default Vector getSuitableVector(Vector vec) throws CloneNotSupportedException {
        if(this.isTuple()){
            return (Vector) this.clone();
        }
        if(vec == null && this.isSparse()){
            return new SparseVector((Vector) this);
        }else if(this.isDense()){
            return new DenseVector((Vector) this);
        }
        return vec;
    }

}
