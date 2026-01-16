package fantasyrealms.game.enemy;

import fantasyrealms.game.character.Character;
import fantasyrealms.service.ItemDatabase;
import fantasyrealms.game.item.Item;

public class Monster extends Character {

    private static final long serialVersionUID = 1L;
    private double dropChance;

    public Monster(String name, int hp, int atk, int def, double dropChance) {
        super(name, hp, 20, atk, def);
        this.dropChance = dropChance;
    }

    @Override
    public String getSkillDescription(int index) {
        return "Atak Potwora";
    }

    @Override
    public void useSkill(int skillIndex, Character target) {
        target.takeDamage(getTotalAttack());
    }

    @Override
    public void specialAttack(Character target) {
        target.takeDamage((int)(getTotalAttack() * 1.5));
    }

    public Item tryDropLoot() {
        if (Math.random() < dropChance) {
            return ItemDatabase.getRandomDrop();
        }
        return null;
    }
}
