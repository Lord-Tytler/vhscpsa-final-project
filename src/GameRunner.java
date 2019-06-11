import javax.swing.JFrame;

public class GameRunner  {
    int width, height;
    public static void main(String[] args) {
        int width = 1366;
        int height = 768;
        GamePanel gamePanel = new GamePanel(width, height);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setSize(width, height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  //sets game to launch in full screen, because jframe is strange and cuts off
        frame.setVisible(true);
    }
}