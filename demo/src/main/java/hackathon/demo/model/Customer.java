package hackathon.demo.model;

public class Customer {
    String name;
    int minWirkungsgrad;
    int preisProBatterie;

    public Customer(String name, int minWirkungsgrad, int preis){
        this.name = name;
        this.minWirkungsgrad = minWirkungsgrad;
        this.preisProBatterie = preis;
    }
}
