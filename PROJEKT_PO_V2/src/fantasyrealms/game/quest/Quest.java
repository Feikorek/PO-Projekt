package fantasyrealms.game.quest;
import fantasyrealms.game.character.Character;
import java.io.Serializable;

public interface Quest extends Serializable {
    String getName();
    String getDescription();
    boolean isCompleted();
    void update(String data);
    void claimReward(Character p);
}