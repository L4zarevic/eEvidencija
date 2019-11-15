package student;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
    
    private final String imePrezime;
    private final String indeks;
    private final String fakultet;
    private int aktivnost;
    
    // Samo za interno koriscenje
    private String ipAdresa;
    
    public Student(String indeks, String imePrezime, String fakultet) {
        this.indeks     = indeks;
        this.imePrezime = imePrezime;
        this.fakultet   = fakultet;
    }

    public String getImePrezime() { return imePrezime; }
    public String getIndeks()     { return indeks;     }
    public String getFakultet()   { return fakultet;   }
    public int getAktivnost()     { return aktivnost;  }
    
    public String getIpAdresa() { return ipAdresa; }
    public void setIpAdresa(String ipAdresa) { this.ipAdresa = ipAdresa; }

    public void setAktivnost(int aktivnost) { this.aktivnost = aktivnost; }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof Student)) {
            return false;
        }
        
        Student student = (Student)o;
        return this.getIndeks().equals(student.getIndeks());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.imePrezime);
        hash = 89 * hash + Objects.hashCode(this.indeks);
        hash = 89 * hash + Objects.hashCode(this.fakultet);
        return hash;
    }
    
}
