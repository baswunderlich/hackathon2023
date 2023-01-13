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
}
