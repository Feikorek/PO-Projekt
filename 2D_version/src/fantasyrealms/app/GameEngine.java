package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.game.character.Warrior;
import fantasyrealms.game.character.Wizard;
import fantasyrealms.game.enemy.Monster;
import fantasyrealms.game.enemy.Boss;
import fantasyrealms.game.enemy.EnemyFactory;
import fantasyrealms.service.ItemDatabase;

import javax.swing.JOptionPane;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    private GameState state;

    public GameEngine() {
        Character initialPlayer = createCharacter();
        List<Point> initialWalls = generateMap();
        this.state = new GameState(initialPlayer, initialWalls);
        generateEnemies();
    }
    
    public GameState getState() {
        return state;
    }

    private Character createCharacter() {
        String[] options = {"Wojownik", "Czarodziej"};
        int choice = JOptionPane.showOptionDialog(null, "Wybierz klasę postaci:", "Kreator Postaci",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        String name = JOptionPane.showInputDialog("Podaj imię bohatera:");
        if (name == null || name.trim().isEmpty()) name = "Bezimienny";

        if (choice == 0) return new Warrior(name);
        else return new Wizard(name);
    }

    private List<Point> generateMap() {
        List<Point> walls = new ArrayList<>();
        Random r = new Random();

        for (int x = 0; x < GameState.MAP_SIZE; x++) {
            for (int y = 0; y < GameState.MAP_SIZE; y++) {
                if (x == 0 || x == GameState.MAP_SIZE - 1 || y == 0 || y == GameState.MAP_SIZE - 1) {
                    walls.add(new Point(x, y));
                } else if (r.nextInt(100) < 15 && !(x == 1 && y == 1)) {
                    walls.add(new Point(x, y));
                }
            }
        }
        return walls;
    }

    private void generateEnemies() {
        Random r = new Random();
        state.enemiesOnMap.clear();

        state.bossLocation = new Point(10, 10);

        for (int i = 0; i < 8; i++) {
            int x = r.nextInt(GameState.MAP_SIZE);
            int y = r.nextInt(GameState.MAP_SIZE);
            Point p = new Point(x, y);

            if (!state.walls.contains(p) && !p.equals(state.townLocation)) {
                Monster m = EnemyFactory.spawnForRegion(state.player.getLevel());
                state.enemiesOnMap.add(new MonsterEntity(m, p));
            }
        }
    }

    public void handleInput(int key) {
        if (state.currentState == GameState.State.GAME_OVER) return;
        if (state.currentState == GameState.State.COMBAT) {
            handleCombatInput(key);
            return;
        }

        int nextX = state.playerX;
        int nextY = state.playerY;

        if (key == KeyEvent.VK_UP) nextY--;
        if (key == KeyEvent.VK_DOWN) nextY++;
        if (key == KeyEvent.VK_LEFT) nextX--;
        if (key == KeyEvent.VK_RIGHT) nextX++;
        
        if (key == KeyEvent.VK_E) {
            showInventory();
            return;
        }

        Point nextP = new Point(nextX, nextY);
        
        if (state.walls.contains(nextP) || nextX < 0 || nextX >= GameState.MAP_SIZE || nextY < 0 || nextY >= GameState.MAP_SIZE) {
            return;
        }

        if (nextP.equals(state.townLocation)) {
            showTownDialog();
            return;
        }

        if (nextP.equals(state.bossLocation)) {
            state.currentEnemy = EnemyFactory.spawnBoss(state.player.getLevel());
            state.currentState = GameState.State.COMBAT;
            return;
        }

        for (MonsterEntity me : new ArrayList<>(state.enemiesOnMap)) {
            if (me.location.equals(nextP)) {
                state.currentEnemy = me.monster;
                state.enemiesOnMap.remove(me);
                state.currentState = GameState.State.COMBAT;
                return;
            }
        }

        state.playerX = nextX;
        state.playerY = nextY;

        if (Math.random() < 0.1) state.player.restoreMana(5); 
    }

    public void handleCombatInput(int key) {
        if (state.currentEnemy == null) return;

        if (key == KeyEvent.VK_1) {
            state.currentEnemy.takeDamage(state.player.getTotalAttack());
        } else if (key == KeyEvent.VK_2) {
            state.player.specialAttack(state.currentEnemy);
        } else if (key == KeyEvent.VK_3) {
            if (!(state.currentEnemy instanceof Boss)) {
                state.currentState = GameState.State.EXPLORING;
                state.currentEnemy = null;
            }
            return;
        }

        if (!state.currentEnemy.isAlive()) {
            state.player.gainXp(50);
            state.currentEnemy = null;
            state.currentState = GameState.State.EXPLORING;
            return;
        }

        state.currentEnemy.specialAttack(state.player);

        if (!state.player.isAlive()) {
            state.currentState = GameState.State.GAME_OVER;
        }
    }

    private void showTownDialog() {
        Character player = state.player;
        String[] options = {"Sklep", "Leczenie (10 złota)", "Kowal (Ulepszanie)", "Wyjdź"};
        int choice = JOptionPane.showOptionDialog(null, "Witaj w mieście! Złoto: " + player.getGold(), "Miasto",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            int cost = 50;
            if (JOptionPane.showConfirmDialog(null, "Kupić losowy przedmiot za " + cost + "?", "Sklep", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (player.removeGold(cost)) {
                    player.addItem(ItemDatabase.getShopItem()); 
                    JOptionPane.showMessageDialog(null, "Kupiono przedmiot!");
                } else {
                    JOptionPane.showMessageDialog(null, "Brak złota!");
                }
            }
        } else if (choice == 1) {
            if (player.removeGold(10)) {
                player.heal(999);
                JOptionPane.showMessageDialog(null, "Jesteś w pełni zdrowy!");
            }
        } else if (choice == 2) {
            if (player.getEquippedWeapon() != null) {
                if (JOptionPane.showConfirmDialog(null, 
                        "Ulepszyć broń za 100 złota? (Ryzyko zniszczenia!)", 
                        "Kowal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (player.removeGold(100)) {
                        if (Math.random() < 0.8) {
                            player.upgradeEquippedWeapon();
                            JOptionPane.showMessageDialog(null, "Sukces!");
                        } else {
                            player.destroyEquippedWeapon();
                            JOptionPane.showMessageDialog(null, "Broń pękła!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Brak złota!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nie masz broni w ręce!");
            }
        }
    }

    private void showInventory() {
        Character player = state.player;
        if (player.getInventory().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Plecak jest pusty.");
            return;
        }

        String[] itemNames = new String[player.getInventory().size()];
        for (int i = 0; i < player.getInventory().size(); i++) {
            itemNames[i] = player.getInventory().get(i).toString();
        }

        String selected = (String) JOptionPane.showInputDialog(null, 
                "Wybierz przedmiot do założenia/użycia:",
                "Ekwipunek", JOptionPane.PLAIN_MESSAGE, null, itemNames, itemNames[0]);

        if (selected != null) {
            for (int i = 0; i < itemNames.length; i++) {
                if (itemNames[i].equals(selected)) {
                    player.equipItem(i); 
                    JOptionPane.showMessageDialog(null, "Użyto przedmiotu.");
                    break;
                }
            }
        }
    }
}
