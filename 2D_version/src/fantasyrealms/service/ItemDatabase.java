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
        // ================= BIŻUTERIA =================

        // COMMON
        commonItems.add(new Item("Miedziany Pierścień", "Prosta ozdoba", "RING", "COMMON", 2));
        commonItems.add(new Item("Rzemyk", "Prosty wisiorek", "NECKLACE", "COMMON", 2));
        commonItems.add(new Item("Żelazny Sygnet", "Ciężki pierścień", "RING", "COMMON", 3));
        commonItems.add(new Item("Kościany Naszyjnik", "Zrobiony z zębów", "NECKLACE", "COMMON", 3));

        // RARE
        rareItems.add(new Item("Srebrny Pierścień", "Lśni w ciemności", "RING", "RARE", 5));
        rareItems.add(new Item("Srebrny Łańcuch", "Solidna ochrona szyi", "NECKLACE", "RARE", 5));
        rareItems.add(new Item("Pierścień Rubinu", "Pulsuje ciepłem", "RING", "RARE", 7));
        rareItems.add(new Item("Amulet Wilka", "Zwiększa wytrzymałość", "NECKLACE", "RARE", 7));

        // EPIC
        epicItems.add(new Item("Złoty Pierścień Króla", "Majestatyczny", "RING", "EPIC", 12));
        epicItems.add(new Item("Medalion Odwagi", "Chroni serce", "NECKLACE", "EPIC", 12));
        epicItems.add(new Item("Sygnet Cienia", "Dla zabójców", "RING", "EPIC", 15));
        epicItems.add(new Item("Wisior Many", "Wzmacnia aurę", "NECKLACE", "EPIC", 15));

        // LEGENDARY
        legendaryItems.add(new Item("Pierścień Władzy", "Jeden by wszystkimi rządzić", "RING", "LEGENDARY", 30));
        legendaryItems.add(new Item("Serce Oceanu", "Starożytny klejnot", "NECKLACE", "LEGENDARY", 30));

        // ================= BRONIE I ZBROJE  =================
        
        // --- COMMON ---
        commonItems.add(new Item("Zardzewiały Miecz", "Ledwo tnie", "WEAPON", "COMMON", 5));
        commonItems.add(new Item("Pałka Goblinów", "Kawał drewna", "WEAPON", "COMMON", 6));
        commonItems.add(new Item("Sztylet Bandyty", "Krótki i tępy", "WEAPON", "COMMON", 7));
        commonItems.add(new Item("Stary Kostur", "Dla maga amatora", "WEAPON", "COMMON", 6));
        commonItems.add(new Item("Proca", "Kamienie bolą", "WEAPON", "COMMON", 4));
        commonItems.add(new Item("Widły", "Dla chłopa", "WEAPON", "COMMON", 5));
        commonItems.add(new Item("Tępy Toporek", "Do rąbania drewna", "WEAPON", "COMMON", 6));
        
        commonItems.add(new Item("Drewniana Tarcza", "Lepsze to niż nic", "ARMOR", "COMMON", 3));
        commonItems.add(new Item("Skórzana Kurtka", "Pachnie wilkiem", "ARMOR", "COMMON", 5));
        commonItems.add(new Item("Dziurawe Buty", "Nie dają obrony", "ARMOR", "COMMON", 1));
        commonItems.add(new Item("Szmaty Żebraka", "Zimno w tym", "ARMOR", "COMMON", 2));
        commonItems.add(new Item("Słomiany Kapelusz", "Chroni przed słońcem", "ARMOR", "COMMON", 1));
        
        commonItems.add(new Item("Mała Mikstura", "Leczy 20 HP", "POTION", "COMMON", 20));
        commonItems.add(new Item("Chleb", "Leczy 10 HP", "POTION", "COMMON", 10));

        // --- RARE ---
        rareItems.add(new Item("Stalowy Miecz", "Solidna robota", "WEAPON", "RARE", 15));
        rareItems.add(new Item("Topór Wikinga", "Ciężki i ostry", "WEAPON", "RARE", 18));
        rareItems.add(new Item("Włócznia Strażnika", "Długa", "WEAPON", "RARE", 16));
        rareItems.add(new Item("Sejmitar", "Zakrzywione ostrze", "WEAPON", "RARE", 17));
        rareItems.add(new Item("Morgensztern", "Kolczasta kula", "WEAPON", "RARE", 19));
        rareItems.add(new Item("Łuk Myśliwski", "Celny", "WEAPON", "RARE", 14));
        rareItems.add(new Item("Katana", "Ostra stal", "WEAPON", "RARE", 20));
        rareItems.add(new Item("Halabarda", "Długa i groźna", "WEAPON", "RARE", 21));

        rareItems.add(new Item("Kolczuga", "Chroni przed cięciami", "ARMOR", "RARE", 12));
        rareItems.add(new Item("Szata Maga", "Lekka, magiczna", "ARMOR", "RARE", 8));
        rareItems.add(new Item("Hełm Rycerski", "Chroni głowę", "ARMOR", "RARE", 10));
        rareItems.add(new Item("Utwardzana Skóra", "Dla łowcy", "ARMOR", "RARE", 9));
        rareItems.add(new Item("Żelazne Rękawice", "Mocny chwyt", "ARMOR", "RARE", 7));
        rareItems.add(new Item("Tarcza Puklerz", "Mała i poręczna", "ARMOR", "RARE", 11));
        
        rareItems.add(new Item("Średnia Mikstura", "Leczy 50 HP", "POTION", "RARE", 50));

        // --- EPIC ---
        epicItems.add(new Item("Ostrze Cienia", "Szybkie jak wiatr", "WEAPON", "EPIC", 30));
        epicItems.add(new Item("Młot Krasnoludów", "Miażdży kości", "WEAPON", "EPIC", 35));
        epicItems.add(new Item("Kostur Żywiołów", "Pazur magii", "WEAPON", "EPIC", 32));
        epicItems.add(new Item("Ostrze Pustyni", "Wysusza wrogów", "WEAPON", "EPIC", 33));
        epicItems.add(new Item("Topór Berserkera", "Szał bitewny", "WEAPON", "EPIC", 38));
        epicItems.add(new Item("Złoty Rapier", "Elegancja i śmierć", "WEAPON", "EPIC", 29));
        epicItems.add(new Item("Ognisty Bicz", "Parzy przy dotyku", "WEAPON", "EPIC", 31));
        epicItems.add(new Item("Lodowa lanca", "Mrozi krew", "WEAPON", "EPIC", 34));

        epicItems.add(new Item("Płytowa Zbroja", "Pełna ochrona", "ARMOR", "EPIC", 25));
        epicItems.add(new Item("Smocza Łuska", "Zbroja z gada", "ARMOR", "EPIC", 28));
        epicItems.add(new Item("Szata Arcykapłana", "Święta aura", "ARMOR", "EPIC", 20));
        epicItems.add(new Item("Pancerz Paladyna", "Lśni w słońcu", "ARMOR", "EPIC", 27));
        epicItems.add(new Item("Zbroja Samuraja", "Lekka i twarda", "ARMOR", "EPIC", 24));
        epicItems.add(new Item("Tarcza Wieżowa", "Mur nie do przebicia", "ARMOR", "EPIC", 30));
        
        epicItems.add(new Item("Wielka Mikstura", "Leczy 100 HP", "POTION", "EPIC", 100));

        // --- LEGENDARY ---
        legendaryItems.add(new Item("EXCALIBUR", "Miecz Królów", "WEAPON", "LEGENDARY", 60));
        legendaryItems.add(new Item("Kostur Arcymaga", "Potęga czysta", "WEAPON", "LEGENDARY", 65));
        legendaryItems.add(new Item("Sztylet Zabójcy Bogów", "Krytyczna moc", "WEAPON", "LEGENDARY", 58));
        legendaryItems.add(new Item("Mjolnir", "Młot piorunów", "WEAPON", "LEGENDARY", 70));
        legendaryItems.add(new Item("Gungnir", "Włócznia Odyna", "WEAPON", "LEGENDARY", 68));
        legendaryItems.add(new Item("Ostrze Chaosu", "Płonie ogniem", "WEAPON", "LEGENDARY", 66));
        
        legendaryItems.add(new Item("Zbroja Boga Wojny", "Niezniszczalna", "ARMOR", "LEGENDARY", 50));
        legendaryItems.add(new Item("Tarcza Egida", "Odbija wszystko", "ARMOR", "LEGENDARY", 45));
        legendaryItems.add(new Item("Szata Nieśmiertelnego", "Magiczna bariera", "ARMOR", "LEGENDARY", 40));
        legendaryItems.add(new Item("Zbroja Smoczego Władcy", "Zrobiona z czarnych łusek", "ARMOR", "LEGENDARY", 55));
        
        legendaryItems.add(new Item("Eliksir Nieśmiertelności", "Leczy wszystko (999)", "POTION", "LEGENDARY", 999));
    }

    public static Item getRandomDrop() {
        return rollItem(0.55, 0.3, 0.12, 0.03); // Trochę większa szansa na lepsze
    }

    public static Item getShopItem() {
        return rollItem(0.4, 0.4, 0.15, 0.05);
    }

    private static Item rollItem(double cChance, double rChance, double eChance, double lChance) {
        double roll = random.nextDouble();
        
        if (roll < lChance) return getRandomFrom(legendaryItems);
        else if (roll < lChance + eChance) return getRandomFrom(epicItems);
        else if (roll < lChance + eChance + rChance) return getRandomFrom(rareItems);
        else return getRandomFrom(commonItems);
    }

    private static Item getRandomFrom(List<Item> list) {
        if(list.isEmpty()) return null;
        return list.get(random.nextInt(list.size()));
    }
}