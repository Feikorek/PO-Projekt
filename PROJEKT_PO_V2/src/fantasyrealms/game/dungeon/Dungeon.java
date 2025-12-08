package fantasyrealms.game.dungeon;

import fantasyrealms.app.ConsoleHelper; 
import fantasyrealms.game.character.Character;
import fantasyrealms.game.enemy.*; 
import fantasyrealms.game.item.Item;
import fantasyrealms.service.QuestService;
import java.util.Scanner;

public class Dungeon {
    private int size, danger;
    private String regionName;
    private QuestService qs;
    
    public Dungeon(int size, int danger, String regionName, QuestService qs) {
        this.size = size;
        this.danger = danger;
        this.regionName = regionName;
        this.qs = qs;
    }

    public void enter(Character player, Scanner sc) {
        ConsoleHelper.clear();
        System.out.println(ConsoleHelper.RED_BOLD + "\n>>> Wyruszasz do: " + regionName.toUpperCase() + " (Poziom: " + danger + ") <<<" + ConsoleHelper.RESET);
        System.out.println("Twój cel: Znajdź legowisko Bossa w głębi lochu (Prawy Dolny Róg)!");

        int x = 0, y = 0;
        int bossX = size - 1;
        int bossY = size - 1;

        boolean exploring = true;
        while (player.isAlive() && exploring) {
            


            // Rysowanie mapy
            System.out.println("\n--- MAPA ---");
            for(int i=0; i<size; i++) {
                for(int j=0; j<size; j++) {
                    if(j==x && i==y) System.out.print(ConsoleHelper.GREEN + "[P]" + ConsoleHelper.RESET); 
                    else if (j==bossX && i==bossY) System.out.print(ConsoleHelper.RED + "[B]" + ConsoleHelper.RESET); // B jak Boss
                    else System.out.print("[ ]");
                }
                System.out.println();
            }

            System.out.println("Lokalizacja: [" + x + "," + y + "]. Cel (B) jest na [" + bossX + "," + bossY + "].");
            System.out.println("Ruch (N/S/E/W) lub (Q) ucieczka");
            
            // Szansa na zwykłą walkę
            if (Math.random() > 0.6 && (x != 0 || y != 0)) {
                Monster m = EnemyFactory.spawnForRegion(danger);
                battle(player, m, sc);
                if (!player.isAlive()) return; 
            }

            System.out.print("> ");
            String c = sc.nextLine().toUpperCase();
            
            switch(c) {
                case "N": if(y > 0) y--; else System.out.println("Ściana."); break;
                case "S": if(y < size-1) y++; else System.out.println("Ściana."); break;
                case "E": if(x < size-1) x++; else System.out.println("Ściana."); break;
                case "W": if(x > 0) x--; else System.out.println("Ściana."); break;
                case "Q": exploring = false; System.out.println("Uciekasz z lochu..."); break;
                default: System.out.println("Nieznany kierunek.");
            }
        }
    }

    private void battle(Character player, Monster enemy, Scanner sc) {
        System.out.println(ConsoleHelper.RED_BOLD + "\n!!! ATAK !!! Napotkano: " + enemy.getName() + ConsoleHelper.RESET);
        
        while(enemy.isAlive() && player.isAlive()) {
            System.out.println(player.getName() + " (HP: " + player.getHp() + " | Mana: " + player.getMana() + ") vs " + 
                               ConsoleHelper.RED + enemy.getName() + " (HP: " + enemy.getHp() + ")" + ConsoleHelper.RESET);
            System.out.println("1. Atak  2. Specjalna Umiejętność  3. Ucieczka");
            
            String choice = sc.nextLine();
            if (choice.equals("2")) {
                player.specialAttack(enemy);
            } else if (choice.equals("3") && !(enemy instanceof Boss)) { 
                // Ucieczka
                if (Math.random() > 0.5) {
                    System.out.println("Udało ci się uciec z walki!");
                    return; 
                } else {
                    System.out.println("Nie udało się uciec!");
                }
            } else {
                System.out.println("Atakujesz bronią.");
                enemy.takeDamage(player.getTotalAttack());
            }

            if(enemy.isAlive()) {

                if (enemy instanceof Boss && Math.random() < 0.3) {
                    enemy.specialAttack(player); 
                } else if (Math.random() < 0.1) {
                    enemy.specialAttack(player); 
                } else {
                    System.out.println(enemy.getName() + " atakuje cię!");
                    player.takeDamage(enemy.getTotalAttack());
                }
            }
        }

        if(player.isAlive()) {
            System.out.println(ConsoleHelper.GREEN + "Zwycięstwo!" + ConsoleHelper.RESET);
            
            // Nagroda za Bossa jest większa
            int xpReward = (enemy instanceof Boss) ? 500 * danger : 25 * danger;
            player.gainXp(xpReward); 
            
            qs.updateQuests(enemy.getName());
            
            Item loot = enemy.tryDropLoot();
            if(loot != null) {
                System.out.println("Zdobyto przedmiot: " + loot.toString()); 
                player.addItem(loot);
            }
        } else {
            System.out.println(ConsoleHelper.RED_BOLD + "Poległeś..." + ConsoleHelper.RESET);
        }
    }
}