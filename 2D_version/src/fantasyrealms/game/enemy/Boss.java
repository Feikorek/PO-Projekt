package fantasyrealms.game.enemy;

import fantasyrealms.game.character.Character;

public class Boss extends Monster {

    private String specialSkillName;
    private boolean isEnraged = false;
    private int chargeTurn = 0;

    public Boss(String name, int hp, int atk, int def, String skillName) {
        // Przekazujemy 1.0 jako dropChance - Boss zawsze coś zostawia
        super(name, hp, atk, def, 1.0);
        this.specialSkillName = skillName;
    }

    @Override
    public void specialAttack(Character target) {
        // Logika Enrage: Jeśli życie spadnie poniżej 50% max HP
        // Używamy getMaxHp(), ponieważ pole w Monster jest prywatne
        if (!isEnraged && getHp() < (getMaxHp() / 2)) {
            isEnraged = true;
            this.baseAttack += 10;
            // Opcjonalnie: można tu dodać komunikat o furii w konsoli
        }

        chargeTurn++;

        // Faza ładowania ataku
        if (chargeTurn == 3) {
            // W tej turze Boss "mruczy" lub przygotowuje moc - brak obrażeń
            return;
        }

        // Faza potężnego uderzenia
        if (chargeTurn >= 4) {
            int massiveDmg = getTotalAttack() * 3;

            // Specjalne przeliczniki dla unikalnych umiejętności
            if (specialSkillName.equals("Piekielny Ogień")) massiveDmg = 100;
            if (specialSkillName.equals("Koniec Świata")) massiveDmg = 999;

            target.takeDamage(massiveDmg);
            chargeTurn = 0;
            return;
        }

        // Standardowy atak z szansą na krytyka
        if (Math.random() < 0.3) {
            target.takeDamage((int)(getTotalAttack() * 1.5));
        } else {
            target.takeDamage(getTotalAttack());
        }
    }

    public String getSpecialSkillName() {
        return specialSkillName;
    }
}
