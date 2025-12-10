package fantasyrealms.game.character;

public class Warrior extends Character {
    public Warrior(String name) {
        super(name, 150, 30, 15, 10);
    }

    @Override
    public String getSkillDescription(int index) {
        if (index == 1) return "Potężne Uderzenie (20 Mana)";
        if (index == 2) return "Okrzyk Bojowy (10 Mana)";
        return "-";
    }

    @Override
    public void useSkill(int skillIndex, Character target) {
        if (skillIndex == 1) {
            if (this.mana >= 20) {
                this.mana -= 20;
                target.takeDamage((int)(getTotalAttack() * 2.0));
            }
        } else if (skillIndex == 2) {
            if (this.mana >= 10) {
                this.mana -= 10;
                this.defenseBonus += 10;
            }
        }
    }

    @Override
    public void specialAttack(Character target) {
        useSkill(1, target);
    }
}