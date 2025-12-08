package fantasyrealms.world.faction;
import fantasyrealms.game.character.Character;

public abstract class Faction {
    protected String name;
    public Faction(String name) { this.name = name; }
    
    public double getPriceMod(Character p) {
        int rep = p.getReputation(name);
        if(rep > 50) return 0.5; // Bardzo tanio dla lubianych xd
        if(rep > 10) return 0.8; // Zniżka
        if(rep < 0) return 1.5;  // Drożej dla wrogów
        return 1.0;
    }
}