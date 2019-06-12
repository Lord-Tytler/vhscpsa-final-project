import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
	// object x,y position
	protected double x, y;
	// speed of object
	protected double speedX, speedY;
	protected double gravity, friction;
	// width and height of object
	protected int width, height;
	// JPanel width and height
	protected int gameWidth, gameHeight;
	// color of object
	protected Color color;

	protected GameObject(int gWidth, int gHeight) {
		gravity = 0.0125;
		friction = 0.99;
		gameWidth = gWidth;
		gameHeight = gHeight;
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect((int) x, (int) y, width, height);
	}

	public void accelerate(double accelX, double accelY) {
		speedX += accelX;
		speedY += accelY;
	}

	public void move(double sX, double sY) {
		x += sX;
		y += sY;
	}

	public abstract void update();

	public Rectangle getRect() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public boolean intersects(GameObject go) {
		return this.getRect().intersects(go.getRect());
	}

	public boolean topCollision(GameObject go) {
		if (this.intersects(go) && y <= go.getY()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean bottomCollision(GameObject go) {
		if (this.intersects(go) && y >= go.getY() + go.getHeight() - 1) {
			return true;
		} else {
			return false;
		}
	}

	public abstract void screenBounds();

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Color getColor() {
		return color;
	}
}
