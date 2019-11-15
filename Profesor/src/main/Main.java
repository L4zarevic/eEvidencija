package main;

import DB.DBManager;
import gui.Kontroler;
import javafx.application.Application;
import javafx.stage.Stage;
import student.UlogovaniStudenti;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        DBManager.Konektuj();
        UlogovaniStudenti.init();
        DBManager.Diskonektuj();
        
        Logger.init();
        Kontroler.init(primaryStage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
