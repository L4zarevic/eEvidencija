package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class Logger {
    
    private static File fajl;
    private static FileWriter output;
    
    private static boolean initialized = false;
    
    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        
        fajl = new File("Log.log");
        if (!fajl.exists()) {
            try {
                fajl.createNewFile();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            output = new FileWriter(fajl);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void log(String msg) {
        try {
            output.append(msg + "\r\n");
            output.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close() {
        if (!initialized) {
            return;
        }
        initialized = false;
        
        if (output != null) {
            try {
                output.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
