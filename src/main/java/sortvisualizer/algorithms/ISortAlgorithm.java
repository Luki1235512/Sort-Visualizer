package sortvisualizer.algorithms;

import sortvisualizer.SortArray;

public interface ISortAlgorithm {

    String getName();

    long getDelay();

    void setDelay(long delay);

    void runSort(SortArray array);

}
