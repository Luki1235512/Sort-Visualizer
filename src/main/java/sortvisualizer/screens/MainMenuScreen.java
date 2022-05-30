package sortvisualizer.screens;

import sortvisualizer.Main;
import sortvisualizer.algorithms.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public final class MainMenuScreen extends Screen {

    private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
    private final ArrayList<AlgorithmCheckBox> checkBoxes;

    public MainMenuScreen(Main main) {
        super(main);
        checkBoxes = new ArrayList<>();
        setUpGUI();
    }

    private void addCheckBox(ISortAlgorithm algorithm, JPanel panel) {
        JCheckBox box = new JCheckBox("", true);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setBackground(BACKGROUND_COLOR);
        box.setForeground(Color.WHITE);
        checkBoxes.add(new AlgorithmCheckBox(algorithm, box));
        panel.add(box);
    }

    private void initContainer(JPanel p) {
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setBackground(BACKGROUND_COLOR);
    }

    public void setUpGUI() {
        JPanel sortAlgorithmContainer = new JPanel();
        JPanel optionsContainer = new JPanel();
        JPanel outerContainer = new JPanel();
        initContainer(this);
        initContainer(optionsContainer);
        initContainer(sortAlgorithmContainer);

        outerContainer.setBackground(BACKGROUND_COLOR);
        outerContainer.setLayout(new BoxLayout(outerContainer, BoxLayout.LINE_AXIS));

        sortAlgorithmContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO: algos
        addCheckBox(new BubbleSort(), sortAlgorithmContainer);
        addCheckBox(new SelectionSort(), sortAlgorithmContainer);
        addCheckBox(new InsertionSort(), sortAlgorithmContainer);
        addCheckBox(new MergeSort(), sortAlgorithmContainer);
        addCheckBox(new QuickSort(), sortAlgorithmContainer);
        addCheckBox(new CountingSort(), sortAlgorithmContainer);

        JCheckBox soundCheckBox = new JCheckBox("Play Sounds");
        soundCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        soundCheckBox.setBackground(BACKGROUND_COLOR);
        soundCheckBox.setForeground(Color.WHITE);

        optionsContainer.add(soundCheckBox);

        JButton startButton = new JButton("Begin Visual Sorter");
        startButton.addActionListener((ActionEvent e) -> {
            ArrayList<ISortAlgorithm> algorithms = new ArrayList<>();
            for (AlgorithmCheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    algorithms.add(cb.getAlgorithm());
                }
            }
            main.pushScreen(
                    new SortingVisualiserScreen(
                            algorithms, soundCheckBox.isSelected(), main
                    )
            );
        });

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        outerContainer.add(optionsContainer);
        outerContainer.add(Box.createRigidArea(new Dimension(5, 0)));
        outerContainer.add(sortAlgorithmContainer);

        int gap = 15;
        add(Box.createRigidArea(new Dimension(0, gap)));
        add(outerContainer);
        add(Box.createRigidArea(new Dimension(0, gap)));
        add(startButton);
    }

    @Override
    public void onOpen() {
        checkBoxes.forEach((box) -> {
            box.unselect();
        });
    }

    private class AlgorithmCheckBox {
        private final ISortAlgorithm algorithm;
        private final JCheckBox box;

        public AlgorithmCheckBox(ISortAlgorithm algorithm, JCheckBox box) {
            this.algorithm = algorithm;
            this.box = box;
            this.box.setText(algorithm.getName());
        }

        public void unselect() {
            this.box.setSelected(false);
        }

        public boolean isSelected() {
            return this.box.isSelected();
        }

        public ISortAlgorithm getAlgorithm() {
            return this.algorithm;
        }



    }

}
