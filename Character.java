import java.awt.Color;
import java.util.ArrayList;

public class Character extends GameObject {
    private Dir dirX;
    private Dir dirY;
    private Platform floor;
    private ArrayList<Platform> platforms;
    int jumpCount;

    public Character(int gWidth, int gHeight, Platform floor, ArrayList<Platform> platforms) {
        super(gWidth, gHeight);
        this.floor = floor;
        this.platforms = platforms;
        width = 20;
        height = 50;
        color = Color.blue;
        x = floor.getX() + (floor.getX() / 8);
        y = floor.getY() - height;
        dirX = Dir.NONE;
        dirY = Dir.NONE;
        speedX = 0;
        speedY = 0;
        jumpCount = 0;

    }

    @Override
    public void update() {
        move(speedX, speedY);
        accelerate(0.0, gravity);
        screenBounds();

        if (dirX == Dir.NONE) {
            if (topCollision(floor)) {
                speedX *= friction * 0.7; // more friction because ground has more friction than air
            } else {
                speedX *= friction;
            }
        }
    }

    @Override
    public void move(double sX, double sY) {
        x += sX;
        for (int i = 0; i < platforms.size(); i++) {
            if (topCollision(platforms.get(i))) {
                y = platforms.get(i).getY() - height;
                speedY = 0;
                jumpCount = 0;
            } else if (bottomCollision(platforms.get(i))) { // bottom collision
                y = platforms.get(i).getY() + platforms.get(i).getHeight();
                speedY = 0;
            } else {
                y += sY;
            }
        }
    }

    public void screenBounds() {
        if (x + width > gameWidth) {
            x = gameWidth - width;
        } else if (x < 0) {
            x = 0;
        }
        if (y + height > gameHeight) {
            y = gameHeight - height;
        } else if (y < 0) {
            y = 0;
        }
    }

    public void jump() {
        if (jumpCount < 2) {
            speedY = -1.75;
            jumpCount++;
        }
    }

    public void setSpeedX(double velX) {
        this.speedX = velX;
    }

    public void setSpeedY(double velY) {
        this.speedY = velY;
    }

    public void setDirX(Dir dir) {
        this.dirX = dir;
    }

    public void setDirY(Dir dir) {
        this.dirY = dir;
    }
}