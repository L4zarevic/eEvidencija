package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;
import main.Logger;
import student.UlogovaniStudenti;

public class Klijent implements Runnable {
    
    private final SSLSocket socket;
    private ObjectOutputStream izlaz;
    private ObjectInputStream ulaz;

    public Klijent(SSLSocket socket) {
        this.socket = socket;
        
        try {
            izlaz = new ObjectOutputStream(getSocket().getOutputStream());
            ulaz  = new ObjectInputStream(getSocket().getInputStream());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final SSLSocket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        try {
            String indeks = (String) ulaz.readObject();
            String ipAdresa = (String) ulaz.readObject();
            
            if (UlogovaniStudenti.dodajStudentaUTabelu(indeks, ipAdresa)) {
                izlaz.writeBoolean(true);
                izlaz.flush();
            } else {
                izlaz.writeBoolean(false);
                izlaz.flush();
                return;
            }

            synchronized (this) {
                this.wait();
            }

            // Trazi aktivnost
            izlaz.writeBoolean(true);
            izlaz.flush();
            
            int aktivnost = ulaz.readInt();
            UlogovaniStudenti.dodajAktivnostStudentu(indeks, aktivnost);
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            Logger.log(ex.toString());
            for (StackTraceElement e : ex.getStackTrace()) {
                Logger.log("\t" + e.toString());
            }
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        stop();
    }
    
    private void stop() {
        try {
            socket.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
