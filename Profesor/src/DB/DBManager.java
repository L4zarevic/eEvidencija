package DB;

import gui.Kontroler;
import gui.evidencija.EvidencijaPodaci;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import student.Student;

public class DBManager {
    
    private static Connection konekcija;
    
    public static void Konektuj() {
        try {
            String s = "jdbc:mysql://192.168.10.16:3306/prisustvo";
            Class.forName("com.mysql.jdbc.Driver");
            konekcija = DriverManager.getConnection(s, "profesor", "123profesor456");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (konekcija == null) {
            Kontroler.prikaziObavjestenje(
                "Greška",
                "Greška",
                "Nije moguće ostvariti konekciju sa bazom podataka",
                Alert.AlertType.ERROR
            );
            System.exit(1);
        }
    }
    
    public static void Diskonektuj() {
        try {
            if (konekcija != null) {
                konekcija.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean ping() {
        try (PreparedStatement stmt = konekcija.prepareStatement("SELECT * FROM studenti");
             ResultSet rezultat = stmt.executeQuery()) {
            
            return rezultat.next();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static Map<String, Student> selectStudenti() {
        Map<String, Student> studenti = new HashMap<>();
        String upit = "SELECT indeks, ime_prezime, fakultet FROM studenti";
        
        try (PreparedStatement stmt = konekcija.prepareStatement(upit);
             ResultSet rezultat = stmt.executeQuery()) {
            
            while (rezultat.next()) {
                String indeks = rezultat.getString(1);
                
                Student temp = new Student(
                    indeks,
                    rezultat.getString(2), // Ime i prezime
                    rezultat.getString(3)  // Fakultet
                );
                studenti.put(indeks, temp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studenti;
    }
    
    public static void zabiljeziPrisustvo(String indeks, String predmet) {
        String upit = "UPDATE studenti SET " + predmet + " = " + predmet + " + ? WHERE indeks = ?";
        
        try (PreparedStatement stmt = konekcija.prepareStatement(upit)) {
            
            stmt.setInt(1, 1); // Prisustvo uvecaj za 1 bod
            stmt.setString(2, indeks);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ArrayList<EvidencijaPodaci> selectPrisustvo(String predmet, boolean vjezba) {
        ArrayList<EvidencijaPodaci> podaci = new ArrayList<>();
        String upit = "SELECT indeks, ime_prezime, fakultet, " + predmet + " FROM studenti";
        
        try (PreparedStatement stmt = konekcija.prepareStatement(upit);
             ResultSet rezultat = stmt.executeQuery()) {
            
            while (rezultat.next()) {
                EvidencijaPodaci temp = new EvidencijaPodaci();
                temp.setIndeks(rezultat.getString(1));
                temp.setImePrezime(rezultat.getString(2));
                temp.setFakultet(rezultat.getString(3));
                temp.setPrisustvo(rezultat.getInt(4));
                temp.setPredmet(predmet);
                
                if (temp.getPrisustvo() <= 0)
                    continue;
                
                podaci.add(temp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return podaci;
    }
    
    public static ArrayList<String> selectPredmeti() {
        ArrayList<String> predmeti = new ArrayList<>();
        String upit = "SELECT predmet FROM predmeti";

        try (PreparedStatement stmt = konekcija.prepareStatement(upit);
             ResultSet rezultat = stmt.executeQuery()) {

            while (rezultat.next()) {
                predmeti.add(rezultat.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return predmeti;
    }
    
    public static boolean dodajNoviPredmet(String predmet) {
        try {
            // Novi red u tabeli predmet
            String upit = "INSERT INTO predmeti (predmet) VALUES (?)";
            PreparedStatement stmt = konekcija.prepareStatement(upit);
            
            stmt.setString(1, predmet);
            stmt.executeUpdate();
            // -------------------------
            
            String imeKolone = predmet.toLowerCase().replace(" ", "_") + "_p"; // Kolona predmet
            upit = "ALTER TABLE studenti\n" +
                   "ADD COLUMN " + imeKolone + " INT(2) NOT NULL DEFAULT 0";
            
            // Predmet
            stmt = konekcija.prepareStatement(upit);
            stmt.executeUpdate();
            // -------
            
            // Vjezbe
            imeKolone = predmet.toLowerCase().replace(" ", "_") + "_v"; // Kolona vjezbe
            upit = "ALTER TABLE studenti\n" +
                   "ADD COLUMN " + imeKolone + " INT(2) NOT NULL DEFAULT 0";
            
            stmt = konekcija.prepareStatement(upit);
            stmt.executeUpdate();
            // ------
            
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean dodajNovogStudenta(String indeks, String imePrezime, String fakultet) {
        boolean rezultat = false;
        String upit = "INSERT INTO studenti (indeks, ime_prezime, fakultet) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = konekcija.prepareStatement(upit)) {
            
            stmt.setString(1, indeks);
            stmt.setString(2, imePrezime);
            stmt.setString(3, fakultet);
            stmt.executeUpdate();
            rezultat = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            rezultat = false;
        }
        
        if (fakultet.equals("Računarstvo i informatika")) {
            upit = "INSERT INTO aktivnost (indeks, ime_prezime) VALUES (?, ?)";
            
            try (PreparedStatement stmt = konekcija.prepareStatement(upit)) {
                
                stmt.setString(1, indeks);
                stmt.setString(2, imePrezime);
                stmt.executeUpdate();
                rezultat = true;
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                rezultat = false;
            }
        }
        return rezultat;
    }
    
    public static void zabiljeziAktivnost(String indeks, String predmet) {
        String upit = "UPDATE aktivnost SET " + predmet + " = " + predmet + " + 1 WHERE indeks = ?";
        
        try (PreparedStatement stmt = konekcija.prepareStatement(upit)) {
            
            stmt.setString(1, indeks);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> selectIpAdrese() {
        List<String> ipAdrese = new ArrayList<>();
        
        try (PreparedStatement stmt = konekcija.prepareStatement("SELECT * FROM ip_adrese");
             ResultSet rezultat = stmt.executeQuery()) {
            
            while (rezultat.next()) {
                ipAdrese.add(rezultat.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ipAdrese;
    }

    public static boolean provjeriIpAdresu(String ipAdresa) {
        String upit = "SELECT * FROM ip_adrese WHERE ip_adresa = ?";
        
        try (PreparedStatement stmt = konekcija.prepareStatement(upit)) {
            
            stmt.setString(1, ipAdresa);
            
            ResultSet rezultat = stmt.executeQuery();
            return rezultat.next();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
