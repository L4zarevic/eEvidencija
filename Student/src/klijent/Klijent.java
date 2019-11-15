package klijent;

import adresa.MreznaAdresa;
import aktivnost.Aktivnost;
import gui.Kontroler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import aktivnost.KeyLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import main.Logger;

public class Klijent implements Runnable {
    
    private SSLSocket socket;
    private ObjectOutputStream izlaz;
    private ObjectInputStream ulaz;

    public Klijent() {
        String ipAdresa = "";
        try {
            File fajl = new File("adresa");
            BufferedReader citac = new BufferedReader(new FileReader(fajl));
            ipAdresa = citac.readLine();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            socket = kreirajSSLSocket(ipAdresa, 25000);
            izlaz  = new ObjectOutputStream(socket.getOutputStream());
            ulaz   = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException ex) {
            Kontroler.kreirajAlert(Alert.AlertType.ERROR, "Greška!", "Predavanje nije u toku!");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop() {
        try {
            socket.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String indeks;

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }
    
    private SSLSocket kreirajSSLSocket(String ipAdresa, int port) throws IOException {
        System.setProperty("javax.net.ssl.trustStore", "cert");
        System.setProperty("javax.net.ssl.trustStorePassword", "3010997");
        SSLSocketFactory ssf = (SSLSocketFactory)SSLSocketFactory.getDefault();
        return (SSLSocket)ssf.createSocket(ipAdresa, port);
    }
    
    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        
        if (!provjeriIndeks()) {
            Platform.runLater(() -> {
                Kontroler.kreirajAlert(
                        Alert.AlertType.ERROR,
                        "Greška!",
                        "Pogrešan broj indeksa!"
                );
            });
        }
        
        try {
            izlaz.writeObject(indeks);
            izlaz.writeObject(MreznaAdresa.getIpv4());
            izlaz.flush();
            indeks = "";

            boolean odgovor = ulaz.readBoolean();
            if (!odgovor) {
                Platform.runLater(() -> {
                    Kontroler.kreirajAlert(
                            Alert.AlertType.ERROR,
                            "Greška!",
                            "Pogrešan broj indeksa!"
                    );
                });
                return;
            }
            
            Platform.runLater(() -> {
                Kontroler.kreirajAlert(
                        Alert.AlertType.INFORMATION,
                        "Dobrodošli",
                        "Uspješno ste se ulogovali!"
                );
                Kontroler.getStage().hide();
            });
            
            KeyLogger.pokreniKeyLogger();

            boolean posaljiAktivnost = ulaz.readBoolean();
            if (posaljiAktivnost) {
                izlaz.writeInt(Aktivnost.getAktivnost());
                izlaz.flush();
            }
        } catch (IOException ex) {
            Logger.log(ex.toString());
            for (StackTraceElement e : ex.getStackTrace()) {
                Logger.log("\t" + e.toString());
            }
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        stop();
        Platform.runLater(() -> {Kontroler.getStage().show();});
    }
    
    private boolean provjeriIndeks() {
        if (indeks.length() != 11)
            return false;
        
        if (indeks.charAt(4) != '/')
            return false;
        
        for (int i = 0; i < indeks.length(); i++) {
            if (!Character.isDigit(indeks.charAt(i)) && i != 4)
                return false;
        }
        
        return true;
    }
    
}
