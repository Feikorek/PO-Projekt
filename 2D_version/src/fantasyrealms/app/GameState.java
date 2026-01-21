package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.game.enemy.Monster;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// Łączy dawne GameState.java oraz pomocniczą klasę MonsterEntity
public class GameState {
    public static final int TILE_SIZE = 48;
    public static final int MAP_SIZE = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAP_SIZE + 300;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAP_SIZE;

    public enum State { EXPLORING, COMBAT, GAME_OVER }

    public State currentState = State.EXPLORING;
    public Character player;
    public int playerX = 1;
    public int playerY = 1;

    public boolean inTown = false;

    public List<Point> walls;
    public Point townLocation = new Point(1, 1);
    public List<MonsterEntity> enemiesOnMap = new ArrayList<>();
    public Point bossLocation = new Point(10, 10);
    public Monster currentEnemy = null;

    public GameState(Character initialPlayer, List<Point> initialWalls) {
        this.player = initialPlayer;
        this.walls = initialWalls;
    }

    // Wewnętrzna klasa pomocnicza (dawny MonsterEntity.java)
    public static class MonsterEntity {
        public Monster monster;
        public Point location;
        public MonsterEntity(Monster m, Point p) {
            this.monster = m;
            this.location = p;
        }
    }
}