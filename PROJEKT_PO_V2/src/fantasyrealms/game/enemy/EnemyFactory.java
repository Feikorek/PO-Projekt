package fantasyrealms.game.enemy;

import java.util.Random;

public class EnemyFactory {
    public static Monster spawnForRegion(int dangerLevel) {
        Random r = new Random();
        int roll = r.nextInt(10); 

        switch (dangerLevel) {
            //  LAS 
            case 1: 
                if(roll==0) return new Monster("Wielki Szczur", 20, 5, 0, 0.2);
                if(roll==1) return new Monster("Dziki Wilk", 35, 8, 2, 0.3);
                if(roll==2) return new Monster("Goblin ", 30, 6, 1, 0.3);
                if(roll==3) return new Monster("Pająk Leśny", 25, 10, 0, 0.2);
                if(roll==4) return new Monster("Bandyta ", 45, 9, 3, 0.4);

                if(roll==5) return new Monster("Wściekły Dzik", 50, 8, 5, 0.3); 
                if(roll==6) return new Monster("Olbrzymia Ropucha", 35, 7, 1, 0.2);
                if(roll==7) return new Monster("Złodziejka", 40, 11, 1, 0.5); 
                if(roll==8) return new Monster("Rój Szerszeni", 15, 15, 0, 0.1); 
                return new Monster("Młody Niedźwiedź", 60, 10, 4, 0.4);

            // PÓŁNOCNA ZIMA
            case 2: 
                if(roll==0) return new Monster("Biały Wilk", 50, 12, 4, 0.3);
                if(roll==1) return new Monster("Lodowy Duch", 40, 15, 0, 0.4);
                if(roll==2) return new Monster("Niedźwiedź Polarny", 80, 10, 8, 0.5);
                if(roll==3) return new Monster("Wiking Wygnaniec", 70, 14, 6, 0.5);
                if(roll==4) return new Monster("Yeti", 100, 18, 5, 0.6);
                if(roll==5) return new Monster("Śnieżna Pantera", 60, 20, 2, 0.4); 
                if(roll==6) return new Monster("Mors Wojownik", 90, 12, 10, 0.3);
                if(roll==7) return new Monster("Zamarznięty Zombie", 55, 10, 2, 0.2);
                if(roll==8) return new Monster("Lodowy Golem", 110, 15, 15, 0.5); 
                return new Monster("Szaman Północy", 45, 25, 2, 0.7); 

            //PUSTYNIA
            case 3: 
                if(roll==0) return new Monster("Skorpion Gigant", 80, 20, 10, 0.4);
                if(roll==1) return new Monster("Pustynny Nomad", 90, 22, 5, 0.5);
                if(roll==2) return new Monster("Mumia", 120, 18, 2, 0.4);
                if(roll==3) return new Monster("Sęp Ścierwojad", 60, 25, 2, 0.3);
                if(roll==4) return new Monster("Czerwona Mrówka", 70, 20, 15, 0.3);

                if(roll==5) return new Monster("Piaskowy Czerw", 150, 15, 5, 0.5); 
                if(roll==6) return new Monster("Grzechotnik", 50, 30, 0, 0.4); 
                if(roll==7) return new Monster("Hiena Cmentarna", 80, 18, 4, 0.3);
                if(roll==8) return new Monster("Dżinn Pustynny", 100, 25, 20, 0.8); 
                return new Monster("Strażnik Grobowca", 130, 20, 25, 0.6);

            // OAZA 
            case 4: 
                if(roll==0) return new Monster("Wąż Wodny", 110, 30, 5, 0.5);
                if(roll==1) return new Monster("Dzikus z Dżungli", 130, 35, 10, 0.6);
                if(roll==2) return new Monster("Trujący Kwiat", 150, 25, 20, 0.5);
                if(roll==3) return new Monster("Krokodyl", 200, 40, 30, 0.7);
                if(roll==4) return new Monster("Syrena Lądowa", 120, 45, 5, 0.8);

                if(roll==5) return new Monster("Wielka Modliszka", 140, 35, 10, 0.5);
                if(roll==6) return new Monster("Czarna Pantera", 100, 50, 5, 0.6); 
                if(roll==7) return new Monster("Szaman Voodoo", 90, 40, 0, 0.7);
                if(roll==8) return new Monster("Bazyliszek", 180, 30, 25, 0.8);
                return new Monster("Bagienny Szlam", 250, 15, 40, 0.4); 

            //POZIOM 5: JASKINIA 
            case 5: 
                if(roll==0) return new Monster("Nietoperz Wampir", 150, 40, 10, 0.6);
                if(roll==1) return new Monster("Skalny Golem", 300, 30, 50, 0.7);
                if(roll==2) return new Monster("Troll Jaskiniowy", 400, 50, 20, 0.8);
                if(roll==3) return new Monster("Mroczny Elf", 200, 60, 15, 0.9);
                if(roll==4) return new Monster("Cień", 100, 80, 0, 0.5); 
                if(roll==5) return new Monster("Minotaur", 450, 55, 30, 0.8);
                if(roll==6) return new Monster("Beholder (Obserwator)", 250, 70, 10, 0.9);
                if(roll==7) return new Monster("Szkieletor Wojownik", 180, 45, 5, 0.4);
                if(roll==8) return new Monster("Cerber", 350, 60, 20, 0.8);
                // BOSS GŁÓWNY 
                return new Monster("SMOK PODZIEMI (Boss)", 1200, 110, 60, 1.0);

            default: 
                return new Monster("Blob", 10, 1, 0, 0);
        }


    }
}