package fantasyrealms.game.enemy;

import fantasyrealms.game.character.Character;
import fantasyrealms.service.ItemDatabase;
import fantasyrealms.game.item.Item;

/**
 * Klasa reprezentująca przeciwnika w grze.
 * Dziedziczy po klasie Character, dodając logikę łupów (loot) oraz
 * przechowywanie maksymalnego zdrowia do poprawnego wyświetlania paska HP.
 */
public class Monster extends Character {

    private static final long serialVersionUID = 1L;
    private double dropChance;
    private int maxHp; // Przechowuje początkową wartość HP dla paska postępu

    public Monster(String name, int hp, int atk, int def, double dropChance) {
        // Character constructor: name, hp, mp, atk, def
        super(name, hp, 20, atk, def);
        this.maxHp = hp;
        this.dropChance = dropChance;
    }

    @Override
    public int getMaxHp() {
        return this.maxHp;
    }

    @Override
    public String getSkillDescription(int index) {
        return "Podstawowy atak potwora";
    }

    @Override
    public void useSkill(int skillIndex, Character target) {
        // Potwory zazwyczaj używają prostego przelicznika ataku
        target.takeDamage(getTotalAttack());
    }

    @Override
    public void specialAttack(Character target) {
        // Silniejszy atak (150% obrażeń), używany przez silniejsze stwory
        int damage = (int) (getTotalAttack() * 1.5);
        target.takeDamage(damage);
    }

    
    // Wylosowany przedmiot lub null, jeśli się nie udało.
    
    public Item tryDropLoot() {
        if (Math.random() < dropChance) {
            return ItemDatabase.getRandomDrop();
        }
        return null;
    }
}
