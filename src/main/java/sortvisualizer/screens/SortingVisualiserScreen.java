package sortvisualizer.screens;

import sortvisualizer.Main;
import sortvisualizer.SortArray;
import sortvisualizer.algorithms.ISortAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class SortingVisualiserScreen extends Screen {

    private final SortArray sortArray;
    private final ArrayList<ISortAlgorithm> sortQueue;

    public SortingVisualiserScreen(ArrayList<ISortAlgorithm> algorithms, boolean playSounds, Main main) {
        super(main);
        setLayout(new BorderLayout());
        sortArray = new SortArray(playSounds);
        add(sortArray, BorderLayout.CENTER);
        sortQueue = algorithms;
    }

    private void longSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void shufflerAndWait() {
        sortArray.shuffle();
        sortArray.resetColours();
        longSleep();
    }

    public void onOpen() {
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (ISortAlgorithm algorithm : sortQueue) {
                    shufflerAndWait();

                    sortArray.setName(algorithm.getName());
                    sortArray.setAlgorithm(algorithm);

                    algorithm.runSort(sortArray);
                    sortArray.resetColours();
                    sortArray.highlightArray();
                    sortArray.resetColours();
                    longSleep();
                }
                return null;
            }

            @Override
            public void done() {
                main.popScreen();
            }
        };
        swingWorker.execute();
    }
}
