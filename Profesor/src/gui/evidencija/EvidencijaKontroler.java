package gui.evidencija;

import DB.DBManager;
import gui.Kontroler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class EvidencijaKontroler extends EvidencijaGUI {

    public EvidencijaKontroler(String predmet, boolean isVjezba) {
        super(predmet, isVjezba);
        
        popuniTabelu(predmet, isVjezba);
        
        print.setOnAction((event) -> sacuvajStudenteUFajl(new ArrayList<>(tabela.getItems())));
    }
    
    private void popuniTabelu(String predmet, boolean isVjezba) {
        DBManager.Konektuj();
        
        if (isVjezba)
            predmet = predmet.toLowerCase().replace(" ", "_") + "_v";
        else
            predmet = predmet.toLowerCase().replace(" ", "_") + "_p";
        
        ObservableList<EvidencijaPodaci> podaci =
                FXCollections.observableArrayList(DBManager.selectPrisustvo(predmet, isVjezba));
        
        tabela.setItems(podaci);
        
        DBManager.Diskonektuj();
    }

    private void sacuvajStudenteUFajl(ArrayList<EvidencijaPodaci> studenti) {
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
                Logger.getLogger(EvidencijaKontroler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (FileWriter fw = new FileWriter(fajl)) {
            
            String podaci = "";
            for (EvidencijaPodaci student : studenti) {
                podaci += student.getImePrezime() + "," +
                          student.getIndeks()     + "," +
                          student.getFakultet()   + "," +
                          student.getPrisustvo()  + "\n";
            }

            fw.write(podaci);
        } catch (IOException ex) {
            Logger.getLogger(EvidencijaKontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
