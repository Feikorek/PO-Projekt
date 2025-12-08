package fantasyrealms.service;

import fantasyrealms.game.quest.Quest;
import fantasyrealms.game.character.Character;
import java.util.ArrayList;
import java.util.List;

public class QuestService {
    private List<Quest> activeQuests = new ArrayList<>();

    public void addQuest(Quest q) {
        activeQuests.add(q);
        System.out.println("[QUEST] Nowe zadanie: " + q.getName());
    }

    public void updateQuests(String eventData) {
        for (Quest q : activeQuests) {
            q.update(eventData);
        }
    }

    public void showLog() {
        System.out.println("\n--- DZIENNIK ZADAŃ ---");
        if(activeQuests.isEmpty()) System.out.println("(Pusto)");
        for(Quest q : activeQuests) {
            System.out.println((q.isCompleted() ? "[GOTOWE] " : "[W TOKU] ") + q.getName() + ": " + q.getDescription());
        }
    }

    public void claimRewards(Character p) {
        List<Quest> toRemove = new ArrayList<>();
        for(Quest q : activeQuests) {
            if(q.isCompleted()) {
                q.claimReward(p);
                toRemove.add(q);
            }
        }
        activeQuests.removeAll(toRemove);
    }
}