package sortvisualizer.screens;

import sortvisualizer.Main;

import javax.swing.*;
import java.awt.*;

import static sortvisualizer.Main.WIN_HEIGHT;
import static sortvisualizer.Main.WIN_WIDTH;

public abstract class Screen extends JPanel {

    protected Main main;

    public Screen(Main main) {
        this.main = main;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIN_WIDTH, WIN_HEIGHT);
    }

    public abstract void onOpen();



}
