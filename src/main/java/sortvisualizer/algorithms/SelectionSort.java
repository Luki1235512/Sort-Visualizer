package sortvisualizer.algorithms;

import sortvisualizer.SortArray;

public class SelectionSort implements ISortAlgorithm {

    private long stepDelay = 120;

    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public long getDelay() {
        return stepDelay;
    }

    @Override
    public void setDelay(long delay) {
        this.stepDelay = delay;
    }

    @Override
    public void runSort(SortArray array) {
        int len = array.arraySize();

        for (int i = 0; i < len - 1; i++) {
            int index = i;
            for (int j = i + 1; j < len; j++) {
                if (array.getValue(j) < array.getValue(index)) {
                    index = j;
                }
            }

            array.swap(i, index, getDelay(), true);

        }

    }
}
