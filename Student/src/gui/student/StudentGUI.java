package gui.student;

import java.nio.file.Paths;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class StudentGUI {

    protected VBox root;
    protected ImageView logo;
    protected Label tekst;
    protected HBox loginKontejner;
    protected TextField indeks;
    protected Button login;

    protected StudentGUI() {
        root = new VBox();
        logo = new ImageView("file:\\" + Paths.get("").toAbsolutePath().toString() + "\\logo.png");
        tekst = new Label();
        loginKontejner = new HBox();
        indeks = new TextField();
        login = new Button("Login");

        root.getStylesheets().add(getClass().getResource("Student.css").toExternalForm());
        root.setId("root");
        root.setAlignment(Pos.CENTER);

        tekst.setText("Poštovani-a,\n\n"
                + "da bi evidentirali Vaše prisustvo na predavanjima/vježbama\n"
                + "potrebno je da unesete vaš broj indeksa.");
        tekst.setId("tekst");

        loginKontejner.setId("loginKontejner");
        loginKontejner.setAlignment(Pos.CENTER);
        loginKontejner.setPrefSize(494, 125);

        indeks.setPrefWidth(200);
        indeks.setPromptText("Npr. 2018/123456");

        login.setPrefWidth(85);
        login.setDefaultButton(true);

        loginKontejner.getChildren().addAll(new Label("Indeks:"), indeks, login);
        root.getChildren().addAll(logo, tekst, loginKontejner,
                new Label("Copyright Studentska kancelarija © 2018"));
    }

    public VBox getRoot() {
        return root;
    }

}
