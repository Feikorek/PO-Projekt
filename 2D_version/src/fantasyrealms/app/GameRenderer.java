package fantasyrealms.app;

import fantasyrealms.game.character.Character;
import fantasyrealms.app.GameState.State;

import java.awt.*;
import javax.swing.JPanel;

public class GameRenderer {
    
    private GameState state;

    public GameRenderer(GameState state) {
        this.state = state;
    }

    public void render(Graphics g, JPanel panel) {
        if (state.currentState == State.EXPLORING) {
            drawExploration(g);
        } else if (state.currentState == State.COMBAT) {
            drawCombat(g);
        } else if (state.currentState == State.GAME_OVER) {
            drawGameOver(g, panel);
        }
        drawSidebar(g, panel);
    }

    private void drawExploration(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, GameState.MAP_SIZE * GameState.TILE_SIZE, GameState.MAP_SIZE * GameState.TILE_SIZE);

        g.setColor(Color.GRAY);
        for (Point p : state.walls) {
            g.fillRect(p.x * GameState.TILE_SIZE, p.y * GameState.TILE_SIZE, GameState.TILE_SIZE, GameState.TILE_SIZE);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(p.x * GameState.TILE_SIZE, p.y * GameState.TILE_SIZE, GameState.TILE_SIZE, GameState.TILE_SIZE);
            g.setColor(Color.GRAY);
        }

        g.setColor(Color.PINK);
        g.fillRect(state.townLocation.x * GameState.TILE_SIZE, state.townLocation.y * GameState.TILE_SIZE, GameState.TILE_SIZE, GameState.TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawString("MIASTO", state.townLocation.x * GameState.TILE_SIZE + 2, state.townLocation.y * GameState.TILE_SIZE + 25);

        g.setColor(Color.RED);
        g.fillRect(state.bossLocation.x * GameState.TILE_SIZE, state.bossLocation.y * GameState.TILE_SIZE, GameState.TILE_SIZE, GameState.TILE_SIZE);

        g.setColor(Color.GREEN);
        for (MonsterEntity me : state.enemiesOnMap) {
            g.fillRect(me.location.x * GameState.TILE_SIZE, me.location.y * GameState.TILE_SIZE, GameState.TILE_SIZE, GameState.TILE_SIZE);
        }

        g.setColor(Color.CYAN);
        g.fillRoundRect(state.playerX * GameState.TILE_SIZE + 5, state.playerY * GameState.TILE_SIZE + 5, GameState.TILE_SIZE - 10, GameState.TILE_SIZE - 10, 15, 15);
    }

    private void drawCombat(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameState.SCREEN_WIDTH, GameState.SCREEN_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("WALKA!", 50, 50);

        if (state.currentEnemy != null) {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Przeciwnik: " + state.currentEnemy.getName(), 50, 90);
            g.drawString("HP: " + state.currentEnemy.getHp(), 50, 110);
        }

        g.drawString("1 - Atak", 50, 160);
        g.drawString("2 - Umiejętność", 50, 180);
        g.drawString("3 - Ucieczka", 50, 200);
    }

    private void drawGameOver(Graphics g, JPanel panel) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("KONIEC GRY", 100, 200);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Poległeś... Zrestartuj grę.", 120, 250);
    }

    private void drawSidebar(Graphics g, JPanel panel) {
        Character player = state.player;
        int x = GameState.MAP_SIZE * GameState.TILE_SIZE + 20;
        
        g.setColor(Color.DARK_GRAY);
        g.fillRect(GameState.MAP_SIZE * GameState.TILE_SIZE, 0, 300, panel.getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("STATYSTYKI", x, 30);
        
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Poziom: " + player.getLevel(), x, 60);
        g.drawString("XP: " + player.getXp(), x, 80);
        g.drawString("Złoto: " + player.getGold(), x, 100);
        g.drawString("Atak: " + player.getTotalAttack(), x, 120);
        g.drawString("Obrona: " + player.getTotalDefense(), x, 140);
        
        g.drawString("--- STEROWANIE ---", x, 200);
        g.drawString("Strzałki: Ruch", x, 220);
        g.drawString("E: Ekwipunek", x, 240);
        g.drawString("1/2/3: Walka", x, 260);
    }
}
