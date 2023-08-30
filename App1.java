import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class App1 {

    private static GUI1 gui;

    public static void main(String[] args) throws Exception {
        World.init();
        gui = new GUI1();

        JFrame app = new JFrame("Renderer");
        app.setTitle("Renderer");
        app.setSize(GUI1.windowSize, GUI1.windowSize);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.add(gui);
        app.addKeyListener(gui);
        app.setVisible(true);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                update();
            }
        }, 0, 1000 / 60);
    }

    private static void update() {
        gui.performRotation();
        World.update();
        gui.repaint();
    }
}
