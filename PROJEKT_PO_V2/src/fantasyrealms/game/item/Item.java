package fantasyrealms.game.item;

import fantasyrealms.app.ConsoleHelper;
import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private String type;   // WEAPON, ARMOR, RING, NECKLACE, POTION
    private String rarity; // COMMON, RARE, EPIC, LEGENDARY
    private int value; 
    
    //Enchanting
    private int upgradeLevel = 0;

    public Item(String name, String description, String type, String rarity, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.value = value;
    }

    // Metoda do ulepszania broni
    public void upgrade() {
        this.upgradeLevel++;

        this.value = (int)(this.value * 1.2) + 2;
    }
    
    public int getUpgradeLevel() { return upgradeLevel; }

    public String getName() { 

        return name + (upgradeLevel > 0 ? " +" + upgradeLevel : ""); 
    }
    
    public String getType() { return type; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        String color = ConsoleHelper.WHITE;
        String prefix = "";

        if ("LEGENDARY".equals(rarity)) {
            color = ConsoleHelper.YELLOW_BOLD; 
            prefix = "[LEGENDARNY] ";
        } else if ("EPIC".equals(rarity)) {
            color = ConsoleHelper.PURPLE; 
            prefix = "[EPICKI] ";
        } else if ("RARE".equals(rarity)) {
            color = ConsoleHelper.BLUE; 
            prefix = "[RZADKI] ";
        }

        return color + prefix + getName() + " (Moc: " + value + ") - " + description + ConsoleHelper.RESET;
    }
}