package fantasyrealms.app;

import fantasyrealms.game.enemy.Monster;
import java.awt.Point;

public class MonsterEntity {
    public Monster monster;
    public Point location;

    public MonsterEntity(Monster m, Point p) {
        this.monster = m;
        this.location = p;
    }
}
