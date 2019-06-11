import java.awt.Color;

public class ReloadBar extends StatusBar {
    private Gun gun;
    private Character character;

    public ReloadBar(int gWidth, int gHeight, GamePanel panel, Character character, Gun gun) {
        super(gWidth, gHeight, panel, character);
        this.gun = gun;
        this.character = character;
        color = Color.white;
    }

    @Override
    public void update() {
        x = character.getCenterX() - width / 2;
        y = character.getY() + character.getHeight() + height + 5;
        if (gun.getReloadCount() < 401) {
            width = (int) Math.round(initWidth * ((401 - (double) gun.getReloadCount()) / 401)) ;
        } else {
            active = false;
        }
    }

    @Override
    public void screenBounds() {

    }

    public void setActive(boolean active) {
        this.active = active;
    }
}