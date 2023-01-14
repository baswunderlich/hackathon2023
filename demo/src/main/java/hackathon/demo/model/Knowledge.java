package hackathon.demo.model;

public class Knowledge {
    String hersteller;
    int wirkungsgrad;
    long lebensdauerNachMessung; //-1 wenn immernoch existiert
    long lebensdauerGesamt;
    long lebensdauerBisMessung;
    int anzahlGeladen;

    int capacity;

    public Knowledge(String hersteller, int wirkungsgrad, long lebensdauerNachMessung, long lebensdauerGesamt, long lebensdauerBisMessung, int anzahlGeladen, int capacity) {
        this.hersteller = hersteller;
        this.wirkungsgrad = wirkungsgrad;
        this.lebensdauerNachMessung = lebensdauerNachMessung;
        this.lebensdauerGesamt = lebensdauerGesamt;
        this.lebensdauerBisMessung = lebensdauerBisMessung;
        this.anzahlGeladen = anzahlGeladen;
        this.capacity = capacity;
    }

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

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public void setWirkungsgrad(int wirkungsgrad) {
        this.wirkungsgrad = wirkungsgrad;
    }

    public long getLebensdauerNachMessung() {
        return lebensdauerNachMessung;
    }

    public void setLebensdauerNachMessung(long lebensdauerNachMessung) {
        this.lebensdauerNachMessung = lebensdauerNachMessung;
    }

    public long getLebensdauerGesamt() {
        return lebensdauerGesamt;
    }

    public void setLebensdauerGesamt(long lebensdauerGesamt) {
        this.lebensdauerGesamt = lebensdauerGesamt;
    }

    public long getLebensdauerBisMessung() {
        return lebensdauerBisMessung;
    }

    public void setLebensdauerBisMessung(long lebensdauerBisMessung) {
        this.lebensdauerBisMessung = lebensdauerBisMessung;
    }

    public void setAnzahlGeladen(int anzahlGeladen) {
        this.anzahlGeladen = anzahlGeladen;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
