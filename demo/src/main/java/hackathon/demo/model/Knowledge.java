package hackathon.demo.model;

public class Knowledge {
    String hersteller;
    int wirkungsgrad;
    long lebensdauerNachMessung; //-1 wenn immernoch existiert
    long lebensdauerGesamt;
    long lebensdauerBisMessung;
    int anzahlGeladen;

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

    public void setLebensdauerNachMessung(int lebensdauerNachMessung) {
        this.lebensdauerNachMessung = lebensdauerNachMessung;
    }

    public void setLebensdauerGesamt(int lebensdauerGesamt) {
        this.lebensdauerGesamt = lebensdauerGesamt;
    }

    public int getAnzahlGeladen() {
        return anzahlGeladen;
    }

    public void setAnzahlGeladen(int anzahlGeladen) {
        this.anzahlGeladen = anzahlGeladen;
    }

    public long getTime(){
        return lebensdauerBisMessung;
    }
}
