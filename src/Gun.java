import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.MouseInfo;

public class Gun extends GameObject implements PickUp {
    private Character character;
    private Dir dir;
    private ArrayList<GameObject> pieces;
    private boolean isHeld;
    private boolean isThrown;
    private int[] mouseLoc;
    private int shotsLeft;
    private final int clipSize, fireRate;
    private int reloadCount, fireCount;
    private double kX, kY, delta;
    private final double speed;

    public Gun(int gWidth, int gHeight, GamePanel panel, Character character, ArrayList<GameObject> pieces) {
        super(gWidth, gHeight, panel);
        this.character = character;
        this.pieces = pieces;
        dir = Dir.NONE;
        width = 20;
        height = 5;
        x = gameWidth / 2;
        y = gameHeight / 8 * 7;
        color = Color.green;
        isHeld = false; // to prevent picking back up immediately
        isThrown = false;
        friction = 0.9995; // less friction than normal other objects
        clipSize = 10;
        fireRate = 50; // firing speed of the gun, higher is slower
        fireCount = 50;
        speed = 2.0;
        shotsLeft = clipSize;
        reloadCount = 0;
        mouseLoc = new int[2];
        delta = 0;
    }

    @Override
    public boolean pickUp(Character grabber) {
        if (!isThrown) {
            character = grabber;
            x = character.getX() - width;
            y = character.getY() + character.getHeight() / 2 + height;
            setIsHeld(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Applies forces to simulate a throwing the gun in the direction of the mouse
     * pointer
     */
    public void drop() {
        if (!isThrown && isHeld) {
            isThrown = true;
            isHeld = false;
            if (mouseLoc[0] < character.getCenterX()) {
                x = character.getCenterX() - width;
            } else {
                x = character.getCenterX();
            }
            speedX = speed * Math.cos(delta);
            speedY = speed * Math.sin(delta);
        }
    }

    public void findDir() {
        mouseLoc = super.panel.getMouseLoc();
        double lX = mouseLoc[0] - character.getCenterX();
        double lY = mouseLoc[1] - character.getCenterY();
        delta = (Math.atan(lY / lX));
        if (mouseLoc[1] < character.getCenterY() && mouseLoc[0] < character.getCenterX()) {
            delta = -(Math.PI - delta);
        } else if (mouseLoc[1] >= character.getCenterY() && mouseLoc[0] < character.getCenterX()) {
            delta = -1 * (-Math.PI - delta);
        }
    }

    public void aimGun() {
        kX = character.getCenterX() + width * Math.cos(delta);
        kY = character.getCenterY() + width * Math.sin(delta);
    }

    public void fire() {
        if (shotsLeft > 0 && reloadCount > 401 && fireCount >= fireRate) {
            super.panel.add(new Bullet(character.getID(), gameWidth, gameHeight, panel, kX, kY, pieces));
            shotsLeft--;
            fireCount = 0;
        }
        if (shotsLeft <= 0) {
            reload();
        }
    }

    public void reload() {
        shotsLeft = clipSize;
        reloadCount = 1; // add 1 to avoid divide by zero problems in reloadBar
        pieces.add(new ReloadBar(gameWidth, gameHeight, panel, character, this));
    }

    public void setIsHeld(boolean b) {
        isHeld = b;
    }

    @Override
    public void update() {
        findDir();
        aimGun();
        move(speedX, speedY);
        accelerate(0.0, gravity);
        screenBounds();
        reloadCount++;
        fireCount++;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(height));
        g.setColor(color);
        if (isHeld) {
            g.drawLine(character.getCenterX(), character.getCenterY(), (int) kX, (int) kY);
        } else {
            g.fillRect((int) x, (int) y, width, height);
        }
    }

    @Override
    public void move(double sX, double sY) {
        x += sX;
        y += sY;
        if (!this.intersects(character)) {
            isThrown = false;
        }
        if (isHeld) {
            speedX = 0;
            speedY = 0;
            x = character.getCenterX();
            y = character.getCenterY() - height / 2;
        } else {
            for (GameObject obj : pieces) {
                if (obj instanceof Platform) {
                    if (topCollision((Platform) obj)) {
                        y = ((Platform) obj).getY() - height;
                        speedX *= friction * 0.7; // more friction because ground has more friction than air
                        if (speedY >= 0) {
                            speedY = 0;
                        }
                    } else {
                        speedX *= friction;
                    }
                }
            }
        }
    }

    @Override
    public void screenBounds() {
        if (x + width > gameWidth) {
            x = gameWidth - width;
        } else if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
            speedY = 0;
        }
    }

    public int getReloadCount() {
        return reloadCount;
    }

    @Override
    public boolean getIsHeld() {
        return isHeld;
    }
}