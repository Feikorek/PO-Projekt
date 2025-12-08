package fantasyrealms.world.location;

import fantasyrealms.app.ConsoleHelper;
import fantasyrealms.game.character.Character;
import java.util.Random;
import java.util.Scanner;

public class Casino {
    private Random random = new Random();

    public void enter(Character p, Scanner sc) {
        ConsoleHelper.clear();
        System.out.println(ConsoleHelper.PURPLE + "\n=== 🕵️ CZARNY RYNEK (PODZIEMIA SKLEPU) 🕵️ ===" + ConsoleHelper.RESET);
        System.out.println("Dym cygar gryzie w oczy. Słyszysz brzęk monet i ciche przekleństwa.");
        
        boolean inCasino = true;
        while (inCasino) {
            System.out.println("\n" + ConsoleHelper.YELLOW + "Twoje złoto: " + p.getGold() + ConsoleHelper.RESET);
            System.out.println("1. Gra w Kubki (Ryzyko: Średnie | Wygrana: 3x)");
            System.out.println("2. Jednoręki Bandyta (Ryzyko: Niskie | Wygrana: 2x-50x)");
            System.out.println("3. Wróć na górę (Do sklepu)");
            System.out.print("> ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    playShellGame(p, sc);
                    break;
                case "2":
                    playSlots(p, sc);
                    break;
                case "3":
                    inCasino = false;
                    break;
                default:
                    System.out.println("Nie ma takiej gry.");
            }
        }
        ConsoleHelper.clear();
    }

    // --- GRA 1: KULKI W KUBECZKACH ---
    private void playShellGame(Character p, Scanner sc) {
        System.out.println(ConsoleHelper.CYAN + "\n--- 🥤 TRZY KUBKI 🥤 ---" + ConsoleHelper.RESET);
        int bet = askForBet(p, sc);
        if (bet <= 0) return;

        p.removeGold(bet); 

        int ballPosition = random.nextInt(3) + 1; 

        System.out.println("Gospodarz tasuje kubki... Szur... Szur...");
        System.out.print("Gdzie jest kulka? (Wybierz 1, 2 lub 3): ");
        
        String input = sc.nextLine();
        int guess = 0;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("To nie liczba!");
            return;
        }

        System.out.println("Podnoszenie kubka...");
        try { Thread.sleep(1000); } catch (Exception e) {} 
        
        if (guess == ballPosition) {
            int winnings = bet * 3;
            p.addGold(winnings);
            System.out.println(ConsoleHelper.GREEN + " BRAWO! Kulka tam była! Wygrywasz " + winnings + " złota!" + ConsoleHelper.RESET);
        } else {
            System.out.println(ConsoleHelper.RED + " PUSTO! Kulka była pod kubkiem nr " + ballPosition + ConsoleHelper.RESET);
        }
    }

    // --- GRA 2: AUTOMATY ---
    private void playSlots(Character p, Scanner sc) {
        System.out.println(ConsoleHelper.CYAN + "\n---  JEDNORĘKI BANDYTA  ---" + ConsoleHelper.RESET);
        int bet = askForBet(p, sc);
        if (bet <= 0) return;

        p.removeGold(bet);

        String[] symbols = {"O", "@", "#", "5", "7"};
        
        System.out.println("Kręcenie...");
        try { Thread.sleep(1000); } catch (Exception e) {}

        String s1 = symbols[random.nextInt(symbols.length)];
        String s2 = symbols[random.nextInt(symbols.length)];
        String s3 = symbols[random.nextInt(symbols.length)];

        System.out.println(ConsoleHelper.PURPLE + " [ " + s1 + " | " + s2 + " | " + s3 + " ] " + ConsoleHelper.RESET);

        if (s1.equals(s2) && s2.equals(s3)) {
            // Jackpot (wszystkie 3 takie same)
            int multiplier = s1.equals("💎") ? 50 : 10;
            int win = bet * multiplier;
            p.addGold(win);
            System.out.println(ConsoleHelper.YELLOW_BOLD + " JACKPOT! Wygrywasz " + win + " złota!" + ConsoleHelper.RESET);
        } else if (s1.equals(s2) || s2.equals(s3) || s1.equals(s3)) {
            // Dwójka (2 takie same)
            int win = (int)(bet * 1.5);
            p.addGold(win);
            System.out.println(ConsoleHelper.GREEN + "Dwa takie same! Wygrywasz " + win + " złota." + ConsoleHelper.RESET);
        } else {
            System.out.println(ConsoleHelper.RED + "Przegrałeś." + ConsoleHelper.RESET);
        }
    }

    private int askForBet(Character p, Scanner sc) {
        System.out.print("Ile stawiasz? (Max: " + p.getGold() + "): ");
        try {
            int bet = Integer.parseInt(sc.nextLine());
            if (bet > p.getGold()) {
                System.out.println(ConsoleHelper.RED + "Nie masz tyle złota!" + ConsoleHelper.RESET);
                return 0;
            }
            if (bet <= 0) {
                System.out.println("Stawka musi być dodatnia.");
                return 0;
            }
            return bet;
        } catch (NumberFormatException e) {
            System.out.println("Błąd wpisywania.");
            return 0;
        }
    }
}