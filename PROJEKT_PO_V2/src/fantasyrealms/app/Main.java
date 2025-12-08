package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.game.character.Warrior;
import fantasyrealms.game.character.Wizard;
import fantasyrealms.game.dungeon.Dungeon;
import fantasyrealms.service.GameService;
import fantasyrealms.service.QuestService;
import fantasyrealms.world.location.Town;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuestService qs = new QuestService();
        Character player = null;
        
        ConsoleHelper.clear();
        System.out.println(ConsoleHelper.YELLOW_BOLD + "\n=== FANTASY REALMS RPG ===" + ConsoleHelper.RESET);

        System.out.println("1. Nowa Gra");
        System.out.println("2. Wczytaj Grę");
        String startChoice = sc.nextLine();

        if (startChoice.equals("2")) {
            player = GameService.loadGame();
        }

        if (player == null) {
            System.out.println("\n--- KREATOR POSTACI ---");
            System.out.println("Wybierz klasę: 1. Wojownik | 2. Czarodziej");
            String c = sc.nextLine();
            System.out.print("Podaj imię: ");
            String n = sc.nextLine();
            
            if(c.equals("1")) {
                player = new Warrior(n);
            } else {
                player = new Wizard(n);
            }
            player.modifyReputation("OrderOfLight", 10);
        }

        // GŁÓWNA PĘTLA
        boolean gameRunning = true;
        while(gameRunning && player.isAlive()) {
            // Kolorowe statystyki
            String hpCol = ConsoleHelper.RED;
            String manaCol = ConsoleHelper.BLUE;
            String goldCol = ConsoleHelper.YELLOW;
            String rst = ConsoleHelper.RESET;

            System.out.println("\n--- " + ConsoleHelper.WHITE_BOLD + player.getName() + " Lv." + player.getLevel() + rst + " ---");
            System.out.println("HP: " + hpCol + player.getHp() + "/" + player.getMaxHp() + rst + 
                             " | Mana: " + manaCol + player.getMana() + rst + 
                             " | Złoto: " + goldCol + player.getGold() + rst);

            System.out.println("1. Miasto (Sklep/Questy)");
            System.out.println("2. Las (Lv 1)");
            System.out.println("3. Północna Zima (Lv 2)");
            System.out.println("4. Pustynia (Lv 3)");
            System.out.println("5. Oaza (Lv 4)");
            System.out.println("6. Jaskinia (Lv 5 - BOSS)");
            System.out.println("7. ZARZĄDZANIE (Ekwipunek)");
            System.out.println("8. Zapisz Grę");
            System.out.println("9. Wyjście");
            System.out.print("> ");

            String choice = sc.nextLine();
            
            // Czyścimy ekran po wyborze
            ConsoleHelper.clear();

            switch(choice) {
                case "1": new Town(qs).enter(player, sc); break;
                case "2": new Dungeon(3, 1, "Mroczny Las", qs).enter(player, sc); break;
                case "3": new Dungeon(4, 2, "Północna Zima", qs).enter(player, sc); break;
                case "4": new Dungeon(4, 3, "Wielka Pustynia", qs).enter(player, sc); break;
                case "5": new Dungeon(5, 4, "Przeklęta Oaza", qs).enter(player, sc); break;
                case "6": new Dungeon(6, 5, "Głęboka Jaskinia", qs).enter(player, sc); break;
                case "7": manageInventory(player, sc, qs); break;
                case "8": GameService.saveGame(player); break;
                case "9": gameRunning = false; break;
                default: System.out.println("Zły wybór.");
            }
        }
        System.out.println("Koniec gry.");
    }

    private static void manageInventory(Character p, Scanner sc, QuestService qs) {
        ConsoleHelper.clear(); 
        String rst = ConsoleHelper.RESET;

        System.out.println("\n=== KARTA POSTACI ===");
        // Atak - Czerwony, Obrona - Zielony
        System.out.println("Atak: " + ConsoleHelper.RED + p.getTotalAttack() + rst + 
                         " | Obrona: " + ConsoleHelper.GREEN + p.getTotalDefense() + rst);
        System.out.println("XP: " + p.getXp());
        
        System.out.println("\n[ ZAŁOŻONE ]");
        // toString() kolor
        System.out.println("Broń: " + (p.getEquippedWeapon() != null ? p.getEquippedWeapon().toString() : "Brak"));
        System.out.println("Zbroja: " + (p.getEquippedArmor() != null ? p.getEquippedArmor().toString() : "Brak"));
        System.out.println("Pierścień: " + (p.getEquippedRing() != null ? p.getEquippedRing().toString() : "Brak"));
        System.out.println("Naszyjnik: " + (p.getEquippedNecklace() != null ? p.getEquippedNecklace().toString() : "Brak"));

        System.out.println("\n[ PLECAK ]");
        if (p.getInventory().isEmpty()) {
            System.out.println("(Pusto)");
            System.out.println("\nWciśnij Enter by wrócić.");
            sc.nextLine();
            return;
        }

        for (int i = 0; i < p.getInventory().size(); i++) {
            System.out.println((i+1) + ". " + p.getInventory().get(i).toString());
        }

        System.out.println("\nWpisz numer przedmiotu, aby go założyć (lub 0 by wrócić):");
        try {
            String input = sc.nextLine();
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < p.getInventory().size()) {
                p.equipItem(idx);
            }
        } catch (NumberFormatException e) {

        }
        ConsoleHelper.clear();
    }
}