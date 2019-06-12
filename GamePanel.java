import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GamePanel extends JPanel implements Runnable {
    private Thread thread;
    private ArrayList<GameObject> pieces;
    private ArrayList<Platform> platforms;
    private Platform floor;
    private Character char1;

    public GamePanel(int width, int height) {
        pieces = new ArrayList<GameObject>();
        platforms = new ArrayList<Platform>();
        thread = new Thread(this);
        floor = new Platform(width, height, width, 20, 0, height - 60);
        char1 = new Character(width, height, floor, platforms);
        thread.start();
        setBackground(Color.black);
        pieces.add(floor);
        pieces.add(char1);
        platforms.add(floor);
        bindKeys();
    }

    private Action left = new AbstractAction("left") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setSpeedX(-1);
            char1.setDirX(Dir.LEFT);
        }
    };
    private Action right = new AbstractAction("right") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setSpeedX(1);
            char1.setDirX(Dir.RIGHT);
        }
    };
    private Action stop = new AbstractAction("stop") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setDirX(Dir.NONE);
        }
    };
    private Action jump = new AbstractAction("jump") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.jump();
        }
    };

    private void bindKeys() {
        // left arrow key press
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left", left);
        // left arrow key release
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stop", stop);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right", right);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stop", stop);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "jump", jump);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "jump", jump);


    }

    private void registerKeyBinding(KeyStroke keyStroke, String name, Action action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(keyStroke, name);
        am.put(name, action);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GameObject piece : pieces) {
            piece.draw(g);
            piece.update();
        }
    }

    @Override
    public void run() {
        // Game loop: sleep for 20 milliseconds, call paintComponent(), repeat.
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
            repaint();
        }
    }
}