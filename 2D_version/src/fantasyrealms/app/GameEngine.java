package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.game.character.Warrior;
import fantasyrealms.game.character.Wizard;
import fantasyrealms.game.enemy.Boss;
import fantasyrealms.game.enemy.EnemyFactory;
import fantasyrealms.game.enemy.Monster;
import fantasyrealms.game.item.Item;
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

    public GameState getState() { return state; }

    private Character createCharacter() {
        String[] options = {"Wojownik", "Czarodziej"};
        int choice = JOptionPane.showOptionDialog(null, "Wybierz klasę postaci:", "Kreator Postaci",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        String name = JOptionPane.showInputDialog("Podaj imię bohatera:");
        if (name == null || name.trim().isEmpty()) name = "Bezimienny";
        
        return (choice == 0) ? new Warrior(name) : new Wizard(name);
    }

    private List<Point> generateMap() {
        List<Point> walls = new ArrayList<>();
        Random r = new Random();

        // Punkty, które MUSZĄ być wolne od ścian
        List<Point> reservedPoints = new ArrayList<>();
        reservedPoints.add(new Point(1, 1)); // Start/Miasto
        reservedPoints.add(new Point(10, 10)); // Boss
        
        // Bufor bezpieczeństwa wokół kluczowych punktów (zapobiega murowaniu)
        addBufferZone(reservedPoints, new Point(1, 1));
        addBufferZone(reservedPoints, new Point(10, 10));

        for (int x = 0; x < GameState.MAP_SIZE; x++) {
            for (int y = 0; y < GameState.MAP_SIZE; y++) {
                Point current = new Point(x, y);
                
                // Granice mapy
                if (x == 0 || x == GameState.MAP_SIZE - 1 || y == 0 || y == GameState.MAP_SIZE - 1) {
                    walls.add(current);
                } 
                // Losowe ściany (15% szans), omijając punkty zarezerwowane
                else if (r.nextInt(100) < 15) {
                    if (!reservedPoints.contains(current)) {
                        walls.add(current);
                    }
                }
            }
        }
        return walls;
    }

    private void addBufferZone(List<Point> reserved, Point center) {
        reserved.add(new Point(center.x + 1, center.y));
        reserved.add(new Point(center.x - 1, center.y));
        reserved.add(new Point(center.x, center.y + 1));
        reserved.add(new Point(center.x, center.y - 1));
    }

    private void generateEnemies() {
        Random r = new Random();
        state.enemiesOnMap.clear();

        for (int i = 0; i < 8; i++) {
            Point p = new Point(r.nextInt(GameState.MAP_SIZE), r.nextInt(GameState.MAP_SIZE));

            if (!state.walls.contains(p) && 
                !p.equals(state.townLocation) && 
                !p.equals(state.bossLocation) &&
                !p.equals(new Point(state.playerX, state.playerY))) {
                
                Monster m = EnemyFactory.spawnForRegion(state.player.getLevel());
                state.enemiesOnMap.add(new GameState.MonsterEntity(m, p));
            }
        }
    }

    public void handleInput(int key) {
        if (state.currentState == GameState.State.GAME_OVER) return;
        if (state.currentState == GameState.State.COMBAT) { handleCombatInput(key); return; }

        int nextX = state.playerX, nextY = state.playerY;
        if (key == KeyEvent.VK_UP) nextY--;
        else if (key == KeyEvent.VK_DOWN) nextY++;
        else if (key == KeyEvent.VK_LEFT) nextX--;
        else if (key == KeyEvent.VK_RIGHT) nextX++;
        else if (key == KeyEvent.VK_E) { showInventory(); return; }

        Point nextP = new Point(nextX, nextY);
        
        // Sprawdzanie kolizji ze ścianami i granicami
        if (state.walls.contains(nextP) || nextX < 0 || nextX >= GameState.MAP_SIZE || nextY < 0 || nextY >= GameState.MAP_SIZE) return;

        // Lokacje specjalne
        if (nextP.equals(state.townLocation)) { showTownDialog(); return; }
        if (nextP.equals(state.bossLocation)) { startCombat(EnemyFactory.spawnBoss(state.player.getLevel())); return; }

        // Kolizja z potworami
        for (GameState.MonsterEntity me : new ArrayList<>(state.enemiesOnMap)) {
            if (me.location.equals(nextP)) {
                state.enemiesOnMap.remove(me);
                startCombat(me.monster);
                return;
            }
        }
        
        state.playerX = nextX; state.playerY = nextY;
    }

    private void startCombat(Monster enemy) {
        state.currentEnemy = enemy;
        state.currentState = GameState.State.COMBAT;
    }

    private void handleCombatInput(int key) {
        if (state.currentEnemy == null) return;
        
        if (key == KeyEvent.VK_1) {
            state.currentEnemy.takeDamage(state.player.getTotalAttack());
        } else if (key == KeyEvent.VK_2) {
            state.player.specialAttack(state.currentEnemy);
        } else if (key == KeyEvent.VK_3 && !(state.currentEnemy instanceof Boss)) {
            state.currentState = GameState.State.EXPLORING;
            state.currentEnemy = null;
            return;
        }

        if (!state.currentEnemy.isAlive()) {
            state.player.gainXp(50);
            state.player.addGold(20);
            state.currentEnemy = null;
            state.currentState = GameState.State.EXPLORING;
            return;
        }

        state.currentEnemy.specialAttack(state.player);
        if (!state.player.isAlive()) state.currentState = GameState.State.GAME_OVER;
    }

    private void showTownDialog() {
        String[] options = {"Sklep (50g)", "Leczenie (10g)", "Kowal", "Wyjdź"};
        int choice = JOptionPane.showOptionDialog(null, 
            "Witaj w mieście. Złoto: " + state.player.getGold(), 
            "Miasto", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (choice == 0) { // Sklep
            if (state.player.getGold() >= 50) {
                state.player.removeGold(50);
                Item dropped = ItemDatabase.getShopItem();
                state.player.addItem(dropped);
                JOptionPane.showMessageDialog(null, "Zakupiono: " + dropped.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Masz za mało złota!");
            }
        } else if (choice == 1) { // Leczenie
            if (state.player.removeGold(10)) state.player.heal(999);
        } else if (choice == 2) {
            handleBlacksmith();
        }
    }

    private void handleBlacksmith() {
        Item weapon = state.player.getEquippedWeapon();
        if (weapon == null) {
            JOptionPane.showMessageDialog(null, "Musisz mieć założoną broń, aby ją ulepszyć!");
            return;
        }
        
        int cost = 100;
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Ulepszenie kosztuje " + cost + "g. Szansa na sukces: 80%. Kontynuować?", 
            "Kowal", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION && state.player.removeGold(cost)) {
            if (Math.random() < 0.8) {
                state.player.upgradeEquippedWeapon();
                JOptionPane.showMessageDialog(null, "Sukces! Twoja broń jest teraz silniejsza.");
            } else {
                state.player.destroyEquippedWeapon();
                JOptionPane.showMessageDialog(null, "Porażka! Broń pękła podczas kucia.");
            }
        }
    }

    private void showInventory() {
        List<Item> inv = state.player.getInventory();
        if (inv.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ekwipunek jest pusty!");
            return;
        }

        // NAPRAWA WYŚWIETLANIA: Tworzymy tablicę Stringów na podstawie toString()
        String[] itemDisplayList = new String[inv.size()];
        for (int i = 0; i < inv.size(); i++) {
            itemDisplayList[i] = inv.get(i).toString();
        }

        String selected = (String) JOptionPane.showInputDialog(
                null, 
                "Wybierz przedmiot do użycia/założenia:", 
                "Ekwipunek", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                itemDisplayList, 
                itemDisplayList[0]);

        if (selected != null) {
            // Znajdujemy indeks wybranego tekstu w tablicy
            for (int i = 0; i < itemDisplayList.length; i++) {
                if (itemDisplayList[i].equals(selected)) {
                    state.player.equipItem(i); // Używamy metody equipItem z indeksem
                    break;
                }
            }
        }
    }
}