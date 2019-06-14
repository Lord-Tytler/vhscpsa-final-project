import java.awt.Color;

public class Platform extends GameObject {
    int speed;
    public Platform(int gWidth, int gHeight, GamePanel panel, int width, int height, int x, int y) {
        super(gWidth, gHeight, panel);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        color = Color.red;

    }
    @Override
    public void update() {
    }
    @Override
    public void screenBounds() {
    }
}