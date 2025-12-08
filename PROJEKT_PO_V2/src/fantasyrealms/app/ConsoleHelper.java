package fantasyrealms.app;

public class ConsoleHelper {
    public static final String RESET = "\033[0m";
    
    // Kolory podstawowe
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    
    // Pogrubione (Bold) - dla ważnych informacji
    public static final String RED_BOLD = "\033[1;31m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String WHITE_BOLD = "\033[1;37m";

    // Czyszczenie ekranu
    public static void clear() {
        for(int i=0; i<50; i++) System.out.println();
    }
}