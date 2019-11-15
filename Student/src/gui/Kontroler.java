package gui;

import gui.student.StudentKontroler;
import java.nio.file.Paths;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Kontroler {
    
    private static StudentKontroler student;
    private static Scene scene;
    private static Stage stage;
    
    public static void init(Stage stage) {
        student = new StudentKontroler();
        scene   = new Scene(student.getRoot());
        
        Platform.setImplicitExit(false);
        
        stage.setOnCloseRequest((event) -> {
            event.consume();
        });
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("e-Evidencija");
        stage.getIcons().add(new Image("file:\\" + Paths.get("").toAbsolutePath().toString() + "\\ikonica.png"));
        stage.setScene(scene);
        stage.show();
        Kontroler.stage = stage;
    }
    
    public static void kreirajAlert(AlertType tip, String naslov, String tekst) {
        Alert greska = new Alert(tip);
        greska.initStyle(StageStyle.UTILITY);
        greska.setHeaderText(null);
        greska.setTitle(naslov);
        greska.setContentText(tekst);
        greska.showAndWait();
    }

    public static Scene getScene() { return scene; }
    public static Stage getStage() { return stage; }
    
}
