package sortvisualizer.algorithms;

import sortvisualizer.SortArray;

public class InsertionSort implements ISortAlgorithm {

    private long stepDelay = 1;

    @Override
    public String getName() {
        return "Insertion Sort";
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
        for (int i = 0; i < array.arraySize(); i++) {
            int temp = array.getValue(i);
            int j = i - 1;
            while (j >= 0 && array.getValue(j) > temp) {
//            while (j >= 0 && temp <= array.getValue(j)) {
                array.updateSingle(j + 1, array.getValue(j), 5, true);
                j--;
            }
            array.updateSingle(j + 1, temp, getDelay(), true);
        }
    }
}
