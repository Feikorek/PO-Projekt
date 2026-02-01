package fantasyrealms.app;

import javax.swing.*;
import fantasyrealms.game.enemy.Boss;
import fantasyrealms.game.enemy.Monster;
import fantasyrealms.game.character.Warrior;
import fantasyrealms.game.item.Item;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        private JButton unequipWeaponBtn, unequipArmorBtn, unequipRingBtn, unequipNecklaceBtn;
        private JButton attackBtn, skillBtn, fleeBtn;
        
        private Map<String, Image> textureCache = new HashMap<>();
        private Image grassImg, wallImg, townImg, exitImg;

        public GameView() {
    // Szerokość mapy + panel boczny (250px)
    setPreferredSize(new Dimension(
        GameState.MAP_SIZE * GameState.TILE_SIZE + 250, 
        GameState.MAP_SIZE * GameState.TILE_SIZE
    ));
    setLayout(null);

    // Ładowanie tekstur
    grassImg = loadImage("grass.png");
    wallImg = loadImage("wall.png");
    townImg = loadImage("town.png");
    
    // NOWA TEKSTURA: Wczytujemy dedykowany plik dla schodów
    exitImg = loadImage("schody.png"); 

    int sidebarX = GameState.MAP_SIZE * GameState.TILE_SIZE + 160;

    // Przyciski ekwipunku
    unequipWeaponBtn = createBtn("Zdejmij", sidebarX, 340, () -> engine.getState().player.unequipWeapon(), true);
    unequipArmorBtn = createBtn("Zdejmij", sidebarX, 370, () -> engine.getState().player.unequipArmor(), true);
    unequipRingBtn = createBtn("Zdejmij", sidebarX, 400, () -> engine.getState().player.unequipRing(), true);
    unequipNecklaceBtn = createBtn("Zdejmij", sidebarX, 430, () -> engine.getState().player.unequipNecklace(), true);

    // Przyciski walki
    attackBtn = createBtn("1. ATAK", 50, 300, () -> engine.handleInput(KeyEvent.VK_1), false);
    skillBtn = createBtn("2. SKILL", 160, 300, () -> engine.handleInput(KeyEvent.VK_2), false);
    fleeBtn = createBtn("3. UCIECZKA", 270, 300, () -> engine.handleInput(KeyEvent.VK_3), false);

    addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            engine.handleInput(e.getKeyCode());
            repaint();
        }
    });
    setFocusable(true);
}

        private Image getEntityImage(Object entity) {
            // Boss zawsze ma jedną, konkretną ikonę
            if (entity instanceof Boss) {
                return getCachedImage("krol_goblinow.png");
            }

            String fileName;
            if (entity instanceof Warrior) fileName = "warrior.png";
            else if (entity instanceof fantasyrealms.game.character.Wizard) fileName = "wizard.png";
            else if (entity instanceof Monster) {
                fileName = ((Monster) entity).getName().toLowerCase().replace(" ", "_") + ".png";
            } else {
                fileName = "default.png";
            }

            return getCachedImage(fileName);
        }

        private Image getCachedImage(String fileName) {
            if (!textureCache.containsKey(fileName)) {
                textureCache.put(fileName, loadImage(fileName));
            }
            return textureCache.get(fileName);
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
            
            boolean exploring = (state.currentState == GameState.State.EXPLORING);
            boolean combat = (state.currentState == GameState.State.COMBAT);

            // Przełączanie widoczności przycisków
            attackBtn.setVisible(combat);
            skillBtn.setVisible(combat);
            fleeBtn.setVisible(combat);

            if (exploring) {
                drawExploration(g, state);
                drawLevelHeader(g, state); // Rysowanie poziomu na górze
            } else if (combat) {
                drawCombat(g, state);
            } else if (state.currentState == GameState.State.GAME_OVER) {
                drawGameOver(g);
            }
            
            drawSidebar(g, state, exploring);
        }

        private void drawLevelHeader(Graphics g, GameState state) {
            g.setColor(new Color(0, 0, 0, 150)); // Półprzezroczyste tło dla czytelności
            g.fillRect(0, 0, GameState.MAP_SIZE * GameState.TILE_SIZE, 35);
            
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String levelText = "LEVEL: " + state.currentLevel;
            int textWidth = g.getFontMetrics().stringWidth(levelText);
            // Centrowanie tekstu na obszarze mapy
            g.drawString(levelText, (GameState.MAP_SIZE * GameState.TILE_SIZE) / 2 - (textWidth / 2), 25);
        }

        private void drawExploration(Graphics g, GameState state) {
            int ts = GameState.TILE_SIZE;
            
            // 1. Trawa
            for (int x = 0; x < GameState.MAP_SIZE; x++) {
                for (int y = 0; y < GameState.MAP_SIZE; y++) {
                    if (grassImg != null) g.drawImage(grassImg, x * ts, y * ts, ts, ts, null);
                    else { g.setColor(new Color(34, 139, 34)); g.fillRect(x * ts, y * ts, ts, ts); }
                }
            }

            // 2. Ściany
            for (Point p : state.walls) renderSprite(g, wallImg, Color.GRAY, p.x, p.y);
            
            // 3. Miasto
            renderSprite(g, townImg, Color.PINK, state.townLocation.x, state.townLocation.y);
            
            // 4. Schody (Wyjście)
            renderSprite(g, exitImg, Color.BLUE, state.exitLocation.x, state.exitLocation.y);
            
            // 5. Boss (Zawsze krol_goblinow.png)
            renderSprite(g, getCachedImage("krol_goblinow.png"), Color.RED, state.bossLocation.x, state.bossLocation.y);
            
            // 6. Zwykłe potwory
            for (GameState.MonsterEntity me : state.enemiesOnMap) {
                renderSprite(g, getEntityImage(me.monster), Color.GREEN, me.location.x, me.location.y);
            }
            
            // 7. Gracz
            renderSprite(g, getEntityImage(state.player), Color.CYAN, state.playerX, state.playerY);
        }

        private void renderSprite(Graphics g, Image img, Color fallback, int x, int y) {
            int ts = GameState.TILE_SIZE;
            if (img != null) {
                g.drawImage(img, x * ts, y * ts, ts, ts, null);
            } else {
                g.setColor(fallback);
                g.fillRect(x * ts + 5, y * ts + 5, ts - 10, ts - 10);
            }
        }

        private void drawCombat(Graphics g, GameState state) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GameState.MAP_SIZE * GameState.TILE_SIZE, GameState.MAP_SIZE * GameState.TILE_SIZE);
            
            if (state.currentEnemy != null) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 22));
                g.drawString("WALKA!", 50, 45);
                
                boolean isBoss = state.currentEnemy instanceof Boss;
                int spriteSize = isBoss ? 160 : 100;
                
                Image enemyImg = getEntityImage(state.currentEnemy);
                if (enemyImg != null) {
                    g.drawImage(enemyImg, 240, 40, spriteSize, spriteSize, null);
                }

                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString(state.currentEnemy.getName(), 50, 85);
                
                // Pasek HP
                int x = 50, y = 105, fullWidth = 200, height = 20;
                g.setColor(new Color(60, 0, 0));
                g.fillRect(x, y, fullWidth, height);
                
                double hpRatio = (double)state.currentEnemy.getHp() / state.currentEnemy.getMaxHp();
                int filledWidth = (int) (fullWidth * Math.max(0, Math.min(1, hpRatio)));

                if (hpRatio > 0.5) g.setColor(Color.GREEN);
                else if (hpRatio > 0.2) g.setColor(Color.YELLOW);
                else g.setColor(Color.RED);
                
                g.fillRect(x, y, filledWidth, height);
                g.setColor(Color.WHITE);
                g.drawRect(x, y, fullWidth, height);
                g.drawString((int)state.currentEnemy.getHp() + " / " + state.currentEnemy.getMaxHp(), x + 60, y + 15);
            }
        }

        private void drawGameOver(Graphics g) {
            g.setColor(Color.BLACK); 
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED); 
            g.setFont(new Font("Arial", Font.BOLD, 40));
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
            g.drawString("Poziom Postaci: " + state.player.getLevel(), x, 150);

            g.setColor(Color.CYAN);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("ZAŁOŻONE:", x, 310);
            
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            g.setColor(Color.WHITE);

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
