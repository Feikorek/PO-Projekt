package fantasyrealms.game.character;

import fantasyrealms.game.item.Item;
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

    // --- NOWOŚĆ: System Obrony ---
    protected boolean isDefending = false;
    protected int defenseBonus = 0;

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

    // --- System Walki: Obrona ---
    public void defend() {
        this.isDefending = true;
        System.out.println(name + " przyjmuje postawę obronną!");
    }

    public void resetTurn() {
        this.isDefending = false;
        if (defenseBonus > 0) defenseBonus -= 2; 
        if (defenseBonus < 0) defenseBonus = 0;
    }

    // --- System Walki: Obrażenia ---
    public void takeDamage(int dmg) {
        int defense = getTotalDefense();
        
        if (isDefending) {
            defense *= 2; // Podwójna obrona przy bloku
        }

        int finalDmg = Math.max(1, dmg - (defense / 3)); 
        this.hp -= finalDmg;
    }

    public int getTotalDefense() {
        int bonus = defenseBonus;
        if (equippedArmor != null) bonus += equippedArmor.getValue();
        if (equippedNecklace != null) bonus += equippedNecklace.getValue();
        return baseDefense + bonus;
    }

    public int getTotalAttack() {
        int bonus = 0;
        if (equippedWeapon != null) bonus += equippedWeapon.getValue();
        if (equippedRing != null) bonus += equippedRing.getValue();
        return baseAttack + bonus;
    }
    
    // --- ABSTRAKCYJNE METODY SKILLI (Wymagane przez SwingGame) ---
    public abstract void useSkill(int skillIndex, Character target);
    public abstract String getSkillDescription(int index);

    // --- Metody dla Kowala ---
    public void upgradeEquippedWeapon() {
        if (equippedWeapon != null) {
            equippedWeapon.upgrade();
        }
    }
    
    public void destroyEquippedWeapon() {
        if (equippedWeapon != null) {
            equippedWeapon = null;
        }
    }

    // Standardowe metody
    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }
    
    public void restoreMana(int amount) {
        this.mana = Math.min(maxMana, this.mana + amount);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void equipItem(int index) {
        if (index < 0 || index >= inventory.size()) return;
        Item item = inventory.get(index);
        
        String type = item.getType();

        switch (type) {
            case "WEAPON":
                if (equippedWeapon != null) inventory.add(equippedWeapon);
                equippedWeapon = item;
                inventory.remove(index);
                break;
            case "ARMOR":
                if (equippedArmor != null) inventory.add(equippedArmor);
                equippedArmor = item;
                inventory.remove(index);
                break;
            case "RING":
                if (equippedRing != null) inventory.add(equippedRing);
                equippedRing = item;
                inventory.remove(index);
                break;
            case "NECKLACE":
                if (equippedNecklace != null) inventory.add(equippedNecklace);
                equippedNecklace = item;
                inventory.remove(index);
                break;
            case "POTION":
                heal(item.getValue());
                inventory.remove(index);
                break;
        }
    }

    public void addGold(int amount) { this.gold += amount; }
    
    public boolean removeGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
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