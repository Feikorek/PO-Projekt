package fantasyrealms.service;

import fantasyrealms.game.character.Character;
import java.io.*;

public class GameService {
    
    private static final String SAVE_FILE = "savegame.dat";

    // ZAPIS
    public static void saveGame(Character player) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(player);
            System.out.println("\n>>> GRA ZOSTAŁA ZAPISANA POMYŚLNIE! <<<");
        } catch (IOException e) {
            System.out.println("Błąd zapisu gry: " + e.getMessage());
        }
    }

    // ODCZYT
    public static Character loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("Brak pliku zapisu.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Character player = (Character) ois.readObject();
            System.out.println("\n>>> GRA WCZYTANA! Witaj ponownie, " + player.getName() + " <<<");
            return player;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd wczytywania gry: " + e.getMessage());
            return null;
        }
    }
}