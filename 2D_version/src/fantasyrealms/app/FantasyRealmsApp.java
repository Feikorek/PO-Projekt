package fantasyrealms.app;

import javax.swing.*;
import fantasyrealms.game.item.Item;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class FantasyRealmsApp extends JFrame {
    private final GameEngine engine = new GameEngine();

    public FantasyRealmsApp() {
        setTitle("Fantasy Realms RPG - Graphical Edition");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        GameView gameView = new GameView();
        add(gameView);
        pack();
        
        setLocationRelativeTo(null);
        setVisible(true);
        gameView.requestFocusInWindow();
    }

    private class GameView extends JPanel {
        // Przyciski ekwipunku
        private JButton unequipWeaponBtn, unequipArmorBtn, unequipRingBtn, unequipNecklaceBtn;
        // Przyciski walki
        private JButton attackBtn, skillBtn, fleeBtn;
        
        private Image playerImg, monsterImg, bossImg, grassImg, wallImg, townImg;

        public GameView() {
            setPreferredSize(new Dimension(
                GameState.MAP_SIZE * GameState.TILE_SIZE + 250, 
                GameState.MAP_SIZE * GameState.TILE_SIZE
            ));
            setLayout(null);

            // Wczytywanie obrazów
            playerImg = loadImage("player.png");
            monsterImg = loadImage("monster.png");
            bossImg = loadImage("boss.png");
            grassImg = loadImage("grass.png");
            wallImg = loadImage("wall.png");
            townImg = loadImage("town.png");

            int sidebarX = GameState.MAP_SIZE * GameState.TILE_SIZE + 160;

            // Inicjalizacja przycisków ekwipunku
            unequipWeaponBtn = createBtn("Zdejmij", sidebarX, 340, () -> engine.getState().player.unequipWeapon(), true);
            unequipArmorBtn = createBtn("Zdejmij", sidebarX, 370, () -> engine.getState().player.unequipArmor(), true);
            unequipRingBtn = createBtn("Zdejmij", sidebarX, 400, () -> engine.getState().player.unequipRing(), true);
            unequipNecklaceBtn = createBtn("Zdejmij", sidebarX, 430, () -> engine.getState().player.unequipNecklace(), true);

            // Inicjalizacja przycisków walki (na obszarze mapy)
            attackBtn = createBtn("1. ATAK", 50, 300, () -> engine.handleInput(KeyEvent.VK_1), false);
            skillBtn = createBtn("2. SKILL", 160, 300, () -> engine.handleInput(KeyEvent.VK_2), false);
            fleeBtn = createBtn("3. UCIECZKA", 270, 300, () -> engine.handleInput(KeyEvent.VK_3), false);

            // Dodanie obsługi klawiatury
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    engine.handleInput(e.getKeyCode());
                    repaint();
                }
            });
            setFocusable(true);
        }

        private JButton createBtn(String txt, int x, int y, Runnable act, boolean isSmall) {
            JButton b = new JButton(txt);
            if (isSmall) {
                b.setBounds(x, y, 75, 20);
                b.setFont(new Font("Arial", Font.PLAIN, 10));
            } else {
                b.setBounds(x, y, 100, 35);
                b.setFont(new Font("Arial", Font.BOLD, 12));
                b.setBackground(Color.DARK_GRAY);
                b.setForeground(Color.WHITE);
            }
            b.setFocusable(false);
            b.addActionListener(e -> {
                act.run();
                repaint();
                requestFocusInWindow();
            });
            add(b);
            return b;
        }

        private Image loadImage(String fileName) {
            URL imgUrl = getClass().getResource("/fantasyrealms/icons/" + fileName);
            return (imgUrl != null) ? new ImageIcon(imgUrl).getImage() : null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            GameState state = engine.getState();
            
            // Zarządzanie widocznością przycisków
            boolean exploring = (state.currentState == GameState.State.EXPLORING);
            boolean combat = (state.currentState == GameState.State.COMBAT);

            // Przyciski walki widoczne tylko w walce
            attackBtn.setVisible(combat);
            skillBtn.setVisible(combat);
            fleeBtn.setVisible(combat);

            if (exploring) {
                drawExploration(g, state);
            } else if (combat) {
                drawCombat(g, state);
            } else if (state.currentState == GameState.State.GAME_OVER) {
                drawGameOver(g);
            }
            
            drawSidebar(g, state, exploring);
        }

        private void drawExploration(Graphics g, GameState state) {
            int ts = GameState.TILE_SIZE;
            for (int x = 0; x < GameState.MAP_SIZE; x++) {
                for (int y = 0; y < GameState.MAP_SIZE; y++) {
                    if (grassImg != null) g.drawImage(grassImg, x * ts, y * ts, ts, ts, null);
                    else { g.setColor(new Color(34, 139, 34)); g.fillRect(x * ts, y * ts, ts, ts); }
                }
            }
            for (Point p : state.walls) renderSprite(g, wallImg, Color.GRAY, p.x, p.y);
            renderSprite(g, townImg, Color.PINK, state.townLocation.x, state.townLocation.y);
            renderSprite(g, bossImg, Color.RED, state.bossLocation.x, state.bossLocation.y);
            for (GameState.MonsterEntity me : state.enemiesOnMap) {
                renderSprite(g, monsterImg, Color.GREEN, me.location.x, me.location.y);
            }
            renderSprite(g, playerImg, Color.CYAN, state.playerX, state.playerY);
        }

        private void renderSprite(Graphics g, Image img, Color fallback, int x, int y) {
            int ts = GameState.TILE_SIZE;
            if (img != null) g.drawImage(img, x * ts, y * ts, ts, ts, null);
            else { g.setColor(fallback); g.fillRect(x * ts + 5, y * ts + 5, ts - 10, ts - 10); }
        }

        private void drawCombat(Graphics g, GameState state) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GameState.MAP_SIZE * GameState.TILE_SIZE, GameState.MAP_SIZE * GameState.TILE_SIZE);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("WALKA!", 50, 50);
            
            if (state.currentEnemy != null) {
                if (monsterImg != null) g.drawImage(monsterImg, 250, 50, 100, 100, null);
                g.drawString("Przeciwnik: " + state.currentEnemy.getName(), 50, 90);
                
                // Pasek HP Przeciwnika
                g.setColor(Color.GRAY);
                g.fillRect(50, 110, 200, 15);
                g.setColor(Color.RED);
                double hpPercent = (double)state.currentEnemy.getHp() / 100.0; // Przykładowe 100 HP
                g.fillRect(50, 110, (int)(200 * Math.max(0, Math.min(1, hpPercent))), 15);
            }
        }

        private void drawGameOver(Graphics g) {
            g.setColor(Color.BLACK); g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("KONIEC GRY", 100, 200);
        }

        private void drawSidebar(Graphics g, GameState state, boolean canUnequip) {
            int x = GameState.MAP_SIZE * GameState.TILE_SIZE + 20;
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x - 20, 0, 300, getHeight());

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("STATYSTYKI", x, 40);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 13));
            g.drawString("HP: " + state.player.getHp() + "/" + state.player.getMaxHp(), x, 75);
            g.drawString("Złoto: " + state.player.getGold(), x, 100);
            g.drawString("Atak: " + state.player.getTotalAttack(), x, 125);

            // Sekcja Ekwipunku
            g.setColor(Color.CYAN);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("ZAŁOŻONE:", x, 310);
            
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            g.setColor(Color.WHITE);

            // Wyświetlanie nazw i widoczność przycisków
            Item w = state.player.getEquippedWeapon();
            g.drawString("Broń: " + (w != null ? w.getName() : "Brak"), x, 355);
            unequipWeaponBtn.setVisible(canUnequip && w != null);

            Item a = state.player.getEquippedArmor();
            g.drawString("Zbroja: " + (a != null ? a.getName() : "Brak"), x, 385);
            unequipArmorBtn.setVisible(canUnequip && a != null);

            Item r = state.player.getEquippedRing();
            g.drawString("Ring: " + (r != null ? r.getName() : "Brak"), x, 415);
            unequipRingBtn.setVisible(canUnequip && r != null);

            Item n = state.player.getEquippedNecklace();
            g.drawString("Amulet: " + (n != null ? n.getName() : "Brak"), x, 445);
            unequipNecklaceBtn.setVisible(canUnequip && n != null);

            g.setColor(Color.LIGHT_GRAY);
            g.drawString("E: Plecak", x, 520);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FantasyRealmsApp::new);
    }
}