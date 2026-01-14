package ro.usv.servicii;

import ro.usv.dao.Dao;
import ro.usv.dao.SerializareDaoComplet;
import ro.usv.modele.Apartament;
import ro.usv.modele.Locuinta;
import ro.usv.modele.RegimHotelier;

import java.util.ArrayList;
import java.util.List;

public class AgentieImobiliaraServ implements IAgentieImobiliaraServ1 {
    // dao ul
    //chestia cu fisiere sau linie cmd
    private Dao<Apartament, Integer> baza_date;
    public AgentieImobiliaraServ(String fisier) {
        this.baza_date = new SerializareDaoComplet<>(fisier);
    }

    public AgentieImobiliaraServ() {
        this.baza_date = new SerializareDaoComplet<>();
    }

    @Override
    public List<Apartament> getApartamentente() {
        return baza_date.getAll();
    }

    @Override
    public void setStocare(String nume) {
        if (nume == null || nume.trim().isEmpty()) {
            this.baza_date = new SerializareDaoComplet<>();
        } else {
            this.baza_date = new SerializareDaoComplet<>(nume);
        }
    }
    @Override
    public void saveApartament(Apartament ap) {
        // verific manual id ul
        if (baza_date.get(ap.getId()) != null) {
            throw new IllegalArgumentException("Id duplicat");
        }
        try {
            baza_date.save(ap);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Id duplicat");
        }
    }

    //pt toate deodata
    @Override
    public void deleteApartmente() {
        baza_date.deleteAll();
    }
    //pe bucata
    @Override
    public void deleteApartment(int id) {
        try {
            baza_date.delete(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Eroare. delete: obj cu id=" + id + " nu exista");
        }
    }

    @Override
    public Apartament getApartamentById(int id) {
        return baza_date.get(id);
    }









//pt nr 1
@Override
public void rentApartament(int id) {
    Apartament ap = baza_date.get(id);

    if (ap == null) {
        //momdif corect
        throw new IllegalArgumentException("Eroare. rent: ap. cu id=" + id + " nu exista");

    }


    String starea = ap.getStare();
    if ("inchiriat".equals(starea)) {
        throw new IllegalArgumentException("Eroare. Un apartament inchiriat nu poate fi inchiriat");
    }

    if ("vandut".equals(starea)) {
        throw new IllegalArgumentException("Eroare. Un apartament vandut nu poate fi inchiriat");
    }


    if ("liber".equals(starea)) {
        ap.setStare("inchiriat");
        baza_date.update(ap);
    }
}
    @Override
    public int countApartamente(String tip) {
        List<Apartament> toate = baza_date.getAll();

        if (tip == null || tip.trim().isEmpty()) {
            return toate.size();
        }

        int t = 0;
        for (Apartament a : toate) {
            if ("L".equalsIgnoreCase(tip) ){
                if(a instanceof Locuinta) {
                t++;
            }} else if ("RH".equalsIgnoreCase(tip) ) {
                if( a instanceof RegimHotelier)
                t++;
            }
        }
        return t;
    }



    @Override
    public void releaseApartament(int id) {
        Apartament a = baza_date.get(id);
        if (a == null) {
            throw new IllegalArgumentException("Eroare. Nu exista apartament cu id=" + id);
        }

        if (!"inchiriat".equals(a.getStare())) {
            throw new IllegalArgumentException("Eroare. Ap. nu a fost inchiriat");
        }

        a.setStare("liber");
        baza_date.update(a);
    }

    @Override
    public List<Apartament> getRentedApartamente(String tip) {
        List<Apartament> sursa = baza_date.getAll();
        List<Apartament> rezultat = new ArrayList<>();

        for (Apartament ap : sursa) {
            if ("inchiriat".equals(ap.getStare())) {
                boolean e_ok = false;
                if (tip == null || tip.isEmpty()) {
                    e_ok = true;
                } else if ("L".equalsIgnoreCase(tip) && ap instanceof Locuinta) {
                    e_ok = true;
                } else if ("RH".equalsIgnoreCase(tip) && ap instanceof RegimHotelier) {
                    e_ok = true;
                }

                if (e_ok) {
                    rezultat.add(ap);
                }
            }
        }
        return rezultat;
    }
}