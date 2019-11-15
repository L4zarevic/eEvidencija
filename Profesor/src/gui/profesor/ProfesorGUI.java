package gui.profesor;

import DB.DBManager;
import gui.TitledBorderPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class ProfesorGUI {
    
    protected final BorderPane root;
    
    protected final MenuBar meni;
    protected final Menu opcije;
    protected final MenuItem evidentiraniStudenti;
    protected final MenuItem info;
    protected final MenuItem dodajStudenta;
    protected final MenuItem dodajPredmet;
    
    protected final VBox lijevi;
    protected final HBox radioButtonKontejner;
    protected final ToggleGroup grupa;
    protected final RadioButton predavanje;
    protected final RadioButton vjezbe;
    protected final HBox predavanjaKonejner;
    protected final ComboBox izborPredavanja;
    protected final Button pocetakRada;
    protected final Button krajRada;
    protected final Button pauza;
    protected final Button nastavi;
    
    protected final TitledBorderPane desni;
    protected final VBox desniKontejner;
    protected final TableView tabela;
    protected final HBox tabelaKontroler;
    protected final Button obrisiStudenta;
    
    protected final ObservableList<String> predmeti;

    protected ProfesorGUI() {
        root = new BorderPane();
        
        meni                 = new MenuBar();
        opcije               = new Menu("Opcije");
        evidentiraniStudenti = new MenuItem("Evidentirani studenti");
        dodajStudenta        = new MenuItem("Dodaj studenta");
        dodajPredmet         = new MenuItem("Dodaj predmet");
	info                 = new MenuItem("Info");
        
        lijevi               = new VBox();
        radioButtonKontejner = new HBox();
        grupa                = new ToggleGroup();
        predavanje           = new RadioButton("Predavanje");
        vjezbe               = new RadioButton("Vježbe");
        predavanjaKonejner   = new HBox();
        izborPredavanja      = new ComboBox();
        pocetakRada          = new Button("Početak rada");
        krajRada             = new Button("Kraj rada");
        pauza                = new Button("Pauziraj");
        nastavi              = new Button("Nastavi");
        
        desni           = new TitledBorderPane("Prisutni studenti");
        desniKontejner  = new VBox();
        tabela          = new TableView();
        tabelaKontroler = new HBox();
        obrisiStudenta  = new Button("Obriši studenta");
        
        DBManager.Konektuj();
        predmeti = FXCollections.observableArrayList(DBManager.selectPredmeti());
        DBManager.Diskonektuj();
        
        root.getStylesheets().add(getClass().getResource("Profesor.css").toExternalForm());
        root.setId("root");
        
        gornji();
        lijevi();
        desni();
        
        root.setTop(meni);
        root.setLeft(lijevi);
        root.setRight(desni);
        
        VBox temp = new VBox(new Label("Copyright Studentska kancelarija © 2018"));
        temp.setAlignment(Pos.CENTER);
        
        root.setBottom(temp);
    }
    
    private void gornji() {
        meni.setPadding(new Insets(0, 0, 5, 0));
        
        meni.getMenus().add(opcije);
        opcije.getItems().addAll(evidentiraniStudenti, dodajStudenta, dodajPredmet, info);
    }
    
    private void lijevi() {
        lijevi.setId("lijevi");
        
        radioButtonKontejner.setId("radioButtonKontejner");
        
        predavanje.setSelected(true);
        predavanje.setToggleGroup(grupa);
        vjezbe.setToggleGroup(grupa);
        
        predavanjaKonejner.setId("predavanjaKontejner");
        
        izborPredavanja.setPrefWidth(150);
        
        pocetakRada.prefWidthProperty().bind(predavanjaKonejner.widthProperty());
        krajRada.prefWidthProperty().bind(predavanjaKonejner.widthProperty());
        pauza.prefWidthProperty().bind(predavanjaKonejner.widthProperty());
        nastavi.prefWidthProperty().bind(predavanjaKonejner.widthProperty());
        
        radioButtonKontejner.getChildren().addAll(predavanje, vjezbe);
        predavanjaKonejner.getChildren().addAll(new Label("Predmet:"), izborPredavanja);
        lijevi.getChildren().addAll(radioButtonKontejner, predavanjaKonejner, pocetakRada);
    }
    
    private void desni() {
        desni.setId("desni");
        kreirajTabelu();
        
        tabelaKontroler.setAlignment(Pos.CENTER);
        tabelaKontroler.setSpacing(5);
        tabelaKontroler.getChildren().add(obrisiStudenta);
        
        desniKontejner.getChildren().add(tabela);
        desni.addAll(desniKontejner);
    }
    
    private void kreirajTabelu() {
        TableColumn imePrezime = new TableColumn("Ime i prezime");
        TableColumn indeks     = new TableColumn("Indeks");
        TableColumn fakultet   = new TableColumn("Fakultet");
        
        imePrezime.setPrefWidth(150);
        indeks.setPrefWidth(100);
        fakultet.setPrefWidth(150);
        
        imePrezime.setCellValueFactory(new PropertyValueFactory("imePrezime"));
        indeks.setCellValueFactory(new PropertyValueFactory("indeks"));
        fakultet.setCellValueFactory(new PropertyValueFactory("fakultet"));
        
        tabela.getColumns().addAll(imePrezime, indeks, fakultet);
    }

    public BorderPane getRoot() { return root; }
    
}
