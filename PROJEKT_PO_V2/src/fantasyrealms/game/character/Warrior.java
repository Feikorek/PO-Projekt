package fantasyrealms.game.character;

public class Warrior extends Character {
    public Warrior(String name) {
        // Dużo HP, mało Many (używana jako Stamina), średni atak
        super(name, 150, 30, 15, 10);
    }

    @Override
    public void specialAttack(Character target) {
        if (this.mana >= 10) {
            this.mana -= 10;
            int dmg = (int)(getTotalAttack() * 2.0); // Podwójne uderzenie
            System.out.println(name + " wykonuje POTĘŻNE CIĘCIE! (Koszt: 10 Energii)");
            target.takeDamage(dmg);
        } else {
            System.out.println(name + " jest zbyt zmęczony na specjala! (Brak energii)");
            // Zwykły atak 
            target.takeDamage(getTotalAttack()); 
        }
    }
}