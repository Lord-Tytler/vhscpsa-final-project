import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;
import java.awt.Component;

public class Character extends GameObject implements Clickable, MenuItem {
    private Dir dirX;
    private ArrayList<GameObject> pieces;
    int jumpCount;
    public Gun gun;
    private boolean hasGun, isFiring;
    private ArrayList<PickUp> pickUps;
    private int health, lives, colorIndex;
    private final int id, maxHealth;
    private final double spawnX, spawnY;
    private ArrayList<Color> colors;
    private HealthBar healthBar;
    private MouseEvent mouse;
    private ReadyButton ready;

    // iX == initX
    public Character(int id, int gWidth, int gHeight, GamePanel panel, double iX, double iY, Color color,
            Platform floor, ArrayList<GameObject> pieces, ArrayList<PickUp> pickUps) { // ask morton why naming the
                                                                                       // double "x" and
        super(gWidth, gHeight, panel);
        healthBar = new HealthBar(gWidth, gHeight, panel, this);
        pieces.add((GameObject) healthBar);
        this.pieces = pieces;
        this.pickUps = pickUps;
        this.id = id;
        maxHealth = 100;
        width = 20;
        height = 50;
        health = maxHealth;
        lives = 3;
        spawnX = iX;
        spawnY = iY;
        this.x = iX;
        this.y = iY;
        dirX = Dir.NONE;
        speedX = 0;
        speedY = 0;
        jumpCount = 0;
        gun = null;
        isFiring = false;

        colors = new ArrayList<Color>();
        colorIndex = id - 1;
        fillColors();
        this.color = colors.get(colorIndex);
        ready = new ReadyButton(gWidth, gHeight, panel, this);
        pieces.add(ready);

    }

    private void fillColors() {
        colors.add(Color.orange);
        colors.add(Color.blue);
        colors.add(Color.yellow);
        colors.add(Color.green);
        colors.add(Color.pink);
        colors.add(Color.cyan);
        colors.add(Color.white);

    }

    @Override
    public void update() {
        if (panel.getGameState() == 0) {
            y = (gameHeight / 2) + (height / 2);
            if (id == 1) {
                x = (gameWidth / 8) * 3 - width / 2;
            } else {
                x = (gameWidth / 8) * 5 - width / 2;
            }
        } else {
            pieces.remove(ready);
            if (health <= 0) {
                loseLife();
            }
            move(speedX, speedY);
            accelerate(0.0, gravity);
            screenBounds();
            if (isFiring) {
                fire();
            }
        }
    }

    @Override
    public void move(double sX, double sY) {
        x += sX;
        y += sY;
        for (GameObject obj : pieces) {
            if (obj instanceof Platform) {
                if (topCollision(obj)) {
                    y = ((Platform) obj).getY() - height;
                    if (speedY >= 0) {
                        speedY = 0;
                    }
                    if (speedY == 0) {
                        jumpCount = 0;
                    }
                }
                if (dirX == Dir.NONE) {
                    if (topCollision(obj)) {
                        speedX *= friction * 0.7; // more friction because ground has more friction than air
                    } else {
                        speedX *= friction;
                    }
                }
            }
        }
        for (PickUp pickUp : pickUps) {
            if (this.intersects((GameObject) pickUp) && !pickUp.getIsHeld()) {
                gunPickup((Gun) pickUp);
            }
        }
    }

    @Override
    public void isClicked() {
        if (panel.getGameState() == 0) {
            if (colorIndex < colors.size() - 1) {
                colorIndex++;
                color = colors.get(colorIndex);
            } else {
                colorIndex = 0;
                color = colors.get(colorIndex);
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

    public void spawn() {
        x = spawnX;
        y = spawnY;
    }

    /**
     * sets the characters Y velocity to make the character jump, adds to jump count
     */
    public void jump() {
        if (jumpCount < 2) {
            speedY = -1.90;
            jumpCount++;
        }
    }

    /** takes a Gun as a parameter and "picks up" gun */
    public void gunPickup(Gun gun) {
        if (gun.pickUp(this)) {
            this.gun = gun;
            hasGun = true;
        }
    }

    /** drops/throws gun if a gun is in posession */
    public void gunDrop() {
        if (hasGun) {
            gun.drop();
            hasGun = false;
            gun.drop();
            this.gun = null;
        }
    }

    /** fires bullet from gun, if a gun is in posession */
    public void fire() {
        if (hasGun && gun != null) {
            gun.fire();
        }
    }

    /**
     * takes a boolean parameter and sets isFiring to that parameter
     * 
     * @param b - boolean representing wheter the character's gun should be firing
     *          or not
     */
    public void setIsFiring(boolean b) {
        isFiring = b;
    }

    /**
     * takes an int parameter and removes that value from health
     * 
     * @param damage - int representing the amount of health to be removed
     */
    public void takeDamage(int damage) {
        health -= damage;
    }

    /**
     * Removes one life from character, resets health or sets active to false
     * depending on the remaining amount of lives
     */
    public void loseLife() {
        lives--;
        if (lives <= 0) {
            active = false;
        } else {
            health = maxHealth;
        }
    }

    /** Returns the int ID of the character */
    public int getID() {
        return id;
    }

    /** Returns the int Health of the character */
    public int getHealth() {
        return health;
    }

    /**
     * takes a double representing the X-axis speed and sets the character's X-axis
     * speed
     * 
     * @param velX - double representing the X-axis speed
     */
    public void setSpeedX(double velX) {
        this.speedX = velX;
    }

    /**
     * takes a double representing the Y-axis speed and sets the character's Y-axis
     * speed
     * 
     * @param velY - double representing the Y-axis speed
     */
    public void setSpeedY(double velY) {
        this.speedY = velY;
    }

    /**
     * takes a enum Dir parameter and sets the char's X direction to that parameter
     * 
     * @param dir - enum Dir (left, right, etc)
     */
    public void setDirX(Dir dir) {
        this.dirX = dir;
    }
}