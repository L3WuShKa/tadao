package ro.usv.modele;

public class RegimHotelier extends Apartament {
    private Long cif_firma;
    private String nume_firma;


    public RegimHotelier(Integer id, Double suprafata, Integer an, String strada, Integer nr, String scara, Integer etaj, Integer nrApt, String denumire, Long cif) {
        super(id, suprafata, an, strada, nr, scara, etaj, nrApt);

        if (denumire == null || cif <= 0) {
            throw new IllegalArgumentException("Valori incorecte pentru crearea unui apartament in regim hotelier");

        }
        this.nume_firma = denumire;
        this.cif_firma = cif;
    }

    @Override
    public String toString() {

        return "{Tip=RH, " + text_comun() + ", denumire='" + nume_firma + "', CIF=" + cif_firma + ", stare=" + getStare() + "}";
    }
}