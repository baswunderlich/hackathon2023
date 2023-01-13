package hackathon.demo.model;

public class Customer implements Comparable<Customer>{
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

    public long getErwLebensdauer() {
        return erwLebensdauer;
    }

    public int getPreisProBatterie() {
        return preisProBatterie;
    }

    public int getMinKapazitaet(){
        return minKapazitaet;
    }

    public int getChargingCycles(){
        return chargingCycles;
    }

    @Override
    public int compareTo(Customer o) {
        if(this.preisProBatterie > o.preisProBatterie){
            return 1;
        }else if(this.preisProBatterie == o.preisProBatterie){
            return 0;
        }else{
            return -1;
        }
    }
}
