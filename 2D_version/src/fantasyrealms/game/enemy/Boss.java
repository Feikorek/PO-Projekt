package fantasyrealms.game.enemy;

import fantasyrealms.game.character.Character;

public class Boss extends Monster {

    private String specialSkillName;
    private boolean isEnraged = false;
    private int chargeTurn = 0;

    public Boss(String name, int hp, int atk, int def, String skillName) {
        super(name, hp, atk, def, 1.0);
        this.specialSkillName = skillName;
    }

    @Override
    public void specialAttack(Character target) {

        if (!isEnraged && this.hp < (this.maxHp / 2)) {
            isEnraged = true;
            this.baseAttack += 10;
        }

        chargeTurn++;

        if (chargeTurn == 3) {
            // Boss przygotowuje potężny atak
            return;
        }

        if (chargeTurn >= 4) {
            int massiveDmg = getTotalAttack() * 3;

            if (specialSkillName.equals("Piekielny Ogień")) massiveDmg = 100;
            if (specialSkillName.equals("Koniec Świata")) massiveDmg = 999;

            target.takeDamage(massiveDmg);
            chargeTurn = 0;
            return;
        }

        if (Math.random() < 0.3) {
            target.takeDamage((int)(getTotalAttack() * 1.5));
        } else {
            target.takeDamage(getTotalAttack());
        }
    }
}
