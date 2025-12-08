package fantasyrealms.service;

import fantasyrealms.app.ConsoleHelper;
import fantasyrealms.game.character.Character;
import java.util.Random;
import java.util.Scanner;

public class RandomEventService {
    private Random random = new Random();

    public void triggerEvent(Character p) {
        // 40% szansy na wydarzenie podczas podróży
        if (random.nextInt(100) < 40) { 
            ConsoleHelper.clear();
            System.out.println(ConsoleHelper.PURPLE + "\n--- WYDARZENIE W PODRÓŻY ---" + ConsoleHelper.RESET);
            int event = random.nextInt(4);

            switch (event) {
                case 0:
                    System.out.println("Spotykasz wędrownego kapłana.");
                    System.out.println("Kapłan: 'Niech światło cię prowadzi.' (Uleczono w pełni!)");
                    p.heal(9999);
                    break;
                case 1:
                    System.out.println("Wpadasz w sidła kłusowników!");
                    System.out.println("Musisz się uwolnić, ale ranisz się przy tym.");
                    p.takeDamage(25);
                    break;
                case 2:
                    System.out.println("Znajdujesz starą, zgubioną sakiewkę przy drodze.");
                    int goldFound = random.nextInt(50) + 10;
                    p.addGold(goldFound);
                    break;
                case 3:
                    System.out.println("Tajemnicze źródło magii!");
                    System.out.println("Czujesz przypływ mocy. Twoja Mana została odnowiona.");
                    p.restoreMana(100);
                    break;
            }
            System.out.println("\n(Naciśnij Enter, aby ruszyć dalej...)");
            new Scanner(System.in).nextLine();
        }
    }
}