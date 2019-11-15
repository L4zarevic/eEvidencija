package gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TitledBorderPane extends StackPane {
    
    private final Label naslov;
    private final StackPane elementi;

    public TitledBorderPane() {
        naslov   = new Label();
        elementi = new StackPane();
        
        init(null);
    }
    
    public TitledBorderPane(String naslovTekst) {
        naslov   = new Label(" " + naslovTekst + " ");
        elementi = new StackPane();
        
        init(null);
    }
    
    public TitledBorderPane(Node sadrzaj) {
        naslov   = new Label();
        elementi = new StackPane();
        
        init(sadrzaj);
    }
    
    public TitledBorderPane(String naslovTekst, Node sadrzaj) {
        naslov   = new Label(" " + naslovTekst + " ");
        elementi = new StackPane();
        
        init(sadrzaj);
    }
    
    private void init(Node sadrzaj) {
        getStylesheets().add(getClass().getResource("TitledBorderPane.css").toExternalForm());
        
        naslov.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(naslov, Pos.TOP_CENTER);

        elementi.getStyleClass().add("bordered-titled-content");
        
        if (sadrzaj != null)
            elementi.getChildren().add(sadrzaj);

        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(naslov, elementi);
    }
    
    public void add(Node node) {
        elementi.getChildren().add(node);
    }
    
    public void addAll(Node... nodes) {
        for (Node node : nodes) {
            add(node);
        }
    }
    
    public void setNaslov(String tekstNaslov) {
        naslov.setText(" " + tekstNaslov + " ");
    }

}
