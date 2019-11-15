package server;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import main.Logger;

public class Server implements Runnable {

    private SSLServerSocket server;
    private boolean pokrenuto;
    
    public ArrayList<Klijent> klijenti = new ArrayList<>();
    
    public Server() {
        server = null;
        try {
            server    = kreirajSSLServerSocket(25000);
            pokrenuto = false;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start() {
        if (!pokrenuto) {
            pokrenuto = true;
            new Thread(this).start();
        }
    }
    
    public void stop() {
        try {
            pokrenuto = false;
            server.close();
            
            klijenti.forEach((klijent) -> {
                synchronized (klijent) {
                    klijent.notifyAll();
                }
            });
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pauziraj() {
        try {
            pokrenuto = false;
            server.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void nastavi() {
        try {
            pokrenuto = true;
            server = kreirajSSLServerSocket(25000);
            new Thread(this).start();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private SSLServerSocket kreirajSSLServerSocket(int port) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", "cert");
        System.setProperty("javax.net.ssl.keyStorePassword", "3010997");
        SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        return (SSLServerSocket)ssf.createServerSocket(port);
    }

    @Override
    public void run() {
        while (pokrenuto) {
            try {
                Klijent klijent = new Klijent((SSLSocket)server.accept());
                if (klijent.getSocket() != null)
                    new Thread(klijent).start();
                klijenti.add(klijent);
            } catch (SocketException ex) {
                // Ovaj exception je normalan kada se ServerSocket zatvori, a server ceka na
                // klijenta da se uloguje
                java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.log(ex.toString());
                for (StackTraceElement e : ex.getStackTrace()) {
                    Logger.log("\t" + e.toString());
                }
                java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
