package fantasyrealms.game.quest;

import fantasyrealms.game.character.Character;
import java.io.Serializable;

public class KillQuest implements Quest, Serializable {
    private static final long serialVersionUID = 1L;
    private String name, target;
    private int needed, current, reward;
    private boolean claimed = false;

    public KillQuest(String name, String target, int needed, int reward) {
        this.name = name;
        this.target = target;
        this.needed = needed;
        this.reward = reward;
    }

    @Override
    public String getName() { 
        return name; 
    }

    @Override
    public String getDescription() { 
        return "Zabij " + target + " (" + current + "/" + needed + ")"; 
    }

    @Override
    public boolean isCompleted() { 
        return current >= needed; 
    }
    
    @Override
    public void update(String enemyName) {
        if (!isCompleted() && enemyName.contains(target)) {
            current++;
            System.out.println("[QUEST] Postęp: " + current + "/" + needed);
        }
    }
    
    @Override
    public void claimReward(Character p) {
        if(isCompleted() && !claimed) {
            p.addGold(reward);
            claimed = true;
            System.out.println("Zadanie ukończone! Nagroda: " + reward);
        }
    }
}