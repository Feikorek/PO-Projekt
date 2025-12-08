package fantasyrealms.game.character;

public class Wizard extends Character {
    public Wizard(String name) {
        //  Mało HP, dużo Many, wysoki atak magiczny
        super(name, 80, 100, 25, 5);
    }

    @Override
    public void specialAttack(Character target) {
        if (this.mana >= 20) {
            this.mana -= 20;
            // Mag zadaje obrażenia magiczne, mnożnik 2.5x
            int dmg = (int)(getTotalAttack() * 2.5); 
            System.out.println(name + " rzuca KULĘ OGNIA! (Koszt: 20 Many)");
            
            target.takeDamage(dmg);
        } else {
            System.out.println(name + " nie ma many! Uderza kosturem.");
            target.takeDamage(getTotalAttack() / 2); // Słaby atak fizyczny
        }
    }
}