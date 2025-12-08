package fantasyrealms.game.character;

import fantasyrealms.game.item.Item;
import fantasyrealms.app.ConsoleHelper; 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected int hp, maxHp, mana, maxMana;
    protected int baseAttack, baseDefense;
    protected int level, xp, gold;
    
    protected List<Item> inventory = new ArrayList<>();
    protected Item equippedWeapon;
    protected Item equippedArmor;
    protected Item equippedRing;      
    protected Item equippedNecklace;  
    
    protected Map<String, Integer> reputation = new HashMap<>();

    public Character(String name, int maxHp, int maxMana, int baseAttack, int baseDefense) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.level = 1;
        this.gold = 100;
    }

    // --- NOWOŚĆ: Metoda dla Kowala ---
    public void upgradeEquippedWeapon() {
        if (equippedWeapon != null) {
            equippedWeapon.upgrade();
            System.out.println(ConsoleHelper.YELLOW_BOLD + "Broń ulepszona! Nowa moc: " + equippedWeapon.getValue() + ConsoleHelper.RESET);
        }
    }
    // --------------------------------

    public int getTotalAttack() {
        int bonus = 0;
        if (equippedWeapon != null) bonus += equippedWeapon.getValue();
        if (equippedRing != null) bonus += equippedRing.getValue();
        return baseAttack + bonus;
    }

    public int getTotalDefense() {
        int bonus = 0;
        if (equippedArmor != null) bonus += equippedArmor.getValue();
        if (equippedNecklace != null) bonus += equippedNecklace.getValue();
        return baseDefense + bonus;
    }

    public void takeDamage(int dmg) {
        int defense = getTotalDefense();
        int finalDmg = Math.max(1, dmg - (defense / 3)); 
        this.hp -= finalDmg;
        System.out.println(ConsoleHelper.RED + name + " otrzymuje " + finalDmg + " obrażeń! (HP: " + hp + ")" + ConsoleHelper.RESET);
    }

    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
        System.out.println(ConsoleHelper.GREEN + name + " uleczony o " + amount + " HP." + ConsoleHelper.RESET);
    }
    
    public void restoreMana(int amount) {
        this.mana = Math.min(maxMana, this.mana + amount);
        System.out.println(ConsoleHelper.BLUE + "Odzyskano " + amount + " many." + ConsoleHelper.RESET);
    }

    public void addItem(Item item) {
        inventory.add(item);
        System.out.println(ConsoleHelper.CYAN + "Otrzymano przedmiot: " + item.getName() + ConsoleHelper.RESET);
    }

    public void equipItem(int index) {
        if (index < 0 || index >= inventory.size()) return;
        Item item = inventory.get(index);
        String type = item.getType();
        String msgColor = ConsoleHelper.CYAN;

        switch (type) {
            case "WEAPON":
                if (equippedWeapon != null) inventory.add(equippedWeapon);
                equippedWeapon = item;
                inventory.remove(index);
                System.out.println(msgColor + "Założono broń." + ConsoleHelper.RESET);
                break;
            case "ARMOR":
                if (equippedArmor != null) inventory.add(equippedArmor);
                equippedArmor = item;
                inventory.remove(index);
                System.out.println(msgColor + "Założono zbroję." + ConsoleHelper.RESET);
                break;
            case "RING":
                if (equippedRing != null) inventory.add(equippedRing);
                equippedRing = item;
                inventory.remove(index);
                System.out.println(msgColor + "Założono pierścień." + ConsoleHelper.RESET);
                break;
            case "NECKLACE":
                if (equippedNecklace != null) inventory.add(equippedNecklace);
                equippedNecklace = item;
                inventory.remove(index);
                System.out.println(msgColor + "Założono naszyjnik." + ConsoleHelper.RESET);
                break;
            case "POTION":
                heal(item.getValue());
                inventory.remove(index);
                break;
            default:
                System.out.println("Nie możesz tego założyć.");
        }
    }

    public void addGold(int amount) { 
        this.gold += amount; 
        System.out.println(ConsoleHelper.YELLOW + "Złoto +" + amount + ConsoleHelper.RESET);
    }
    
    public boolean removeGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        System.out.println(ConsoleHelper.RED + "Za mało złota!" + ConsoleHelper.RESET);
        return false;
    }

    public void modifyReputation(String factionName, int amount) {
        reputation.put(factionName, reputation.getOrDefault(factionName, 0) + amount);
    }

    public int getReputation(String factionName) {
        return reputation.getOrDefault(factionName, 0);
    }
    
    public void gainXp(int amount) { 
        this.xp += amount; 
        if(xp >= level * 100) {
            level++;
            maxHp += 20;
            maxMana += 10;
            hp = maxHp;
            mana = maxMana;
            baseAttack += 5;
            System.out.println(ConsoleHelper.YELLOW_BOLD + "!!! AWANS !!! Poziom " + level + ConsoleHelper.RESET);
        }
    }

    // Gettery
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getGold() { return gold; }
    public boolean isAlive() { return hp > 0; }
    public List<Item> getInventory() { return inventory; }
    public Item getEquippedWeapon() { return equippedWeapon; }
    public Item getEquippedArmor() { return equippedArmor; }
    public Item getEquippedRing() { return equippedRing; }
    public Item getEquippedNecklace() { return equippedNecklace; }

    public abstract void specialAttack(Character target);
}