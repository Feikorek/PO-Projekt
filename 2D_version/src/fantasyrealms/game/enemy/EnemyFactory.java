package fantasyrealms.game.enemy;

import java.util.Random;

public class EnemyFactory {
    
    public static Monster spawnForRegion(int dangerLevel) {
        Random r = new Random();
        int roll = r.nextInt(10); 

        switch (dangerLevel) {
            case 1: 
                if(roll==0) return new Monster("Wielki Szczur", 20, 5, 0, 0.2);
                if(roll==1) return new Monster("Dziki Wilk", 35, 8, 2, 0.3);
                return new Monster("Goblin", 30, 6, 1, 0.3);
            case 2: 
                if(roll==0) return new Monster("Biały Wilk", 50, 12, 4, 0.3);
                return new Monster("Yeti", 100, 18, 5, 0.6);
            case 3: 
                if(roll==0) return new Monster("Skorpion", 80, 20, 10, 0.4);
                return new Monster("Mumia", 120, 18, 2, 0.4);
            case 4: 
                return new Monster("Bazyliszek", 180, 30, 25, 0.8);
            case 5: 
                return new Monster("Smok", 500, 50, 20, 1.0);
            default: 
                // Generowanie proceduralne dla wyższych poziomów
                return new Monster("Cień Poziomu " + dangerLevel, dangerLevel * 30, dangerLevel * 8, dangerLevel * 2, 0.5);
        }
    }

    // Ta metoda musi być POZA poprzednią metodą, ale WEWNĄTRZ klasy
    public static Boss spawnBoss(int level) {
        switch(level) {
            case 1: return new Boss("Król Goblinów", 250, 20, 5, "Przyzwanie Goblinów");
            case 2: return new Boss("Lodowy Gigant", 400, 35, 15, "Zamieć");
            case 3: return new Boss("Faraon", 600, 50, 20, "Piaskowa Burza");
            case 4: return new Boss("Hydra", 800, 65, 25, "Trujący Wyziew");
            case 5: return new Boss("STAROŻYTNY SMOK", 1500, 100, 50, "Piekielny Ogień");
            default: return new Boss("Mroczny Lord", level * 200, level * 20, level * 10, "Zniszczenie");
        }
    }
}