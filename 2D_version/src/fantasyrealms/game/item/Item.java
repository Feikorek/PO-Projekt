package fantasyrealms.game.item;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private String type;   // WEAPON, ARMOR, RING, NECKLACE, POTION
    private String rarity; // COMMON, RARE, EPIC, LEGENDARY
    private int value; 
    
    // --- NOWOŚĆ: Poziom ulepszenia (Enchanting) ---
    private int upgradeLevel = 0;

    public Item(String name, String description, String type, String rarity, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.value = value;
    }

    // Metoda do ulepszania
    public void upgrade() {
        this.upgradeLevel++;
        // Każde ulepszenie zwiększa statystyki o 20% + 2 punkty stałe
        this.value = (int)(this.value * 1.2) + 2;
    }
    
    public int getUpgradeLevel() { return upgradeLevel; }

    public String getName() { 
        // Jeśli przedmiot jest ulepszony, dodajemy "+X" do nazwy
        return name + (upgradeLevel > 0 ? " +" + upgradeLevel : ""); 
    }
    
    public String getType() { return type; }
    public int getValue() { return value; }

}