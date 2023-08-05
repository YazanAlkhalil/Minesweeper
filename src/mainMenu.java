import javax.swing.*;
import java.awt.*;


public class mainMenu extends JPanel{

    private Image img;

    public mainMenu(String img) {
      this(new ImageIcon(img).getImage());
    }
    public mainMenu(Image img) {
        this.img = img;
        Dimension size = new Dimension(100, 100);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
      }
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
}
}
