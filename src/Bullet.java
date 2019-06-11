import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.MouseInfo;

public class Bullet extends GameObject {
    private ArrayList<GameObject> pieces;
    private double angle;
    private int bounceCount;
    private Point p;
    private int charID; // id of the character who fires the bullet, to prevent suicide

    public Bullet(int id, int gWidth, int gHeight, GamePanel panel, double x, double y, ArrayList<GameObject> pieces) {
        super(gWidth, gHeight, panel);
        this.pieces = pieces;
        this.charID = id;
        bounceCount = 0;
        this.x = x;
        this.y = y;
        p = MouseInfo.getPointerInfo().getLocation();
        angle = (Math.atan(((p.getY() - 30) - y) / (p.getX() - x))); // -30 to account for windows toolbar at top
        width = 5;
        height = 5;
        color = Color.yellow;
        fire();
    }

    @Override
    public void update() {
        if (bounceCount >= 3) {
            active = false;
        }
        screenBounds();
        move(speedX, speedY);
    }

    @Override
    public void move(double sX, double sY) {
        x += sX;
        y += sY;
        for (GameObject obj : pieces) {
            if (obj.isActive()) {
                if (obj instanceof Platform) {
                    if (topCollision(obj) || bottomCollision(obj)) {
                        speedY *= -1;
                        bounceCount++;
                    } else if (leftCollision(obj) || rightCollision(obj)) {
                        speedX *= -1;
                        bounceCount++;
                    }
                } else if (obj instanceof Character) {
                    if (!(((Character) obj).getID() == charID)) {
                        if (this.intersects(obj) && active == true) {
                            active = false;
                            ((Character) obj).takeDamage(10);
                        }
                    }
                }
            }
        }
    }

    public void fire() {
        if (p.x < x) {
            speedX = -1.25 * Math.cos(angle);
            speedY = -1.25 * Math.sin(angle);
        } else {
            speedX = 1.25 * Math.cos(angle);
            speedY = 1.25 * Math.sin(angle);
        }
    }

    @Override
    public void screenBounds() {
        if (y < 0) {
            speedY *= -1;
            bounceCount++;
        } else if (y > gameHeight) {
            speedY *= -1;
            bounceCount++;
        }
        if (x < 0) {
            speedX *= -1;
            bounceCount++;
        } else if (x > gameWidth) {
            speedX *= -1;
            bounceCount++;
        }
    }
}