package aktivnost;

import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;

public class KeyLogger {
    
    private static boolean shift = false;
    private static boolean altgr = false;
    
    public static final int SHIFT_1 = 160;
    public static final int SHIFT_2 = 161;
    public static final int ALT_GR  = 165;

    public static void pokreniKeyLogger() {

        new GlobalKeyListener().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                int kod = event.getVirtualKeyCode();
                
                switch (kod) {
                    case SHIFT_1:
                    case SHIFT_2:
                        shift = true;
                        break;
                    case ALT_GR:
                        altgr = true;
                        break;
                }
                
                char karakter = toChar(kod);
                Aktivnost.incAktivnost(karakter);
            }

            @Override
            public void keyReleased(KeyEvent event) {
                switch (event.getVirtualKeyCode()) {
                    case SHIFT_1:
                    case SHIFT_2:
                        shift = false;
                        break;
                    case ALT_GR:
                        altgr = false;
                        break;
                }
            }
            
        });
    }

    private static char toChar(int code) {
        // Engleska tastatura
        switch (code) {
            case 48:
                if (shift)
                    return ')';
                break;
            case 57:
                if (shift)
                    return '(';
                break;
            case 186:
                if (!shift)
                    return ';';
                break;
            case 219:
                if (shift)
                    return '{';
                return '[';
            case 221:
                if (shift)
                    return '}';
                return ']';

            // Srpska tastatura
            case 66:
                if (altgr)
                    return '{';
                break;
            case 70:
                if (altgr)
                    return '[';
                break;
            case 71:
                if (altgr)
                    return ']';
                break;
            case 78:
                if (altgr)
                    return '}';
                break;
            case 188:
                if (shift)
                    return ';';
                break;
            default:
                break;
        }

        return ' ';
    }

}
