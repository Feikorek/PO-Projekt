package fantasyrealms.game.character;

public class Wizard extends Character {
    public Wizard(String name) {
        super(name, 80, 100, 25, 5);
    }

    @Override
    public String getSkillDescription(int index) {
        if (index == 1) return "Kula Ognia (20 Mana)";
        if (index == 2) return "Leczenie (25 Mana)";
        return "-";
    }

    @Override
    public void useSkill(int skillIndex, Character target) {
        if (skillIndex == 1) {
            if (this.mana >= 20) {
                this.mana -= 20;
                target.takeDamage((int)(getTotalAttack() * 2.5));
            }
        } else if (skillIndex == 2) {
            if (this.mana >= 25) {
                this.mana -= 25;
                this.heal(40);
            }
        }
    }

    @Override
    public void specialAttack(Character target) {
        useSkill(1, target);
    }
}