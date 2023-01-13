package hackathon.demo.model;

import java.util.Date;

public class Battery {
    String hersteller;
    int wirkungsgrad;
    int kapazitaet; //mAh
    int aufladungenMinuten;
    Date herstellungsdatum;

    public Battery(String hersteller, int wirkungsgrad, int kapazitaet, int aufladungenMinuten, Date herstellungsdatum) {
        this.hersteller = hersteller;
        this.wirkungsgrad = wirkungsgrad;
        this.kapazitaet = kapazitaet;
        this.aufladungenMinuten = aufladungenMinuten;
        this.herstellungsdatum = herstellungsdatum;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public int getWirkungsgrad() {
        return wirkungsgrad;
    }

    public void setWirkungsgrad(int wirkungsgrad) {
        this.wirkungsgrad = wirkungsgrad;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public int getAufladungenMinuten() {
        return aufladungenMinuten;
    }

    public void setAufladungenMinuten(int aufladungenMinuten) {
        this.aufladungenMinuten = aufladungenMinuten;
    }

    public Date getHerstellungsdatum() {
        return herstellungsdatum;
    }

    public void setHerstellungsdatum(Date herstellungsdatum) {
        this.herstellungsdatum = herstellungsdatum;
    }
}
