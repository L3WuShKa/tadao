package ro.usv.modele;

import ro.usv.dao.Entitate;
import java.io.Serializable;

public abstract class Apartament implements Entitate<Integer>, Serializable {
    private Integer id_ap;
    private String strada_nume;
    private Integer etaj;
    private Double metri_patrati;
    private Integer nr_apartament;
    private Integer nr_strada;
    private String scara_bloc;
    private String starea_curenta; // liber,inchiriat, sau vandut
    private Integer an_constr;

    public Apartament(Integer id, Double suprafata, Integer an, String strada,
                      Integer nr, String scara, Integer etaj, Integer nrApt) {
        //verificarile alea
        if (id == null || suprafata <= 0 || strada == null || nr <= 0 || an > 2025 + 1) {
            throw new IllegalArgumentException("Valori incorecte pentru crearea unui apartament");
        }

        this.id_ap = id;
        this.metri_patrati = suprafata;
        this.an_constr = an;
        this.strada_nume = strada;
        this.nr_strada = nr;
        this.scara_bloc = scara;
        this.etaj = etaj;
        this.nr_apartament = nrApt;
        this.starea_curenta = "liber";  //default e liber
    }

    @Override
    public Integer getId() {
        return id_ap;
    }

    public void setId(Integer id) { this.id_ap = id; }

    // gett eri si setteri
    public String get_strada_nume() { return strada_nume; }
    public String getStare() { return starea_curenta; }
    public Integer get_etaj() { return etaj; }
    public void setStare(String stareNoua)
    {
        this.starea_curenta = stareNoua;
    }

    public Integer getId_ap() {
        return id_ap;
    }

    public void setId_ap(Integer id_ap) {
        this.id_ap = id_ap;
    }

    public Double getMetri_patrati() {
        return metri_patrati;
    }

    public void setMetri_patrati(Double metri_patrati) {
        this.metri_patrati = metri_patrati;
    }

    public Integer getNr_apartament() {
        return nr_apartament;
    }

    public void setNr_apartament(Integer nr_apartament) {
        this.nr_apartament = nr_apartament;
    }

    public Integer getNr_strada() {
        return nr_strada;
    }

    public void setNr_strada(Integer nr_strada) {
        this.nr_strada = nr_strada;
    }

    public String getScara_bloc() {
        return scara_bloc;
    }

    public void setScara_bloc(String scara_bloc) {
        this.scara_bloc = scara_bloc;
    }

    public Integer getAn_constr() {
        return an_constr;
    }

    public void setAn_constr(Integer an_constr) {
        this.an_constr = an_constr;
    }

    //formatez comun practic doar copii au acces , exact ce am nevoie
    protected String text_comun() { //AAAAAAH modificare marunta ma disperaaaaa
        return "id=" + id_ap + ", suprafata=" + metri_patrati + ", anConstructie=" + an_constr + ", strada='"
                + strada_nume + "', nr=" + nr_strada + ", scara=" + scara_bloc + ", etaj=" + etaj + ", nrApt="
                + nr_apartament;
    }

    //pt dao sa permita preluarea

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Apartament)) return false;
        Apartament altul = (Apartament) obj;
        return id_ap != null ? id_ap.equals(altul.id_ap) : altul.id_ap == null;
    }

    @Override
    public int hashCode() {
        return id_ap != null ? id_ap.hashCode() : 0;
    }
}