package gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InfoGUI {
    
    private final VBox root;
    private final Label label;
    private final Scene scene;
    private final Stage stage;

    public InfoGUI() {
        this.root  = new VBox();
        this.label = new Label();
        this.scene = new Scene(root);
        this.stage = new Stage(StageStyle.UTILITY);
        
        stage.setTitle("Info");
        
        root.setId("root");
        root.getStylesheets().add(getClass().getResource("Info.css").toExternalForm());
        
        label.setMinHeight(410);
        label.setWrapText(true);
        label.setText(
                "e-Evidencija je program za evidenciju prisustva studenata na predavanjima/vježbama.\n"
                + "\n"
                + "Za korištenje programa potrebno je: \n"
                + "1. Izabrati tip nastave \"predavanje\" ili \"vježbe\"\n"
                + "2. Izabrati željeni predmet iz liste predmeta (ukoliko ne postoji predmet u listi, predmet možete dodati opcijom \"Dodaj predmet\")\n"
                + "3. Izabrati opciju \"Početak rada\" nakon cega je studentima omogućeno da se prijave\n"
                + "4. U slucaju pauze studentima u toku predavanja dodata je i opcija \"Pauza\" cime se privremeno onemogućava prijavljivanje studentima, nakon pauze \n"
                + "   izabrati opcju Nastavak rada\n"
                + "5. Na kraju predavanja/vježbe izabrati \"Kraj rada\" nakon cega ce prisustvo studenata biti evidentirano\n"
                + "\n"
                + "\n"
                + "Dodatne opcije:\n"
                + "\n"
                + "-Evidencija prisustva\n"
                + "Ovom opcijom dobija se evidencija prisustva (na osnovu izabranog tipa nastave i željenog predmeta iz glavnog prozora programa)\n"
                + "\n"
                + "-Dodaj novog studenta\n"
                + "Ovom opcijom se dodaje novi student u bazu\n"
                + "\n"
                + "-Dodaj novi predmet\n"
                + "Ovom opcijom se dodaje novi predmet u listu predmeta\n"
                + "\n"
                + "Copyright Studentska kancelarija © 2018 - studentska.kancelarija@sinergija.edu.ba");
        
        root.getChildren().add(label);
        stage.setScene(scene);
    }
    
    public void prikazi() {
        stage.show();
    }
    
}
