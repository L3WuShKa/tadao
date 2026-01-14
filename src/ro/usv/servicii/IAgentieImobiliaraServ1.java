package ro.usv.servicii;

import ro.usv.modele.Apartament;
import java.util.List;

public interface IAgentieImobiliaraServ1 {
    // fct impl nr1
    public void releaseApartament(int id);
    public int countApartamente(String tip_cautat);
    public void rentApartament(int id);
    public List<Apartament> getRentedApartamente(String tip);
    public void setStocare(String numeFisier);
    public Apartament getApartamentById(int id_cautat);
    public void saveApartament(Apartament ap_nou);
    public void deleteApartmente();
    public void deleteApartment(int id_de_sters);
    public List<Apartament> getApartamentente();



}