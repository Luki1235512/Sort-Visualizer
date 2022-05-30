package sortvisualizer;

import sortvisualizer.algorithms.ISortAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SortArray extends JPanel {

    public static final int DEFAULT_WIN_WIDTH = 1280;
    public static final int DEFAULT_WIN_HEIGHT = 720;
    private static final int DEFAULT_BAR_WIDTH = 5;

    private static final double BAR_HEIGHT_PERCENT = 512.0 / 720.0;
    private static final int NUM_BARS = DEFAULT_WIN_WIDTH / DEFAULT_BAR_WIDTH;

    private final int[] array;
    private final int[] barColours;
    private int spinnerValue = 0;
    private String algorithmName = "";
    private ISortAlgorithm algorithm;
    private long algorithmDelay = 0;

    private MidiSoundPlayer player;
    private JSpinner spinner;
    private boolean playSounds;

    private int arrayChanges = 0;

    public SortArray(boolean playSounds) {
        setBackground(Color.DARK_GRAY);
        this.array = new int[NUM_BARS];
        this.barColours = new int[NUM_BARS];
        for (int i = 0; i < NUM_BARS; i++) {
            this.array[i] = i;
            this.barColours[i] = 0;
        }
        this.player = new MidiSoundPlayer(NUM_BARS);
        this.playSounds = playSounds;
        this.spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        this.spinner.addChangeListener((event) -> {
            this.algorithmDelay = (Integer) this.spinner.getValue();
            this.algorithm.setDelay(this.algorithmDelay);
        });

        add(this.spinner, BorderLayout.LINE_START);
    }

    public int arraySize() {
        return this.array.length;
    }

    public int getValue(int index) {
        return this.array[index];
    }

    public int getMaxValue() {
        return Arrays.stream(array).max().orElse(Integer.MIN_VALUE);
    }

    private void finaliseUpdate(int value, long millisecondDelay, boolean isStep) {
        repaint();
        try {
            Thread.sleep(millisecondDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (this.playSounds) {
            this.player.makeSound(value);
        }
        if (isStep) {
            this.arrayChanges++;
        }
    }

    public void swap(int firstIndex, int secondIndex, long millisecondDelay, boolean isStep) {
        int temp = this.array[firstIndex];
        this.array[firstIndex] = this.array[secondIndex];
        this.array[secondIndex] = temp;

        this.barColours[firstIndex] = 100;
        this.barColours[secondIndex] = 100;

        this.finaliseUpdate((this.array[firstIndex] + this.array[secondIndex]) / 2, millisecondDelay, isStep);
    }

    public void updateSingle(int index, int value, long millisecondDelay, boolean isStep) {
        this.array[index] = value;
        this.barColours[index] = 100;

        this.finaliseUpdate(value, millisecondDelay, isStep);
        repaint();
    }

    public void shuffle() {
        this.arrayChanges = 0;
        Random rng = new Random();
        for (int i = 0; i < arraySize(); i++) {
            int swapWithIndex = rng.nextInt(arraySize() - 1);
            this.swap(i, swapWithIndex, 5, false);
        }
        arrayChanges = 0;
    }

    public void highlightArray() {
        for (int i = 0; i < arraySize(); i++) {
            updateSingle(i, getValue(i), 5, false);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIN_WIDTH, DEFAULT_WIN_HEIGHT);
    }

    public void resetColours() {
        for (int i = 0; i < NUM_BARS; i++) {
            barColours[i] = 0;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D panelGraphics = (Graphics2D) g.create();

        try {
            Map<RenderingHints.Key, Object> renderingHints = new HashMap<>();
            renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            panelGraphics.addRenderingHints(renderingHints);
            panelGraphics.setColor(Color.WHITE);
            panelGraphics.setFont(new Font("Monospaced", Font.BOLD, 20));
            panelGraphics.drawString(" Current algorithm: " + algorithmName, 10, 30);
            panelGraphics.drawString(" Current step delay: " + algorithmDelay + " ms", 10, 55);
            panelGraphics.drawString(" Array Changes: " + arrayChanges, 10, 80);

            drawBars(panelGraphics);
        } finally {
            panelGraphics.dispose();
        }
    }

    private void drawBars(Graphics2D panelGraphics) {
        int barWidth = getWidth() / NUM_BARS;
        int bufferedImageWidth = barWidth * NUM_BARS;
        int bufferedImageHeight = getHeight();

        if (bufferedImageHeight > 0 && bufferedImageWidth > 0) {
            if (bufferedImageWidth < 256) {
                bufferedImageWidth = 256;
            }

            double maxValue = getMaxValue();

            BufferedImage bufferedImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, BufferedImage.TYPE_INT_ARGB);
            makeBufferedImageTransparent(bufferedImage);
            Graphics2D bufferedGraphics = null;
            try {
                bufferedGraphics = bufferedImage.createGraphics();

                for (int x = 0; x < NUM_BARS; x++) {
                    double currentValue = getValue(x);
                    double percentOfMax = currentValue / maxValue;
                    double heightPercentOfPanel = percentOfMax * BAR_HEIGHT_PERCENT;
                    int height = (int) (heightPercentOfPanel * (double) getHeight());
                    int xBegin = x + (barWidth - 1) * x;
                    int yBegin = getHeight() - height;

                    int val = barColours[x] * 2;
                    if (val > 190) {
                        bufferedGraphics.setColor(new Color(255 - val, 255, 255 - val));
                    } else {
                        bufferedGraphics.setColor(new Color(255, 255 - val, 255 - val));
                    }
                    bufferedGraphics.fillRect(xBegin, yBegin, barWidth, height);
                    if (barColours[x] > 0) {
                        barColours[x] -= 5;
                    }
                 }
            }
            finally {
                if (bufferedGraphics != null) {
                    bufferedGraphics.dispose();
                }
            }
            panelGraphics.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);

        }
    }

    private void makeBufferedImageTransparent(BufferedImage image) {
        Graphics2D bufferedGraphics = null;
        try {
            bufferedGraphics = image.createGraphics();

            bufferedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
            bufferedGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            bufferedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }
        finally {
            if (bufferedGraphics != null) {
                bufferedGraphics.dispose();
            }
        }
    }

    @Override
    public void setName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setAlgorithm(ISortAlgorithm algorithm) {
        this.algorithm = algorithm;
        algorithmDelay = algorithm.getDelay();
        spinner.setValue((int) algorithm.getDelay());
    }

    public long getAlgorithmDelay() {
        return algorithmDelay;
    }

}
