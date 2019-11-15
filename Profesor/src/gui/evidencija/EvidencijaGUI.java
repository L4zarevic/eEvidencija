package gui.evidencija;

import java.nio.file.Paths;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class EvidencijaGUI {
    
    protected final VBox root;
    protected final Scene scene;
    protected final Stage stage;
    protected final TableView tabela;
    protected final Button print;

    protected EvidencijaGUI(String predmet, boolean vjezba) {
        root   = new VBox();
        scene  = new Scene(root);
        stage  = new Stage(StageStyle.DECORATED);
        tabela = new TableView();
        print  = new Button("Sačuvaj prisustvo");
        
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(15));
        kreirajTabelu(predmet);
        
        root.getChildren().addAll(tabela, print);
        stage.setScene(scene);
        stage.setTitle("Evidentirani studenti");
        stage.getIcons().add(new Image("file:\\" + Paths.get("").toAbsolutePath().toString() + "\\ikonica.png"));
    }
    
    public void prikazi() {
        stage.show();
    }
    
    private void kreirajTabelu(String predmet) {
        TableColumn indeks = new TableColumn("Indeks");
        TableColumn imePrezime = new TableColumn("Ime i prezime");
        TableColumn fakultet = new TableColumn("Fakultet");
        TableColumn predmetC = new TableColumn("Predmet");
        TableColumn prisustvo = new TableColumn("Prisustvo");
        
        indeks.setPrefWidth(100);
        imePrezime.setPrefWidth(150);
        fakultet.setPrefWidth(150);
        predmetC.setPrefWidth(100);
        prisustvo.setPrefWidth(100);
        
        indeks.setCellValueFactory(new PropertyValueFactory("indeks"));
        imePrezime.setCellValueFactory(new PropertyValueFactory("imePrezime"));
        fakultet.setCellValueFactory(new PropertyValueFactory("fakultet"));
        predmetC.setCellValueFactory(new PropertyValueFactory("predmet"));
        prisustvo.setCellValueFactory(new PropertyValueFactory("prisustvo"));
        
        tabela.getColumns().addAll(indeks, imePrezime, fakultet, predmetC, prisustvo);
    }
    
}
