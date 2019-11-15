package student;

import DB.DBManager;
import gui.Kontroler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class UlogovaniStudenti {
    
    private static Map<String, Student> STUDENTI;
    private static ObservableList<Student> ULOGOVANI_STUDENTI;
    
    private static List<String> IP_ADRESE;
    
    public static void init() {
        STUDENTI           = DBManager.selectStudenti();
        ULOGOVANI_STUDENTI = FXCollections.observableArrayList();
        
        IP_ADRESE = DBManager.selectIpAdrese();
    }
    
    public static boolean dodajStudentaUTabelu(String indeks, String ipAdresa) {
        Student student = STUDENTI.get(indeks);
        if (student == null) {
            return false;
        }
        
        if (student.getImePrezime() == null) {
            return false;
        }
        
        DBManager.Konektuj();
        if (!DBManager.provjeriIpAdresu(ipAdresa)) {
            return false;
        }
        DBManager.Diskonektuj();
        
        student.setIpAdresa(ipAdresa);
        
        for (Student temp : ULOGOVANI_STUDENTI) {
            if (temp.getIndeks().equals(student.getIndeks())) {
                return false;
            }
            
            if (temp.getIpAdresa().equals(student.getIpAdresa())) {
                return false;
            }
        }
        
        ULOGOVANI_STUDENTI.add(student);
        return true;
    }
    
    public static void dodajAktivnostStudentu(String indeks, int aktivnost) {
        ULOGOVANI_STUDENTI.forEach((student) -> {
            if (student.getIndeks().equals(indeks)) {
                student.setAktivnost(aktivnost);
            }
        });
    }
    
    public static void zabiljeziPrisustvo(String predmet, boolean isVjezba, boolean aktivnost) {
        if (!DBManager.ping()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Konekcija sa bazom je nepostojeća, da li želite da sačuvate prisustvo u .csv fajlu?");
            
            Optional<ButtonType> rezultat = alert.showAndWait();
            if (rezultat.get() == ButtonType.OK) {
                sacuvajStudenteUFajl();
            }
            return;
        }
        
        if (isVjezba)
            predmet = predmet.toLowerCase().replace(" ", "_") + "_v";
        else
            predmet = predmet.toLowerCase().replace(" ", "_") + "_p";
        
        final String temp = predmet;
        ULOGOVANI_STUDENTI.forEach((student) -> {
            DBManager.zabiljeziPrisustvo(student.getIndeks(), temp);
            
            if (aktivnost) {
                DBManager.zabiljeziAktivnost(student.getIndeks(), temp.substring(0, temp.length() - 2));
            }
        });
    }

    public static ObservableList<Student> getUlogovaniStudenti() {
        return ULOGOVANI_STUDENTI;
    }
    
    public static Map<String, Student> getStudenti() {
        return STUDENTI;
    }
    
    public static void sacuvajStudenteUFajl() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sačuvaj kao .csv");
        
        FileChooser.ExtensionFilter ektenzije =
                new FileChooser.ExtensionFilter("Sačuvaj kao .csv", ".csv");

        fileChooser.getExtensionFilters().add(ektenzije);
        
        File fajl = fileChooser.showSaveDialog(Kontroler.getStage());
        if (fajl == null) {
            return;
        }
        
        if (!fajl.exists()) {
            try {
                fajl.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(UlogovaniStudenti.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (FileWriter fw = new FileWriter(fajl)) {
            
            String studenti = "";
            for (Student student : ULOGOVANI_STUDENTI) {
                studenti += student.getImePrezime() + "," +
                            student.getIndeks() + "," +
                            student.getFakultet() + "\n";
            }

            fw.write(studenti);
        } catch (IOException ex) {
            Logger.getLogger(UlogovaniStudenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
}
