package hackathon.demo.model;

import java.util.Date;

public class Battery {
    String id;
    String hersteller;
    int wirkungsgrad;
    int kapazitaet; //mAh
    int chargingCycles;
    long herstellungsdatum;

    public Battery(String id, String hersteller, int wirkungsgrad, int kapazitaet, int chargingCycles, long herstellungsdatum) {
        this.id = id;
        this.hersteller = hersteller;
        this.wirkungsgrad = wirkungsgrad;
        this.kapazitaet = kapazitaet;
        this.chargingCycles = chargingCycles;
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

    public int getChargingCycles() {
        return chargingCycles;
    }

    public void setChargingCycles(int aufladungenMinuten) {
        this.chargingCycles = aufladungenMinuten;
    }

    public long getHerstellungsdatum() {
        return herstellungsdatum;
    }

    public void setHerstellungsdatum(long herstellungsdatum) {
        this.herstellungsdatum = herstellungsdatum;
    }
}
