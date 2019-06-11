import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class StatusBar extends GameObject {
    private Character character;
    public double initWidth, initHeight;
    public StatusBar(int gWidth, int gHeight, GamePanel panel, Character character) {
        super(gWidth, gHeight, panel);
        this.character = character;
        color = Color.red;
        active = true;
        initWidth = 20;
        initHeight = 5;
        width = (int)initWidth;
        height = (int)initHeight;
    }
    @Override
    public void update() {
    }
    @Override
    public void screenBounds() {
        
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}