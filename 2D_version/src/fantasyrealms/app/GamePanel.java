package fantasyrealms.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    
    private final GameEngine gameEngine;
    private final GameRenderer gameRenderer;

    public GamePanel() {
        // Inicjalizacja silnika
        this.gameEngine = new GameEngine();
        this.gameRenderer = new GameRenderer(gameEngine.getState());

        this.setPreferredSize(new Dimension(GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameRenderer.render(g, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameEngine.handleInput(e.getKeyCode());
        repaint();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}