package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.game.character.Warrior;
import fantasyrealms.game.character.Wizard;
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
                // Granice mapy
                if (x == 0 || x == GameState.MAP_SIZE - 1 || y == 0 || y == GameState.MAP_SIZE - 1) {
                    walls.add(new Point(x, y));
                // Losowe ściany, z wyłączeniem początkowej pozycji Miasta (1, 1)
                } else if (r.nextInt(100) < 15 && !(x == 1 && y == 1)) { 
                    walls.add(new Point(x, y));
                }
            }
        }
        return walls;
    }

    public void handleInput(int key) {
        if (state.currentState == GameState.State.GAME_OVER) return;

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

        // Interakcja z Miastem
        if (nextP.equals(state.townLocation)) {
            showTownDialog();
            return;
        }

        // Aktualizacja pozycji gracza
        state.playerX = nextX;
        state.playerY = nextY;

        // Mała szansa na odnowienie many po ruchu
        if (Math.random() < 0.1) state.player.restoreMana(5); 
    }

    private void showTownDialog() {
        Character player = state.player;
        String[] options = {"Sklep", "Leczenie (10 złota)", "Kowal (Ulepszanie)", "Wyjdź"};
        int choice = JOptionPane.showOptionDialog(null, "Witaj w mieście! Złoto: " + player.getGold(), "Miasto",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Sklep
            int cost = 50;
            if (JOptionPane.showConfirmDialog(null, "Kupić losowy przedmiot za " + cost + "?", "Sklep", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (player.removeGold(cost)) {
                    player.addItem(ItemDatabase.getShopItem()); 
                    JOptionPane.showMessageDialog(null, "Kupiono przedmiot!");
                } else {
                    JOptionPane.showMessageDialog(null, "Brak złota!");
                }
            }
        } else if (choice == 1) { // Leczenie
            if (player.removeGold(10)) {
                player.heal(999);
                JOptionPane.showMessageDialog(null, "Jesteś w pełni zdrowy!");
            }
        } else if (choice == 2) { // Kowal
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