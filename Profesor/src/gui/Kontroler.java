package gui;

import DB.DBManager;
import gui.profesor.ProfesorKontroler;
import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.Logger;

public class Kontroler {
    
    private static ProfesorKontroler profesor;
    private static Scene scene;
    private static Stage stage;
    
    public static void init(Stage stage) {
        profesor = new ProfesorKontroler();
        scene    = new Scene(profesor.getRoot());
        
        stage.setOnCloseRequest((event) -> {
            Logger.close();
            DBManager.Diskonektuj();
            System.exit(0);
        });
        stage.setResizable(false);
        stage.setTitle("e-Evidencija");
        stage.getIcons().add(new Image("file:\\" + Paths.get("").toAbsolutePath().toString() + "\\ikonica.png"));
        stage.setScene(scene);
        stage.show();
        Kontroler.stage = stage;
    }
    
    public static void prikaziObavjestenje(String naslov, String header, String poruka, Alert.AlertType tip) {
        Alert greska = new Alert(tip);
        greska.setTitle(naslov);
        greska.setHeaderText(header);
        greska.setContentText(poruka);
        greska.showAndWait();
    }

    public static ProfesorKontroler getProfesorGUI() { return profesor; }
    public static Scene getScene() { return scene; }
    public static Stage getStage() { return stage; }
    
}
