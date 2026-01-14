package ro.usv;

import ro.usv.modele.*;
import ro.usv.servicii.AgentieImobiliaraServ;
import ro.usv.servicii.IAgentieImobiliaraServ1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;

import java.util.List;
import java.util.Scanner;


public class AgentieImobiliaraClient {

    private static IAgentieImobiliaraServ1 serviciu = new AgentieImobiliaraServ();

    public static void main(String[] args) {
        Scanner cititor;

        // arg pt fisier sa vad daca am
        if (args.length > 0) {
            try {
                cititor = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("Fisierul de comenzi nu a fost gasit: " + args[0]);
                return;
            }
        } else {
            cititor = new Scanner(System.in);
        }

        while (cititor.hasNextLine()) {
            String linie = cititor.nextLine().trim();
            if (linie.isEmpty()) continue;

            //afisez comanda inca oadta
            System.out.println(linie);

            List<String> bucati = sparge_linie(linie);
            if (bucati.isEmpty()) continue;

            String cmd = bucati.get(0);

            try {
                switch (cmd) {
                    case "release":
                        fa_eliberare(bucati);
                        break;
                    case "count":
                        fa_numarare(bucati);
                        break;
                    case "rent":
                        fa_inchiriere(bucati);
                        break;
                    case "rented":
                        fa_rented(bucati);
                        break;
                    case "list":
                        fa_listare(bucati);
                        break;
                    case "file":
                        fa_fisier(bucati);
                        break;
                    case "available":
                        fa_available(bucati);
                        break;
                    case "delete":
                        fa_stergere(bucati);
                        break;
                    case "clear":
                        serviciu.deleteApartmente();
                        System.out.println("S-au eliminat toate apartamentele");
                        break;
                    case "buy":
                        fa_cumparare(bucati);
                        break;
                    case "stop":
                        System.out.println("La revedere!");
                        return;
                    case "rem":
                        break;
                    default:
                        System.out.println("Eroare. Comanda neimplementata");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

//modificat la functii cuvant corect
    private static void fa_numarare(List<String> params) {
        String tip = (params.size() > 1) ? params.get(1) : null;
        int nr = serviciu.countApartamente(tip);

        if (tip == null) {
            System.out.println("Nr.apartamente: " + nr);
        } else if ("L".equalsIgnoreCase(tip)) {
            System.out.println("Nr.locuinte: " + nr);
        } else if ("RH".equalsIgnoreCase(tip)) {
            System.out.println("Nr.apartamente in regim hotelier: " + nr);
        } else {
            System.out.println("Nu sunt apartamente de tipul " + tip);
        }
    }
    private static void fa_cumparare(List<String> params) {
        if (params.size() < 2) {
            System.out.println("Eroare. Numarul parametrilor nu este corect");
            return;
        }


        String tip = params.get(1);
        try {
            if ("L".equalsIgnoreCase(tip)) {
                if (params.size() != 11) {
                    System.out.println("Eroare. Numarul parametrilor nu este corect");
                    return;
                }
                Integer id = Integer.parseInt(params.get(2));
                Double mp = Double.parseDouble(params.get(3));
                Integer an = Integer.parseInt(params.get(4));
                String str = params.get(5);
                Integer nr = Integer.parseInt(params.get(6));
                String scara = params.get(7);
                Integer et = Integer.parseInt(params.get(8));
                Integer nrAp = Integer.parseInt(params.get(9));
                Integer nrLoc = Integer.parseInt(params.get(10));

                Locuinta l = new Locuinta(id, mp, an, str, nr, scara, et, nrAp, nrLoc);
                serviciu.saveApartament(l);

            } else if ("RH".equalsIgnoreCase(tip) || "RHotel".equalsIgnoreCase(tip)) {
                if (params.size() != 12) {
                    System.out.println("Eroare. Numarul parametrilor nu este corect");
                    return;
                }
                Integer id = Integer.parseInt(params.get(2));
                Double mp = Double.parseDouble(params.get(3));
                Integer an = Integer.parseInt(params.get(4));
                String str = params.get(5);
                Integer nr = Integer.parseInt(params.get(6));
                String scara = params.get(7);
                Integer et = Integer.parseInt(params.get(8));
                Integer nrAp = Integer.parseInt(params.get(9));
                String nume = params.get(10);
                Long cod = Long.parseLong(params.get(11));

                RegimHotelier rh = new RegimHotelier(id, mp, an, str, nr, scara, et, nrAp, nume, cod);
                serviciu.saveApartament(rh);
            } else {
                System.out.println("Eroare. Tip apartament necunoscut");
            }
        } catch (NumberFormatException e) {
            System.out.println("Eroare. Format incorect pentru parametrul (" + e.getMessage() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void fa_fisier(List<String> params) {
        if (params.size() > 1) {
            serviciu.setStocare(params.get(1));
        } else {
            serviciu.setStocare(null);
        }
    }
    private static void fa_available(List<String> params) {
        String filtru = (params.size() > 1) ? params.get(1) : null;
        if (filtru != null && !filtru.equalsIgnoreCase("L") && !filtru.equalsIgnoreCase("RH") && !filtru.equalsIgnoreCase("RHotel")) {
            System.out.println("Eroare. Tip apartament necunoscut");
            return;
        }
        List<Apartament> toate = serviciu.getApartamentente();
        List<String> de_afisat = new ArrayList<>();

        for (Apartament ap : toate) {
            if ("liber".equals(ap.getStare())) {
                boolean ok = false;
                if (filtru == null) ok = true;
                else if ("L".equalsIgnoreCase(filtru) && ap instanceof Locuinta) ok = true;
                else if (("RH".equalsIgnoreCase(filtru) || "RHotel".equalsIgnoreCase(filtru)) && ap instanceof RegimHotelier) ok = true;

                if (ok) {
                    String t = (ap instanceof Locuinta) ? "L" : "RH";
                    // modificat aici sa aiba {} in loc de ()
                    de_afisat.add("{Tip=" + t + ", id=" + ap.getId() + ", str." + ap.get_strada_nume() + ", etaj " + ap.get_etaj() + ", stare=liber}");
                }
            }
        }
        afisare_stringuri_virgule_cu_paranteze(de_afisat);
    }

    private static void fa_stergere(List<String> params) {
        if (params.size() < 2) {
            System.out.println("Eroare. Numarul parametrilor nu este corect");
            return;
        }
        try {
            int id = Integer.parseInt(params.get(1));
            serviciu.deleteApartment(id);
        } catch (NumberFormatException e) {
            System.out.println("Eroare. Format incorect pentru parametrul " + params.get(1));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void fa_listare(List<String> params) {
        if (params.size() == 1) {
            List<Apartament> lista = serviciu.getApartamentente();
            // de modificat aici ca sa fol afisare cu paranteze []
            afisare_cu_paranteze(lista);
        } else {
            try {
                int id = Integer.parseInt(params.get(1));
                Apartament ap = serviciu.getApartamentById(id);
                if (ap == null) {
                    System.out.println("Eroare. Nu exista apartament cu id=" + id);
                } else {
                    System.out.println(ap);
                }
            } catch (NumberFormatException e) {
                System.out.println("Eroare. Format incorect pentru parametrul " + params.get(1));
            }
        }
    }


    private static void fa_inchiriere(List<String> params) {
        if (params.size() < 2) {
            System.out.println("Eroare. Numarul parametrilor nu este corect");
            return;
        }
        try {
            int id = Integer.parseInt(params.get(1));
            serviciu.rentApartament(id);
            System.out.println("OK");
        } catch (NumberFormatException e) {
            System.out.println("Eroare. Format incorect pentru parametrul " + params.get(1));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void fa_eliberare(List<String> params) {
        if (params.size() < 2) {
            System.out.println("Eroare. Numarul parametrilor nu este corect");
            return;
        }
        try {
            int id = Integer.parseInt(params.get(1));
            serviciu.releaseApartament(id);
            System.out.println("OK");
        } catch (NumberFormatException e) {
            System.out.println("Eroare. Format incorect pentru parametrul " + params.get(1));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void fa_rented(List<String> params) {
        String tip = (params.size() > 1) ? params.get(1) : null;

        if (tip != null && !tip.equalsIgnoreCase("L") && !tip.equalsIgnoreCase("RH")) {
            System.out.println("Eroare. Tip apartament necunoscut");
            return;
        }

        List<Apartament> rezultat = serviciu.getRentedApartamente(tip);
        afisare_cu_paranteze(rezultat);
    }



    private static void afisare_cu_virgule(List<Apartament> lista) {
        if (lista.isEmpty()) {
            System.out.println("[]");
            return;
        }
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            if (i < lista.size() - 1) {
                System.out.println(",");
            } else {
                System.out.println("");
            }
        }
    }


    private static void afisare_stringuri_virgule_cu_paranteze(List<String> lista) {
        if (lista.isEmpty()) {
            System.out.println("[]");
            return;
        }
        System.out.println("[");
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            if (i < lista.size() - 1) {
                System.out.println(",");
            } else {
                System.out.println("]");
            }
        }
    }


    private static void afisare_cu_paranteze(List<Apartament> lista) {
        if (lista.isEmpty()) {
            System.out.println("[]");
            return;
        }
        System.out.println("[");
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            if (i < lista.size() - 1) {
                System.out.println(",");
            } else {
                System.out.println("]");
            }
        }
    }

    private static List<String> sparge_linie(String text) {
        List<String> tokenuri = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(text); // aici i was in need of help geminii
        while (m.find()) {
            String t = m.group(1).trim();
            if (t.startsWith("\"") && t.endsWith("\"")) {
                t = t.substring(1, t.length() - 1);
            }
            if (!t.isEmpty())
                tokenuri.add(t);
        }
        return tokenuri;
    }
}