package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.game.character.Warrior;
import fantasyrealms.game.character.Wizard;
import fantasyrealms.game.item.Item;
import fantasyrealms.service.ItemDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

// --- GŁÓWNY SILNIK GRAFICZNY ---
class GamePanel extends JPanel implements KeyListener {
    
    private final int TILE_SIZE = 48;
    private final int MAP_SIZE = 12;
    private final int SCREEN_WIDTH = TILE_SIZE * MAP_SIZE + 300;
    private final int SCREEN_HEIGHT = TILE_SIZE * MAP_SIZE;

    private enum State { EXPLORING, GAME_OVER }
    private State currentState = State.EXPLORING;

    private Character player;
    private int playerX = 1;
    private int playerY = 1;
    private List<Point> walls = new ArrayList<>();
    
    private String gameLog = "Rozpocznij przygodę!";

    private Point townLocation = new Point(1, 1); // Miasto na start

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        initGame();
    }

    private void initGame() {
        String[] options = {"Wojownik", "Czarodziej"};
        int choice = JOptionPane.showOptionDialog(this, "Wybierz klasę postaci:", "Kreator Postaci",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        String name = JOptionPane.showInputDialog("Podaj imię bohatera:");
        if (name == null || name.trim().isEmpty()) name = "Bezimienny";

        if (choice == 0) player = new Warrior(name);
        else player = new Wizard(name);

        generateMap();
    }

    private void generateMap() {
        walls.clear();
        Random r = new Random();

        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                if (x == 0 || x == MAP_SIZE - 1 || y == 0 || y == MAP_SIZE - 1) {
                    walls.add(new Point(x, y));
                } else if (r.nextInt(100) < 15 && !(x==1 && y==1)) {
                    walls.add(new Point(x, y));
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentState == State.EXPLORING) {
            drawExploration(g);
        } else if (currentState == State.GAME_OVER) {
            drawGameOver(g);
        }
        
        drawSidebar(g);
    }

    private void drawExploration(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, MAP_SIZE * TILE_SIZE, MAP_SIZE * TILE_SIZE);

        g.setColor(Color.GRAY);
        for (Point p : walls) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.GRAY);
        }

        g.setColor(Color.PINK);
        g.fillRect(townLocation.x * TILE_SIZE, townLocation.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawString("MIASTO", townLocation.x * TILE_SIZE + 2, townLocation.y * TILE_SIZE + 25);

        g.setColor(Color.CYAN);
        g.fillRoundRect(playerX * TILE_SIZE + 5, playerY * TILE_SIZE + 5, TILE_SIZE - 10, TILE_SIZE - 10, 15, 15);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("KONIEC GRY", 100, 200);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Poległeś... Zrestartuj grę.", 120, 250);
    }

    private void drawSidebar(Graphics g) {
        int x = MAP_SIZE * TILE_SIZE + 20;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(MAP_SIZE * TILE_SIZE, 0, 300, getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("STATYSTYKI", x, 30);
        
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Poziom: " + player.getLevel(), x, 60);
        g.drawString("XP: " + player.getXp(), x, 80);
        g.drawString("Złoto: " + player.getGold(), x, 100);
        g.drawString("Atak: " + player.getTotalAttack(), x, 120);
        g.drawString("Obrona: " + player.getTotalDefense(), x, 140);
        
        g.drawString("--- STEROWANIE ---", x, 200);
        g.drawString("Strzałki: Ruch", x, 220);
        g.drawString("E: Ekwipunek", x, 240);
        g.drawString("M: Miasto", x, 260);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (currentState == State.GAME_OVER) return;
        if (currentState == State.EXPLORING) {
            handleExplorationInput(e.getKeyCode());
        }
        repaint();
    }

    private void handleExplorationInput(int key) {
        int nextX = playerX;
        int nextY = playerY;

        if (key == KeyEvent.VK_UP) nextY--;
        if (key == KeyEvent.VK_DOWN) nextY++;
        if (key == KeyEvent.VK_LEFT) nextX--;
        if (key == KeyEvent.VK_RIGHT) nextX++;
        
        if (key == KeyEvent.VK_E) showInventory();

        Point nextP = new Point(nextX, nextY);
        if (walls.contains(nextP) || nextX < 0 || nextX >= MAP_SIZE || nextY < 0 || nextY >= MAP_SIZE) {
            return;
        }

        if (nextP.equals(townLocation)) {
            showTownDialog();
            return;
        }

        playerX = nextX;
        playerY = nextY;

        if(Math.random() < 0.1) player.restoreMana(5);
    }

    private void showTownDialog() {
        String[] options = {"Sklep", "Leczenie (10 złota)", "Kowal (Ulepszanie)", "Wyjdź"};
        int choice = JOptionPane.showOptionDialog(this, "Witaj w mieście! Złoto: " + player.getGold(), "Miasto",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            int cost = 50;
            int buy = JOptionPane.showConfirmDialog(this, "Kupić losowy przedmiot za " + cost + "?", "Sklep", JOptionPane.YES_NO_OPTION);
            if (buy == JOptionPane.YES_OPTION) {
                if (player.removeGold(cost)) {
                    player.addItem(ItemDatabase.getShopItem());
                    JOptionPane.showMessageDialog(this, "Kupiono przedmiot!");
                } else {
                    JOptionPane.showMessageDialog(this, "Brak złota!");
                }
            }
        } else if (choice == 1) {
            if (player.removeGold(10)) {
                player.heal(999);
                JOptionPane.showMessageDialog(this, "Jesteś w pełni zdrowy!");
            }
        } else if (choice == 2) {
            if (player.getEquippedWeapon() != null) {
                                int upgrade = JOptionPane.showConfirmDialog(this, 
                        "Ulepszyć broń za 100 złota? (Ryzyko zniszczenia!)", 
                        "Kowal", JOptionPane.YES_NO_OPTION);
                if (upgrade == JOptionPane.YES_OPTION) {
                    if (player.removeGold(100)) {
                        if (Math.random() < 0.8) {
                            player.upgradeEquippedWeapon();
                            JOptionPane.showMessageDialog(this, "Sukces!");
                        } else {
                            player.destroyEquippedWeapon();
                            JOptionPane.showMessageDialog(this, "Broń pękła!");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nie masz broni w ręce!");
            }
        }
    }

    private void showInventory() {
        if (player.getInventory().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plecak jest pusty.");
            return;
        }

        String[] itemNames = new String[player.getInventory().size()];
        for (int i = 0; i < player.getInventory().size(); i++) {
            itemNames[i] = player.getInventory().get(i).toString();
        }

        String selected = (String) JOptionPane.showInputDialog(this, 
                "Wybierz przedmiot do założenia/użycia:",
                "Ekwipunek", JOptionPane.PLAIN_MESSAGE, null, itemNames, itemNames[0]);

        if (selected != null) {
            for (int i = 0; i < itemNames.length; i++) {
                if (itemNames[i].equals(selected)) {
                    player.equipItem(i);
                    JOptionPane.showMessageDialog(this, "Użyto przedmiotu.");
                    break;
                }
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
