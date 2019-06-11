import java.awt.Color;
import java.awt.Graphics;

public class ReadyButton extends GameObject implements Clickable, MenuItem {
    private Character character;
    private boolean ready;

    public ReadyButton(int gWidth, int gHeight, GamePanel panel, Character character) {
        super(gWidth, gHeight, panel);
        this.character = character;
        width = 75;
        height = 25;
        color = Color.red;
        ready = false;
    }

    @Override
    public void update() {
        if (panel.getGameState() == 0) {
            x = character.getCenterX() - width / 2;
            y = character.getY() + character.getHeight() + (height * 2);
        } else {
            active = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        int stringWidth = g.getFontMetrics().stringWidth("READY");
        int stringHeight = g.getFontMetrics().getHeight();
        if (ready) {
            color = Color.green;
            g.setColor(color);
            g.fillRect((int) x, (int) y, width, height);
            g.setColor(Color.white);
            g.drawString("READY", (int)x + (width / 2) - (stringWidth / 2), (int)y + (height / 2) + (stringHeight / 4));
        } else {
            color = Color.red;
            g.setColor(color);
            g.drawRect((int) x, (int) y, width, height);
            g.drawString("READY", (int)x + (width / 2) - (stringWidth / 2), (int)y + (height / 2) + (stringHeight / 4));
        }
    }

    @Override
    public void isClicked() {
        ready = !(ready);
    }
    public boolean isReady() {
        return ready;
    }

    @Override
    public void screenBounds() {
    }
}