package hackathon.demo.model;

public class Customer {
    String name;
    int minKapazitaet;
    int minWirkungsgrad;
    long erwLebensdauer; //in ms;
    int chargingCycles;
    int preisProBatterie;

    public Customer(String name, int minWirkungsgrad, long erwLebensdauer, int preis, int chargingCycles, int minKapazitaet){
        this.name = name;
        this.minWirkungsgrad = minWirkungsgrad;
        this.erwLebensdauer = erwLebensdauer;
        this.preisProBatterie = preis;
        this.chargingCycles = chargingCycles;
        this.minKapazitaet = minKapazitaet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinWirkungsgrad() {
        return minWirkungsgrad;
    }

    public void setMinWirkungsgrad(int minWirkungsgrad) {
        this.minWirkungsgrad = minWirkungsgrad;
    }

    public long getErwLebensdauer() {
        return erwLebensdauer;
    }

    public void setErwLebensdauer(long erwLebensdauer) {
        this.erwLebensdauer = erwLebensdauer;
    }

    public int getPreisProBatterie() {
        return preisProBatterie;
    }

    public void setPreisProBatterie(int preisProBatterie) {
        this.preisProBatterie = preisProBatterie;
    }

    public int getMinKapazitaet(){
        return minKapazitaet;
    }

    public void setMinKapazitaet(int minKapazitaet) {
        this.minKapazitaet = minKapazitaet;
    }

    public int getChargingCycles(){
        return chargingCycles;
    }
}
