package hackathon.demo.model;

public class Answer {
    public String kunde;
    public double preis;
    public Battery battery;

    public Answer(String kunde, double preis, Battery battery){
        this.kunde = kunde;
        this.preis = preis;
        this.battery = battery;
    }
}