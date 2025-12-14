package fantasyrealms.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingGame extends JFrame {
    
    public SwingGame() {
        setTitle("Fantasy Realms RPG - 2D Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        GamePanel panel = new GamePanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingGame());
    }
}
