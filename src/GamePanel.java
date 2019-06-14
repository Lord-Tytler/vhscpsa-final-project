import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class GamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    private Thread thread;
    private ArrayList<GameObject> pieces;
    private ArrayList<Platform> platforms;
    private ArrayList<PickUp> pickUps;
    private Platform floor, bottomLeft, bottomCenter, bottomRight, topLeft, topCenter, topRight;
    private Character char1, char2;
    private Gun gun;
    private final int gWidth, gHeight;
    private int gameState; // int to determine which part of the should be on screen, 0 being menu, 1 being
                           // playing
    private int sidePlatWidth, sidePlatHeight, centerPlatWidth;
    private int[] mouseLoc;

    public GamePanel(int width, int height) {
        pieces = new ArrayList<GameObject>();
        platforms = new ArrayList<Platform>();
        pickUps = new ArrayList<PickUp>();
        thread = new Thread(this);
        gWidth = width;
        gHeight = height;
        gameState = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
        mouseLoc = new int[2];
        sidePlatWidth = width / 16 * 3;
        centerPlatWidth = width / 2;
        sidePlatHeight = 20;
        floor = new Platform(width, height, this, width, sidePlatHeight, 0, height - 60);
        // gameWidth, gameHeight, rectWidth, rectHeight, x, y
        bottomLeft = new Platform(width, height, this, sidePlatWidth, sidePlatHeight, 0, (height / 16 * 12));
        bottomCenter = new Platform(width, height, this, centerPlatWidth, sidePlatHeight,
                (width / 2) - (centerPlatWidth / 2), height / 16 * 9);
        bottomRight = new Platform(width, height, this, sidePlatWidth, sidePlatHeight, width - sidePlatWidth,
                height / 16 * 12);
        topLeft = new Platform(width, height, this, sidePlatWidth, sidePlatHeight, 0, height / 16 * 6);
        topCenter = new Platform(width, height, this, centerPlatWidth, sidePlatHeight,
                (width / 2) - (centerPlatWidth / 2), height / 16 * 3);
        topRight = new Platform(width, height, this, sidePlatWidth, sidePlatHeight, width - sidePlatWidth,
                height / 16 * 6);

        char1 = new Character(1, width, height, this, (floor.getX()), (floor.getY() - 225), Color.blue, floor, pieces,
                pickUps);
        char2 = new Character(2, width, height, this, (width - (floor.getX() / 8) - char1.getWidth()),
                (floor.getY() - 225), Color.yellow, floor, pieces, pickUps);

        gun = new Gun(width, height, this, char1, pieces);

        thread.start();
        setBackground(Color.black);
        // arrayList of gameObjects
        pieces.add(floor);
        pieces.add(bottomLeft);
        pieces.add(bottomRight);
        pieces.add(bottomCenter);
        pieces.add(topLeft);
        pieces.add(topRight);
        pieces.add(topCenter);
        pieces.add(char1);
        pieces.add(char2);
        pieces.add(gun);

        // arrayList of platforms, for collision
        platforms.add(floor);
        platforms.add(bottomLeft);
        platforms.add(bottomRight);
        platforms.add(bottomCenter);
        platforms.add(topLeft);
        platforms.add(topRight);
        platforms.add(topCenter);

        pickUps.add(gun);

        bindKeys();
    }

    private Action leftOne = new AbstractAction("leftOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setSpeedX(-1);
            char1.setDirX(Dir.LEFT);
        }
    };
    private Action rightOne = new AbstractAction("rightOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setSpeedX(1);
            char1.setDirX(Dir.RIGHT);
        }
    };
    private Action stopOne = new AbstractAction("stopOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setDirX(Dir.NONE);
        }
    };
    private Action jumpOne = new AbstractAction("jumpOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.jump();
        }
    };
    private Action startFireOne = new AbstractAction("startFireOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setIsFiring(true);
        }
    };
    private Action stopFireOne = new AbstractAction("stopFireOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.setIsFiring(false);
        }
    };
    private Action dropOne = new AbstractAction("dropOne") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char1.gunDrop();
        }
    };
    // Character2
    private Action leftTwo = new AbstractAction("leftTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.setSpeedX(-1);
            char2.setDirX(Dir.LEFT);
        }
    };
    private Action rightTwo = new AbstractAction("rightTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.setSpeedX(1);
            char2.setDirX(Dir.RIGHT);
        }
    };
    private Action stopTwo = new AbstractAction("stopTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.setDirX(Dir.NONE);
        }
    };
    private Action jumpTwo = new AbstractAction("jumpTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.jump();
        }
    };
    private Action startFireTwo = new AbstractAction("startFireTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.setIsFiring(true);
            // char2.fire();
        }
    };
    private Action stopFireTwo = new AbstractAction("stopFireTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.setIsFiring(false);
        }
    };
    private Action dropTwo = new AbstractAction("dropTwo") {
        @Override
        public void actionPerformed(ActionEvent ae) {
            char2.gunDrop();
        }
    };

    /**
     * binds all keys for gameplay
     */
    private void bindKeys() {
        // Character 1
        // movement
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "leftOne", leftOne);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stopOne", stopOne);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "rightOne", rightOne);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stopOne", stopOne);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "jumpOne", jumpOne);
        // gun
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "startFireOne", startFireOne);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "stopFireOne", stopFireOne);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "dropOne", dropOne);
        // Character 2
        // movement
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false), "leftTwo", leftTwo);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "stopTwo", stopTwo);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, false), "rightTwo", rightTwo);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, true), "stopTwo", stopTwo);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, true), "jumpTwo", jumpTwo);
        // gun
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0, false), "startFireTwo", startFireTwo);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0, true), "stopFireTwo", stopFireTwo);
        registerKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true), "dropTwo", dropTwo);

    }

    private void registerKeyBinding(KeyStroke keyStroke, String name, Action action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(keyStroke, name);
        am.put(name, action);
    }

    public void mouseClicked(MouseEvent e) {
        setMouseLoc(e);
        Rectangle mouse = new Rectangle(getMouseLoc()[0], getMouseLoc()[1], 1, 1);
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) instanceof Clickable) {
                if (pieces.get(i).intersects(mouse)) {
                    ((Clickable) pieces.get(i)).isClicked();
                }
            }
        }

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        setMouseLoc(e);
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void setMouseLoc(MouseEvent e) {
        mouseLoc[0] = e.getX();
        mouseLoc[1] = e.getY();
    }

    public int[] getMouseLoc() {
        return mouseLoc;
    }

    /**
     * Takes GameObject parameter and adds it to GameObject ArrayList "pieces"
     * 
     * @param go - GameObject to be added to pieces
     */
    public void add(GameObject go) {
        pieces.add(go);
    }

    /**
     * @return the integer representing which part of the game one is in
     */
    public int getGameState() {
        return gameState;
    }

    public void nextGameState() {
        gameState++;
        if (gameState == 1) {
            for (int i = 0; i < pieces.size(); i++) {
                if (pieces.get(i) instanceof Character) {
                    ((Character) pieces.get(i)).spawn();
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == 0) {
            runMenus(g);
        } else if (gameState == 1) {
            runMain(g);
        }
    }

    public void runMenus(Graphics g) {
        boolean ready = true;
        for (int i = 0; i < pieces.size(); i++) {
            g.setColor(Color.white);
            g.drawString("CLICK TO CHANGE COLOR",
                    gWidth / 2 - g.getFontMetrics().stringWidth("CLICK TO CHANGE COLOR") / 2,
                    400 + g.getFontMetrics().getHeight() / 2);

            if (pieces.get(i).isActive()) {
                if (pieces.get(i) instanceof MenuItem) {
                    pieces.get(i).draw(g);
                    pieces.get(i).update();
                    if (pieces.get(i) instanceof ReadyButton && !((ReadyButton) pieces.get(i)).isReady()) {
                        ready = false;
                    }
                }
            } else {
                pieces.remove(i);
                i--;
            }
        }
        if (ready && gameState == 0) {
            nextGameState();
        }
    }

    public void runMain(Graphics g) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).isActive()) {
                pieces.get(i).draw(g);
                pieces.get(i).update();
            } else {
                pieces.remove(i);
                i--;
            }
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