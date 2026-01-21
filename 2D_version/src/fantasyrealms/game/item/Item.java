package fantasyrealms.game.item;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private String type;   
    private String rarity; 
    private int value; 
    private int upgradeLevel = 0;

    public Item(String name, String description, String type, String rarity, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.value = value;
    }

    // TA METODA MUSI BYĆ DOKŁADNIE TAKA - TO ONA ODPOWIADA ZA WYŚWIETLANIE NAZWY
    @Override
    public String toString() {
        String statLabel;
        switch (type) {
            case "WEAPON": statLabel = "ATK"; break;
            case "ARMOR":  statLabel = "DEF"; break;
            case "POTION": statLabel = "HEAL"; break;
            default:       statLabel = "STAT"; break;
        }

        String levelInfo = (upgradeLevel > 0 ? " +" + upgradeLevel : "");
        
        // Zwraca czytelny tekst zamiast fantasyrealms.game.item.Item@...
        return "[" + rarity + "] " + name + levelInfo + " (" + type + ") | " + statLabel + ": " + value;
    }

    // Reszta metod (upgrade, gettery itp.)
    public void upgrade() {
        this.upgradeLevel++;
        this.value = (int)(this.value * 1.2) + 2;
    }

    public String getName() { 
        return name + (upgradeLevel > 0 ? " +" + upgradeLevel : ""); 
    }

    public String getType() { return type; }
    
    public int getValue() { return value; }

    public String getRarity() { 
        return rarity; 
    }

    // DODAJ TĘ METODĘ - Jej brakował najbardziej:
    public String getDescription() {
        return description;
    }
    
}