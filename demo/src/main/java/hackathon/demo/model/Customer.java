package hackathon.demo.model;

public class Customer {
    String name;
    int minWirkungsgrad;
    long erwLebensdauer; //in ms;
    int preisProBatterie;

    public Customer(String name, int minWirkungsgrad, long erwLebensdauer, int preis){
        this.name = name;
        this.minWirkungsgrad = minWirkungsgrad;
        this.erwLebensdauer = erwLebensdauer;
        this.preisProBatterie = preis;
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
}
