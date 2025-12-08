package fantasyrealms.game.enemy;

import fantasyrealms.app.ConsoleHelper;
import fantasyrealms.game.character.Character;

public class Boss extends Monster {
    private String specialSkillName;

    public Boss(String name, int hp, int atk, int def, String skillName) {
        super(name, hp, atk, def, 1.0); // 100% szansy na drop
        this.specialSkillName = skillName;
    }

    @Override
    public void specialAttack(Character target) {
        System.out.println(ConsoleHelper.RED_BOLD + "\n⚔️ " + name + " używa ULTIMATE: " + specialSkillName + "! ⚔️" + ConsoleHelper.RESET);
        
        if (specialSkillName.equals("Przyzwanie Goblinów")) {
            System.out.println("Boss przyzywa sługi, które cię gryzą!");
            target.takeDamage(15);
        } 
        else if (specialSkillName.equals("Zamieć")) {
            System.out.println("Zimno przenika twoje kości!");
            target.takeDamage(25);
        }
        else if (specialSkillName.equals("Piaskowa Burza")) {
            System.out.println("Piasek cię oślepia!");
            target.takeDamage(35);
        }
        else if (specialSkillName.equals("Trujący Wyziew")) {
            System.out.println("Dławisz się toksynami!");
            target.takeDamage(40);
        }
        else if (specialSkillName.equals("Piekielny Ogień")) {
            System.out.println("Smok zieje ogniem! TO BOLI!");
            target.takeDamage(60);
        }
        else {
            target.takeDamage(getTotalAttack() * 2);
        }
    }
}