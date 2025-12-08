package fantasyrealms.game.enemy;

import fantasyrealms.game.character.Character;
import fantasyrealms.service.ItemDatabase;
import fantasyrealms.app.ConsoleHelper; // kolory
import fantasyrealms.game.item.Item;

public class Monster extends Character {
    private static final long serialVersionUID = 1L; 
    private double dropChance;

    public Monster(String name, int hp, int atk, int def, double dropChance) {
        super(name, hp, 20, atk, def);
        this.dropChance = dropChance;
    }

    @Override
    public void specialAttack(Character target) {
        // Kolor Czerwony dla ataku specjalnego potwora
        System.out.println(ConsoleHelper.RED + name + " wpada w szał! (Krytyczny atak)" + ConsoleHelper.RESET);
        target.takeDamage((int)(getTotalAttack() * 1.5));
    }

    public Item tryDropLoot() {
        if (Math.random() < dropChance) {
            Item item = ItemDatabase.getRandomDrop();
            return item;
        }
        return null;
    }
}