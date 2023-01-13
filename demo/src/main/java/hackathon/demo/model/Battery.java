package hackathon.demo.model;

public class Battery {
    String batteryPass;
    int herstellungsdatum;
    int[] aufladungen;
    int[] wirkungsgrad;

    public Battery(String batteryPass, int herstellungsdatum, int[] aufladungen, int[] wirkungsgrad) {
        this.batteryPass = batteryPass;
        this.herstellungsdatum = herstellungsdatum;
        this.aufladungen = aufladungen;
        this.wirkungsgrad = wirkungsgrad;
    }
}
