package sortvisualizer;

import sortvisualizer.screens.MainMenuScreen;
import sortvisualizer.screens.Screen;

import javax.swing.*;
import java.util.ArrayList;

public class Main {

    private final JFrame window;

    public static final int WIN_WIDTH = 1200;
    public static final int WIN_HEIGHT = 720;

    private final ArrayList<Screen> screens;

    public Main() {
        this.screens = new ArrayList<>();
        this.window = new JFrame("Sort visualizer");
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setVisible(true);
    }

    public Screen getCurrentScreen() {
        return screens.get(screens.size() - 1);
    }

    public void pushScreen(Screen screen) {
        if (!this.screens.isEmpty()) {
            this.window.remove(getCurrentScreen());
        }

        this.screens.add(screen);
        this.window.setContentPane(screen);
        this.window.validate();
        screen.onOpen();
    }

    public void popScreen() {
        if (!this.screens.isEmpty()) {
            Screen prev = getCurrentScreen();
            this.screens.remove(prev);
            this.window.remove(prev);
            if (!this.screens.isEmpty()) {
                Screen current  = getCurrentScreen();
                this.window.setContentPane(current);
                this.window.validate();
                current.onOpen();
            }
            else {
                this.window.dispose();
            }
        }
    }

    public void start() {
        pushScreen(new MainMenuScreen(this));
        this.window.pack();
    }


    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(() -> {
            new Main().start();
        });
    }
}
