package fantasyrealms.service;

import fantasyrealms.game.item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemDatabase {
    private static List<Item> commonItems = new ArrayList<>();
    private static List<Item> rareItems = new ArrayList<>();
    private static List<Item> epicItems = new ArrayList<>();
    private static List<Item> legendaryItems = new ArrayList<>();
    
    private static Random random = new Random();

    static {
        // --- Przedmioty są dodawane poprawnie ---
        commonItems.add(new Item("Miedziany Pierścień", "Prosta ozdoba", "RING", "COMMON", 2));
        commonItems.add(new Item("Zardzewiały Miecz", "Ledwo tnie", "WEAPON", "COMMON", 5));
        commonItems.add(new Item("Drewniana Tarcza", "Lepsze to niż nic", "ARMOR", "COMMON", 3));
        commonItems.add(new Item("Mała Mikstura", "Leczy 20 HP", "POTION", "COMMON", 20));

        rareItems.add(new Item("Srebrny Pierścień", "Lśni w ciemności", "RING", "RARE", 5));
        rareItems.add(new Item("Stalowy Miecz", "Solidna robota", "WEAPON", "RARE", 15));
        rareItems.add(new Item("Kolczuga", "Chroni przed cięciami", "ARMOR", "RARE", 12));

        epicItems.add(new Item("Złoty Pierścień Króla", "Majestatyczny", "RING", "EPIC", 12));
        epicItems.add(new Item("Ostrze Cienia", "Szybkie jak wiatr", "WEAPON", "EPIC", 30));
        epicItems.add(new Item("Płytowa Zbroja", "Pełna ochrona", "ARMOR", "EPIC", 25));

        legendaryItems.add(new Item("EXCALIBUR", "Miecz Królów", "WEAPON", "LEGENDARY", 60));
        legendaryItems.add(new Item("Zbroja Boga Wojny", "Niezniszczalna", "ARMOR", "LEGENDARY", 50));
        legendaryItems.add(new Item("Eliksir Nieśmiertelności", "Leczy wszystko", "POTION", "LEGENDARY", 999));
    }

    public static Item getRandomDrop() {
        return rollItem(0.55, 0.3, 0.12, 0.03);
    }

    public static Item getShopItem() {
        return rollItem(0.4, 0.4, 0.15, 0.05);
    }

    private static Item rollItem(double c, double r, double e, double l) {
        double roll = random.nextDouble();
        if (roll < l) return getRandomFrom(legendaryItems);
        if (roll < l + e) return getRandomFrom(epicItems);
        if (roll < l + e + r) return getRandomFrom(rareItems);
        return getRandomFrom(commonItems);
    }

    private static Item getRandomFrom(List<Item> list) {
        if (list.isEmpty()) return null;
        Item template = list.get(random.nextInt(list.size()));
        
        // Tutaj Java wymaga, aby w klasie Item były metody getName(), getDescription() itd.
        return new Item(
            template.getName(), 
            template.getDescription(), 
            template.getType(), 
            template.getRarity(), 
            template.getValue()
        );
    }
}