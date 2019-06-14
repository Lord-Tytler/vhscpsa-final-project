import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class HealthBar extends StatusBar {
    private Character character;
    public HealthBar(int gWidth, int gHeight, GamePanel panel, Character character) {
        super(gWidth, gHeight, panel, character);
        this.character = character;
        color = Color.red;
        x = character.getCenterX() - (width / 2);
        y = character.getY() - height - 5;
    }
    @Override
    public void update() {
        x = character.getCenterX() - width / 2;
        y = character.getY() - height - 5;
        width = (int)Math.round(initWidth * ((double)character.getHealth() / 100));
    }
    @Override
    public void screenBounds() {
        
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}