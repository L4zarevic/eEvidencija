package gui.student;

import klijent.Klijent;

public class StudentKontroler extends StudentGUI {
    
    private Klijent klijent;

    public StudentKontroler() {
        super();
        HandleButtonLogin();
    }
    
    private void HandleButtonLogin() {
        login.setOnAction((event) -> {
            klijent = new Klijent();
            klijent.setIndeks(indeks.getText());
            indeks.setText("");
            new Thread(klijent).start();
        });
    }
    
}
