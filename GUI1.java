import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI1 extends JPanel implements KeyListener {

    public static int windowSize = 0x400;

    private final int viewSize = 10;

    @Override
    public void paintComponent(Graphics g) {
        // Black background
        g.setColor(Color.black);
        g.fillRect(0, 0, windowSize, windowSize);

        for (int i = 0; i < World.furthermostCuts.size(); i++) {
            int j = World.furthermostCuts.get(i);

            g.setColor(World.cutColors.get(j));
            // g.setColor(Color.green);

            int[] xPoints = new int[World.cuts.get(j).size()];
            int[] yPoints = new int[World.cuts.get(j).size()];

            for (int k = 0; k < xPoints.length; k++) {
                double x = World.cuts.get(j).get(k)[0];
                double y = World.cuts.get(j).get(k)[1];
                double z = World.cuts.get(j).get(k)[2];
                xPoints[k] = (int) (windowSize * ((x * (viewSize / (z + World.zOffset)) + viewSize / 2.0) / viewSize));
                yPoints[k] = (int) (windowSize
                        * (1 - (y * (viewSize / (z + World.zOffset)) + viewSize / 2.0) / viewSize));
            }

            g.fillPolygon(xPoints, yPoints, World.cuts.get(j).size());
        }
    }

    // --- --- ---

    private double speedMultiplier = 0.05;

    public void performRotation() {
        double zw = (posZW ? 1 : (negZW ? -1 : 0)) * speedMultiplier;
        double yw = (posYW ? 1 : (negYW ? -1 : 0)) * speedMultiplier;
        double xw = (posXW ? 1 : (negXW ? -1 : 0)) * speedMultiplier;
        double y = (posY ? 1 : (negY ? -1 : 0)) * speedMultiplier;

        World.rotate(zw, yw, xw, y);
    }

    // --- INPUT METHODS ---

    private boolean posZW = false;
    private boolean negZW = false;

    private boolean posYW = false;
    private boolean negYW = false;

    private boolean posXW = false;
    private boolean negXW = false;

    private boolean posY = false;
    private boolean negY = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 16) {
            World.shift = true;
        }

        switch (e.getKeyChar()) {
            case '1' -> negZW = true;
            case '3' -> posZW = true;

            case '4' -> negYW = true;
            case '6' -> posYW = true;

            case '7' -> negXW = true;
            case '9' -> posXW = true;

            case 'a' -> negY = true;
            case 'd' -> posY = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 16) {
            World.shift = false;
        }

        switch (e.getKeyChar()) {
            case '1' -> negZW = false;
            case '3' -> posZW = false;

            case '4' -> negYW = false;
            case '6' -> posYW = false;

            case '7' -> negXW = false;
            case '9' -> posXW = false;

            case 'a' -> negY = false;
            case 'd' -> posY = false;
        }
    }

}
