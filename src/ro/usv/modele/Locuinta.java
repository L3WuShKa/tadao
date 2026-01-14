package ro.usv.modele;

public class Locuinta extends Apartament {
    private Integer nr_persoane;

    public Locuinta(Integer id, Double suprafata, Integer an, String strada, Integer nr, String scara, Integer etaj, Integer nrApt, Integer nrPers) {
        super(id, suprafata, an, strada, nr, scara, etaj, nrApt);

        if (nrPers < 0) {
            throw new IllegalArgumentException("Valori incorecte pentru crearea unei locuinte");
        }
        this.nr_persoane = nrPers;
    }

    @Override
    public String toString() {
        return "{Tip=L, " + text_comun() + ", nrPersoane=" + nr_persoane + ", stare=" + getStare() + "}";
    }
}