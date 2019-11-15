package gui.profesor;

import DB.DBManager;
import gui.InfoGUI;
import gui.Kontroler;
import gui.evidencija.EvidencijaKontroler;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import server.Server;
import student.Student;
import student.UlogovaniStudenti;

public class ProfesorKontroler extends ProfesorGUI {
    
    private Server server;
    
    private boolean aktivnost = false;
    
    private final Label predavanjeUToku = new Label("PREDAVANJE U TOKU!");

    public ProfesorKontroler() {
        super();
        
        tabela.setItems(UlogovaniStudenti.getUlogovaniStudenti());
        izborPredavanja.setItems(predmeti);
        izborPredavanja.getSelectionModel().select(0);
        
        HandleButtonPocetakRada();
        HandleButtonKrajRada();
        HandleButtonPauza();
        HandleButtonNastavi();
        
        evidentiraniStudenti.setOnAction((event) -> new EvidencijaKontroler(getPredmet(), isVjezba()).prikazi());
        HandleOpcijuDodajPredmet();
        HandleOpcijuDodajStudenta();
	info.setOnAction((event) -> new InfoGUI().prikazi());
        HandleButtonObrisiStudenta();
    }
    
    private void HandleButtonPocetakRada() {
        pocetakRada.setOnAction((event) -> {
            server = new Server();
            server.start();
            
            aktivnost = provjeriAktivnost();
            
            lijevi.getChildren().remove(pocetakRada);
            lijevi.getChildren().addAll(krajRada, pauza, predavanjeUToku);
            
            desniKontejner.getChildren().add(tabelaKontroler);
        });
    }
    
    private void HandleButtonKrajRada() {
        krajRada.setOnAction((event) -> {
            server.stop();
            
            lijevi.getChildren().remove(krajRada);
            lijevi.getChildren().remove(pauza);
            lijevi.getChildren().remove(predavanjeUToku);
            lijevi.getChildren().add(pocetakRada);
            
            desniKontejner.getChildren().remove(tabelaKontroler);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProfesorKontroler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            DBManager.Konektuj();
            UlogovaniStudenti.zabiljeziPrisustvo(getPredmet(), isVjezba(), aktivnost);
            UlogovaniStudenti.getUlogovaniStudenti().clear();
            DBManager.Diskonektuj();
        });
    }
    
    private void HandleButtonPauza() {
        pauza.setOnAction((event) -> {
            server.pauziraj();
            
            lijevi.getChildren().remove(pauza);
            lijevi.getChildren().remove(predavanjeUToku);
            lijevi.getChildren().add(nastavi);
        });
    }
    
    private void HandleButtonNastavi() {
        nastavi.setOnAction((event) -> {
            server.nastavi();
            
            lijevi.getChildren().remove(nastavi);
            lijevi.getChildren().add(pauza);
            lijevi.getChildren().add(predavanjeUToku);
        });
    }
    
    private void HandleOpcijuDodajPredmet() {
        dodajPredmet.setOnAction((event) -> {
            DBManager.Konektuj();
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Dodaj predmet");
            dialog.setHeaderText("Dodaj predmet");
            dialog.setContentText("Naziv predmeta:");
            
            Optional<String> rezultat = dialog.showAndWait();
            rezultat.ifPresent(predmet -> {
                if (DBManager.dodajNoviPredmet(predmet)) {
                    Kontroler.prikaziObavjestenje(
                        "Obavještenje",
                        "Obavještenje",
                        "Predmet uspješno dodat",
                        Alert.AlertType.INFORMATION
                    );
                    predmeti.add(predmet);
                } else {
                    Kontroler.prikaziObavjestenje(
                        "Greška",
                        "Greška",
                        "Predmet nije dodat",
                        Alert.AlertType.ERROR
                    );
                }
            });
            DBManager.Diskonektuj();
        });
    }
    
    private void HandleOpcijuDodajStudenta() {
        dodajStudenta.setOnAction((event) -> {
            DBManager.Konektuj();
            Dialog<ArrayList<String>> dialog = new Dialog<>();
            dialog.setTitle("Dodaj studenta");
            dialog.setHeaderText("Dodaj studenta\nU ime/prezime studenta ne treba stavljati karaktere: šđčćž");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField indeks = new TextField();
            TextField imePrezime = new TextField();
            ComboBox<String> fakultet = new ComboBox<>();
            
            indeks.setPromptText("Npr. 2018/123456");
            indeks.getStylesheets().add(getClass().getResource("Profesor.css").toExternalForm());
            
            fakultet.getItems().addAll("Pravni", "Filoloski", "Poslovna ekonomija",
                                       "Računarstvo i informatika");

            gridPane.add(new Label("Indeks"), 0, 0);
            gridPane.add(indeks, 1, 0);
            gridPane.add(new Label("Ime i prezime:"), 0, 1);
            gridPane.add(imePrezime, 1, 1);
            gridPane.add(new Label("Fakultet"), 0, 2);
            gridPane.add(fakultet, 1, 2);
            
            dialog.setResultConverter(button -> {
                if (button.getText().equals("OK")) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(indeks.getText());
                    temp.add(imePrezime.getText());
                    temp.add(fakultet.getValue());
                    return temp;
                }
                return null;
            });

            dialog.getDialogPane().setContent(gridPane);
            Optional<ArrayList<String>> rezultat = dialog.showAndWait();
            
            if (rezultat != null) {
                rezultat.ifPresent((podaci) -> {
                    String indeksS = podaci.get(0);
                    String imePrezimeS = podaci.get(1);
                    String fakultetS = podaci.get(2);
                    
                    if (DBManager.dodajNovogStudenta(indeksS, imePrezimeS, fakultetS)) {
                        Student student = new Student(indeksS, imePrezimeS, fakultetS);
                        UlogovaniStudenti.getStudenti().put(indeksS, student);
                        
                        Kontroler.prikaziObavjestenje(
                            "Obavještenje",
                            "Obavještenje",
                            "Student uspješno dodat",
                            Alert.AlertType.INFORMATION
                        );
                    } else {
                        Kontroler.prikaziObavjestenje(
                            "Greška",
                            "Greška",
                            "Student nije dodat",
                            Alert.AlertType.ERROR
                        );
                    }
                });
            }
            DBManager.Diskonektuj();
        });
    };
    
    private void HandleButtonObrisiStudenta() {
        obrisiStudenta.setOnAction((event) -> {
            Student student = (Student)tabela.getSelectionModel().getSelectedItem();
            UlogovaniStudenti.getUlogovaniStudenti().remove(student);
        });
    }
    
    private boolean provjeriAktivnost() {
        switch ((String)izborPredavanja.getValue()) {
            case "Programiranje 1":   return true;
            case "Programiranje 2":   return true;
            case "SPA":               return true;
            case "Programski jezici": return true;
            default:                  return false;
        }
    }

    public Server getServer() {
        return server;
    }
    
    public String getPredmet() {
        return (String)izborPredavanja.getValue();
    }
    
    public boolean isVjezba() {
        return vjezbe.isSelected();
    }
    
}
