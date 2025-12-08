package fantasyrealms.world.faction;
import fantasyrealms.game.character.Character;

public class DarkBrotherhood extends Faction {
    public DarkBrotherhood() {
        super("Mroczne Bractwo");
    }

    @Override
    public double getPriceMod(Character p) {
        int goodRep = p.getReputation("OrderOfLight");
        if (goodRep > 20) return 2.5; 
        return 0.8; 
    }
}