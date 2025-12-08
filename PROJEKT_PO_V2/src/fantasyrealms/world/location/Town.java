package fantasyrealms.world.location;

import fantasyrealms.app.ConsoleHelper;
import fantasyrealms.game.character.Character;
import fantasyrealms.game.item.Item;
import fantasyrealms.game.quest.KillQuest;
import fantasyrealms.service.*;
import fantasyrealms.world.faction.OrderOfLight;
import java.util.Scanner;

public class Town {
    private QuestService qs;
    
    public Town(QuestService qs) { this.qs = qs; }

    public void enter(Character p, Scanner sc) {
        OrderOfLight faction = new OrderOfLight();
        
        ConsoleHelper.clear();
        System.out.println(ConsoleHelper.YELLOW_BOLD + "\n--- MIASTO WAROWNIA ---" + ConsoleHelper.RESET);
        
        boolean stay = true;
        while(stay) {
            System.out.println("\nZłoto: " + ConsoleHelper.YELLOW + p.getGold() + ConsoleHelper.RESET);
            System.out.println("1. Sklep Wielobranżowy");
            System.out.println("2. " + ConsoleHelper.CYAN + "Kowal (Enchanting +1)" + ConsoleHelper.RESET);
            System.out.println("3. " + ConsoleHelper.GREEN + "Alchemik (Crafting)" + ConsoleHelper.RESET);
            System.out.println("4. Tablica Zadań");
            System.out.println("5. Wyjdź na mapę");
            
            System.out.print("> ");
            String w = sc.nextLine();
            
            if(w.equals("1")) shop(p, sc, faction);
            else if(w.equals("2")) blacksmith(p, sc); 
            else if(w.equals("3")) alchemist(p, sc); 
            else if(w.equals("4")) {
                System.out.println(ConsoleHelper.CYAN + "Nowe zadanie: Zabij 3 Gobliny" + ConsoleHelper.RESET);
                qs.addQuest(new KillQuest("Tępienie Goblinów", "Goblin", 3, 100));
            }
            else if(w.equals("5")) stay = false;
        }
    }

    // --- SYSTEM ENCHANTINGU ---
    private void blacksmith(Character p, Scanner sc) {
        ConsoleHelper.clear();
        System.out.println("--- KOWAL ---");
        System.out.println("Kowal: 'Mogę ulepszyć twoją broń, ale to kosztuje.'");
        
        if (p.getEquippedWeapon() == null) {
            System.out.println(ConsoleHelper.RED + "Nie masz założonej broni!" + ConsoleHelper.RESET);
            return;
        }

        Item weapon = p.getEquippedWeapon();
        int cost = 100 * (weapon.getUpgradeLevel() + 1); // Koszt rośnie z poziomem
        
        System.out.println("Twoja broń: " + weapon.getName());
        System.out.println("Koszt ulepszenia: " + cost + " złota.");
        System.out.println("1. Ulepsz | 2. Wróć");
        
        if(sc.nextLine().equals("1")) {
            if(p.removeGold(cost)) {
                // System ryzyka - 80% szans
                if(Math.random() < 0.8) {
                    p.upgradeEquippedWeapon();
                    System.out.println(ConsoleHelper.GREEN + "Sukces! Broń lśni nową mocą!" + ConsoleHelper.RESET);
                } else {
                    System.out.println(ConsoleHelper.RED + "Porażka! Kowalowi omsknął się młot. Złoto przepadło." + ConsoleHelper.RESET);
                }
            }
        }
    }

    // --- SYSTEM CRAFTINGU (Alchemia) ---
    private void alchemist(Character p, Scanner sc) {
        ConsoleHelper.clear();
        System.out.println("--- ALCHEMIK ---");
        System.out.println("Alchemik: 'Mieszam zioła i magię. Czego potrzebujesz?'");
        System.out.println("1. Uwarz Miksturę Leczenia (Koszt: 40 złota)");
        System.out.println("2. Uwarz Eliksir Many (Koszt: 40 złota)");
        System.out.println("3. Wróć");
        
        String c = sc.nextLine();
        if(c.equals("1")) {
            if(p.removeGold(40)) {
                System.out.println("Alchemik miesza składniki... Gotowe!");
                p.addItem(new Item("Mikstura Rzemieślnicza", "Leczy 80 HP", "POTION", "COMMON", 80));
            }
        } else if(c.equals("2")) {
            if(p.removeGold(40)) {
                System.out.println("Wypijasz wywar na miejscu...");
                p.restoreMana(80);
            }
        }
    }

    private void shop(Character p, Scanner sc, OrderOfLight f) {
        ConsoleHelper.clear();
        double mod = f.getPriceMod(p);
        int price = (int)(20 * mod);

        boolean inShop = true;
        while(inShop) {
            System.out.println(ConsoleHelper.YELLOW + "\n--- SKLEP ---" + ConsoleHelper.RESET);
            System.out.println("Twoje złoto: " + ConsoleHelper.YELLOW_BOLD + p.getGold() + ConsoleHelper.RESET);
            System.out.println("1. Kup losowy przedmiot (Cena: " + price + ")");
            System.out.println("2. Sprzedaj wszystko z plecaka");
            System.out.println("3. Wróć do miasta");
            System.out.println("4. " + ConsoleHelper.PURPLE + "(Szeptem) Szukam rozrywki... [Kasyno]" + ConsoleHelper.RESET);
            
            System.out.print("> ");
            String w = sc.nextLine();
            
            if(w.equals("1")) {
                if(p.removeGold(price)) {
                    Item bought = ItemDatabase.getShopItem(); 
                    p.addItem(bought);
                    System.out.println("Kupiono: " + bought.toString());
                }
            } else if(w.equals("2")) {
                if(p.getInventory().isEmpty()) {
                    System.out.println("Masz pusty plecak.");
                } else {
                    int totalEarned = 0;
                    for(Item i : p.getInventory()) {
                        int value = i.getValue() / 2;
                        p.addGold(value);
                        totalEarned += value;
                    }
                    p.getInventory().clear();
                    System.out.println(ConsoleHelper.GREEN + "Sprzedano wszystko za " + totalEarned + " złota." + ConsoleHelper.RESET);
                }
            } else if(w.equals("3")) {
                inShop = false;
            } else if(w.equals("4")) {
                new Casino().enter(p, sc);
            }
        }
    }
}