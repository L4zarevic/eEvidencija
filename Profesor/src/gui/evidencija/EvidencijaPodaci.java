package gui.evidencija;

public class EvidencijaPodaci {
    
    private String indeks;
    private String imePrezime;
    private String fakultet;
    private int prisustvo;
    private String predmet;

    public String getPredmet() {
        return predmet;
    }    
    
    public String getIndeks() {
        return indeks;
    }

    public String getFakultet() {
        return fakultet;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public int getPrisustvo() {
        return prisustvo;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public void setFakultet(String fakultet) {
        this.fakultet = fakultet;
    }

    public void setPrisustvo(int prisustvo) {
        this.prisustvo = prisustvo;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }
    
}
