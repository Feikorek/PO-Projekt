package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import java.awt.Point;
import java.util.List;

public class GameState {

    public static final int TILE_SIZE = 48;
    public static final int MAP_SIZE = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAP_SIZE + 300;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAP_SIZE;

    public enum State { EXPLORING, GAME_OVER }

    // Zmienne stanu gry
    public State currentState = State.EXPLORING;
    public Character player;
    public int playerX = 1;
    public int playerY = 1;
    public List<Point> walls; // Lista ścian
    public Point townLocation = new Point(1, 1);
    public String gameLog = "Rozpocznij przygodę!";

    public GameState(Character initialPlayer, List<Point> initialWalls) {
        this.player = initialPlayer;
        this.walls = initialWalls;
    }
}