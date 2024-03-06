package Vector;

import java.util.ArrayList;

public interface Vector<T> {
    Vector<T> cloneVec();
    ArrayList<T> toList();
}
