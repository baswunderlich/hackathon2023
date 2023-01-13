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

    public int getWirkungsgrad() {
        return wirkungsgrad;
    }
    public int getAnzahlGeladen() {
        return anzahlGeladen;
    }

    public long getTime(){
        return lebensdauerBisMessung;
    }
}
