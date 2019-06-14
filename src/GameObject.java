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
	public boolean active;
	protected GamePanel panel;

	protected GameObject(int gWidth, int gHeight, GamePanel panel) {
		gravity = 0.0125;
		friction = 0.99;
		this.panel = panel;
		gameWidth = gWidth;
		gameHeight = gHeight;
		active = true;
	}

	/**
	 * Takes Graphics as a parameter draws character on screen
	 * 
	 * @param g - Graphics
	 */
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect((int) x, (int) y, width, height);
	}

	/**
	 * Takes two double parameters representing acceleration and continuously adds
	 * them to the objects speed
	 * 
	 * @param accelX - double X axis acceleration
	 * @param accelY - double Y axis acceleration
	 */
	public void accelerate(double accelX, double accelY) {
		speedX += accelX;
		speedY += accelY;
	}

	/**
	 * Takes two double parameters representing the objects speed and continously
	 * adds them to the objects X/Y
	 * 
	 * @param sX - double speedX
	 * @param sY - double speedY
	 */
	public void move(double sX, double sY) {
		x += sX;
		y += sY;
	}

	/** Called repeatedly to run any and all methods of an object */
	public abstract void update();

	/**
	 * @return Rectangle - the rectangle of the object
	 */
	public Rectangle getRect() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	/**
	 * @param go - Gameobject
	 * @return boolean indicating whether or not two objects intersect
	 */
	public boolean intersects(GameObject go) {
		return this.getRect().intersects(go.getRect());
	}
	/**
	 * @param r - Rectangle to intersect with object
	 * @return boolean indicating whether GameObject and rectangle parameter intersect
	 */
	public boolean intersects(Rectangle r) {
		return this.getRect().intersects(r);
	}

	/**
	 * @param go - Gameobject
	 * @return boolean indicating if object collides with another from above
	 */
	public boolean topCollision(GameObject go) {
		if (this.intersects(go) && y + height / 16 * 15 <= go.getY()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param go - Gameobject
	 * @return boolean indicating if object collides with another from below
	 */
	public boolean bottomCollision(GameObject go) {
		if (this.intersects(go) && y >= go.getY() + go.getHeight() - 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param go - Gameobject
	 * @return boolean indicating if object collides with another from the right
	 */
	public boolean rightCollision(GameObject go) {
		if (this.intersects(go) && x + width >= go.getX()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param go - Gameobject
	 * @return boolean indicating if object collides with another from the left
	 */
	public boolean leftCollision(GameObject go) {
		if (this.intersects(go) && x <= go.getX() + go.getWidth()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return boolean indicating whether or not object is active
	 */
	public boolean isActive() {
		return active;
	}
	/** Abstract method meant to be used by each GameObject extension to keep it on the screen */
	public abstract void screenBounds();

	/**
	 * @return x - int x-axis position of object
	 */
	public int getX() {
		return (int) x;
	}
	/**
	 * @return y - int y-axis position of object
	 */
	public int getY() {
		return (int) y;
	}

	/**
	 * @return centerX - x position of the center of the object
	 */
	public int getCenterX() {
		return (int) x + width / 2;
	}

	/**
	 * @return centerY - y position of the center of the object
	 */
	public int getCenterY() {
		return (int) y + height / 2;
	}

	/**
	 * @return width - int width of the object
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height - int height of the object
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return color - Color of the object
	 */
	public Color getColor() {
		return color;
	}
}
