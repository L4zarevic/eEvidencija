package aktivnost;

public class Aktivnost {
    
    private static int tackaZarez     = 0;
    private static int malaZagrada    = 0;
    private static int srednjaZagrada = 0;
    private static int velikaZagrada  = 0;
    
    private static void incTackaZarez()     { ++tackaZarez;     }
    private static void incMalaZagrada()    { ++malaZagrada;    }
    private static void incSrednjaZagrada() { ++srednjaZagrada; }
    private static void incVelikaZagrada()  { ++velikaZagrada;  }
    
    public static void incAktivnost(char karakter) {
        switch (karakter) {
            case ';':
                incTackaZarez();
                break;
                
            case '(':
            case ')':
                incMalaZagrada();
                break;
                
            case '[':
            case ']':
                incSrednjaZagrada();
                break;
                
            case '{':
            case '}':
                incVelikaZagrada();
                break;
        }
    }
    
    public static int getAktivnost() {
        return tackaZarez + malaZagrada + srednjaZagrada + velikaZagrada;
    }
    
}
