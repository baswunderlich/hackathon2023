package hackathon.demo.calculator;

import hackathon.demo.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;

class Entry{
    public long time;
    public int wirkungsgrad;
    public int capacity;
    public int chargingCycles;

    public Entry(long time, int wirkungsgrad, int chargingCycles, int capacity) {
        this.time = time;
        this.wirkungsgrad = wirkungsgrad;
        this.chargingCycles = chargingCycles;
        this.capacity = capacity;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getWirkungsgrad() {
        return wirkungsgrad;
    }

    public void setWirkungsgrad(int wirkungsgrad) {
        this.wirkungsgrad = wirkungsgrad;
    }

    public int getChargingCycles() {
        return chargingCycles;
    }

    public void setChargingCycles(int chargingCycles) {
        this.chargingCycles = chargingCycles;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

public class Calculator {
    public static Answer getAnswerForBatteryPass(String pass) throws FileNotFoundException {
        var battery = Calculator.getBattery(pass);
        if(battery == null){
            throw new FileNotFoundException();
        }
        var customers = Calculator.getCustomers();
        var history = Calculator.getHistory(battery.getHersteller());
        var viableCustomers = new LinkedList<Customer>();

        for(var c : customers){
            System.out.println("_____________________\n+" +
                    "Prüfung ob Batterie von " + battery.getHersteller() +
                    "\nein Match für " + c.getName());
            if(wirkungsgradOk(c, battery, history) && kapazitaetOk(c, battery, history)){
                viableCustomers.add(c);
            }
        }

        Collections.sort(viableCustomers);

        /*
        times 0.9 to make money
         */
        if(viableCustomers.size() < 1){
            System.out.println("Kein Kunde gefunden");
            return null;
        }
        return new Answer(viableCustomers.getFirst().getName(), viableCustomers.getFirst().getPreisProBatterie()*0.9);
    }

    private static boolean wirkungsgradOk(Customer customer, Battery b, List<Knowledge> history){
        //Check whether Wirkungsgrad matches
        //Create history function
        var historyOfWirkungsgrad = new Entry[history.size()+2];
        historyOfWirkungsgrad[0] = new Entry(0, 100, 0, 0);
        for(int i = 1; i < historyOfWirkungsgrad.length-1; i++){
            historyOfWirkungsgrad[i] = new Entry(history.get(i-1).getTime(), history.get(i-1).getWirkungsgrad(), history.get(i-1).getAnzahlGeladen(), 0);
        }
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(history.get(history.size()-1).getTime()+1, 0, Integer.MAX_VALUE, 0);
        for(var h: history)
            System.out.println("history: " + h);
        for(var h: historyOfWirkungsgrad)
            System.out.println("-hvw: " + h);

        System.out.println("Prüfe WirkungsgradAnforderung:");
        var byTimeOk = Calculator.approveValuesWirkungsgradByTime(historyOfWirkungsgrad, b, customer.getMinWirkungsgrad(), customer.getErwLebensdauer());
        var byCyclesOk = Calculator.approveValuesOfWirkungsgradByChargingCycles(historyOfWirkungsgrad, b, customer.getMinWirkungsgrad(), customer.getChargingCycles());
        System.out.println("byTimeOk: " + byTimeOk);
        System.out.println("byCyclesOk: " + byCyclesOk);
        return byTimeOk && byCyclesOk;
    }

    private static boolean kapazitaetOk(Customer customer, Battery b, List<Knowledge> history){
        //Check whether Wirkungsgrad matches
        //Create history function
        var historyOfKapazitaet = new Entry[history.size()+2];
        historyOfKapazitaet[0] = new Entry(0, 100, 0, 100);
        for(int i = 1; i < historyOfKapazitaet.length-1; i++){
            historyOfKapazitaet[i] = new Entry(history.get(i-1).getTime(), history.get(i-1).getWirkungsgrad(), history.get(i-1).getAnzahlGeladen(), history.get(i-1).getCapacity());
        }
        historyOfKapazitaet[historyOfKapazitaet.length-1] = new Entry(history.get(history.size()-1).getTime()+1, 0, Integer.MAX_VALUE, 0);
        System.out.println("Prüfe Kapazitaetanforderung:");
        var byTimeOk = Calculator.approveValuesForCapacityByTime(historyOfKapazitaet, b, customer.getMinKapazitaet(), customer.getErwLebensdauer());
        var byCyclesOk = Calculator.approveValuesForCapacityByChargingCycles(historyOfKapazitaet, b, customer.getMinKapazitaet(), customer.getChargingCycles());
        System.out.println("byTimeOk: " + byTimeOk);
        System.out.println("byCyclesOk: " + byCyclesOk);
        return byTimeOk && byCyclesOk;
    }
    private static boolean approveValuesForCapacityByTime(Entry[] historyOfValues, Battery b, int capacity, long erwLebensdauer){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfValues.length+1];
        softendHistoryOfWirkungsgrad[0] = new Entry(0, 100, 0, b.getWerkKapazitaet());
        softendHistoryOfWirkungsgrad[softendHistoryOfWirkungsgrad.length-1] = new Entry(historyOfValues[historyOfValues.length-1].getTime()+1, 0, 0, 0);
        System.out.println("historyOfValues " + historyOfValues.length);
        System.out.println("softendHistoryOfWirkungsgrad " + softendHistoryOfWirkungsgrad.length);
        for(int i = 1; i < softendHistoryOfWirkungsgrad.length-1; i++){
            System.out.println("i: " + i);
            softendHistoryOfWirkungsgrad[i] = new Entry(
                    (int)(historyOfValues[i-1].getTime() + historyOfValues[i].getTime()) / 2,
                    0,
                    Integer.MAX_VALUE,
                    (int)(historyOfValues[i-1].getCapacity() + historyOfValues[i].getCapacity()) / 2 );
        }
        for(var h: softendHistoryOfWirkungsgrad)
            System.out.println("s: " + h);

        //Start comparing values with necessary wirkungsgrad
        Entry lastEntry = null;
        for(var v : softendHistoryOfWirkungsgrad){
            if(v.getTime()*1000 < LocalDateTime.now().getSecond()){
            /*
            We dont need to look at data before the actual current situation
             */
                continue;
            }
            if(v.getTime() <= v.getTime()+erwLebensdauer){
                /*
                Wenn der Zeitpunkt der Messung vor der erwLebensdauer Ende liegt
                    => ÜBERPRÜFEN
                */
                if(v.getWirkungsgrad() < capacity){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     */
                    return false;
                }
            }else{
                /*
                Wenn der Zeitpunkt danach liegt und trotzdem über dem noetigen Wirkungsgrad, muss es trotzdem passen.
                Sonst überprüfen wir mithile einer Geraden zwischen den zwei Datenpnukten
                */
                if(v.getWirkungsgrad() < capacity){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getWirkungsgrad()-lastEntry.getWirkungsgrad())/(v.getTime()-lastEntry.getTime());
                    if(capacity > m*lastEntry.getWirkungsgrad()){
                        /*
                        Der Wirkungsgrad auf der Geraden reicht nicht aus
                         */
                        return false;
                    }
                    return true;
                }
            }
            /*
            Muss nur gespeichert werden, wenn Der Punkt darüber lag, sonst eh egal
             */
            lastEntry = v;
        }
        return true;
    }

    private static boolean approveValuesForCapacityByChargingCycles(Entry[] historyOfValues, Battery b, int capacity, long chargingCycles){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfValues.length+1];
        softendHistoryOfWirkungsgrad[0] = new Entry(0, 100, 0, b.getWerkKapazitaet());
        softendHistoryOfWirkungsgrad[softendHistoryOfWirkungsgrad.length-1] = new Entry(historyOfValues[historyOfValues.length-1].getTime()+1, 0, Integer.MAX_VALUE, 0);
        for(int i = 1; i < softendHistoryOfWirkungsgrad.length-1; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry(
                    (historyOfValues[i-1].getTime() + historyOfValues[i].getTime()) / 2,
                    0,
                    (historyOfValues[i-1].getChargingCycles() + historyOfValues[i].getChargingCycles()) / 2,
                    (historyOfValues[i-1].getCapacity() + historyOfValues[i].getCapacity()) / 2);
        }

        //Start comparing values with necessary wirkungsgrad
        Entry lastEntry = null;
        for(var v : softendHistoryOfWirkungsgrad){
            if(v.getChargingCycles() < b.getChargingCycles()){
            /*
            We dont need to look at data before the actual current situation
             */
                continue;
            }
            if(v.getChargingCycles() < v.getChargingCycles()+chargingCycles){
                /*
                Wenn Menge an Zyklen vor der erwZyklen Ende liegt
                    => ÜBERPRÜFEN
                */
                if(v.getWirkungsgrad() < capacity){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     */
                    return false;
                }
            }else{
                /*
                Wenn der Zeitpunkt danach liegt und trotzdem über dem noetigen Wirkungsgrad, muss es trotzdem passen.
                Sonst überprüfen wir mithile einer Geraden zwischen den zwei Datenpnukten
                */
                if(v.getWirkungsgrad() < capacity){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getWirkungsgrad()-lastEntry.getWirkungsgrad())/(v.getChargingCycles()-lastEntry.getChargingCycles());
                    if(capacity > m*lastEntry.getWirkungsgrad()){
                        /*
                        Der Wirkungsgrad auf der Geraden reicht nicht aus
                         */
                        return false;
                    }
                    return true;
                }
            }
            /*
            Muss nur gespeichert werden, wenn Der Punkt darüber lag, sonst eh egal
             */
            lastEntry = v;
        }
        return true;
    }

    private static boolean approveValuesWirkungsgradByTime(Entry[] historyOfValues, Battery b, int wirkungsgrad, long erwLebensdauer){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfValues.length+1];
        softendHistoryOfWirkungsgrad[0] = new Entry(0, 100, 0, b.getWerkKapazitaet());
        softendHistoryOfWirkungsgrad[softendHistoryOfWirkungsgrad.length-1] = new Entry(historyOfValues[historyOfValues.length-1].getTime()+1, 0, 0, 0);
        System.out.println("historyOfValues " + historyOfValues.length);
        System.out.println("softendHistoryOfWirkungsgrad " + softendHistoryOfWirkungsgrad.length);
        for(var h: softendHistoryOfWirkungsgrad)
            System.out.println("s: " + h);
        for(int i = 1; i < softendHistoryOfWirkungsgrad.length-1; i++){
            System.out.println("i: " + i);
            softendHistoryOfWirkungsgrad[i] = new Entry((int)(historyOfValues[i-1].getTime() + historyOfValues[i].getTime()) / 2, (int)(historyOfValues[i-1].getWirkungsgrad() + historyOfValues[i].getWirkungsgrad()) / 2, Integer.MAX_VALUE, 0);
        }

        //Start comparing values with necessary wirkungsgrad
        Entry lastEntry = null;
        for(var v : softendHistoryOfWirkungsgrad){
            if(v.getTime()*1000 < LocalDateTime.now().getSecond()){
            /*
            We dont need to look at data before the actual current situation
             */
                continue;
            }
            if(v.getTime() <= v.getTime()+erwLebensdauer){
                /*
                Wenn der Zeitpunkt der Messung vor der erwLebensdauer Ende liegt
                    => ÜBERPRÜFEN
                */
                if(v.getWirkungsgrad() < wirkungsgrad){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     */
                    return false;
                }
            }else{
                /*
                Wenn der Zeitpunkt danach liegt und trotzdem über dem noetigen Wirkungsgrad, muss es trotzdem passen.
                Sonst überprüfen wir mithile einer Geraden zwischen den zwei Datenpnukten
                */
                if(v.getWirkungsgrad() < wirkungsgrad){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getWirkungsgrad()-lastEntry.getWirkungsgrad())/(v.getTime()-lastEntry.getTime());
                    if(wirkungsgrad > m*lastEntry.getWirkungsgrad()){
                        /*
                        Der Wirkungsgrad auf der Geraden reicht nicht aus
                         */
                        return false;
                    }
                    return true;
                }
            }
            /*
            Muss nur gespeichert werden, wenn Der Punkt darüber lag, sonst eh egal
             */
            lastEntry = v;
        }
        return true;
    }

    private static boolean approveValuesOfWirkungsgradByChargingCycles(Entry[] historyOfValues, Battery b, int wirkungsgrad, long chargingCycles){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfValues.length+1];
        softendHistoryOfWirkungsgrad[0] = new Entry(0, 100, 0 , b.getWerkKapazitaet());
        softendHistoryOfWirkungsgrad[softendHistoryOfWirkungsgrad.length-1] = new Entry(historyOfValues[historyOfValues.length-1].getTime()+1, 0, Integer.MAX_VALUE, 0);
        for(int i = 1; i < softendHistoryOfWirkungsgrad.length-1; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry(
                    (historyOfValues[i-1].getTime() + historyOfValues[i].getTime()) / 2,
                    (historyOfValues[i-1].getWirkungsgrad() + historyOfValues[i].getWirkungsgrad()) / 2,
                    (historyOfValues[i-1].getChargingCycles() + historyOfValues[i].getChargingCycles()) / 2,
                    0);
        }

        //Start comparing values with necessary wirkungsgrad
        Entry lastEntry = null;
        for(var v : softendHistoryOfWirkungsgrad){
            if(v.getChargingCycles() < b.getChargingCycles()){
            /*
            We dont need to look at data before the actual current situation
             */
                continue;
            }
            if(v.getChargingCycles() < v.getChargingCycles()+chargingCycles){
                /*
                Wenn Menge an Zyklen vor der erwZyklen Ende liegt
                    => ÜBERPRÜFEN
                */
                if(v.getWirkungsgrad() < wirkungsgrad){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     */
                    return false;
                }
            }else{
                /*
                Wenn der Zeitpunkt danach liegt und trotzdem über dem noetigen Wirkungsgrad, muss es trotzdem passen.
                Sonst überprüfen wir mithile einer Geraden zwischen den zwei Datenpnukten
                */
                if(v.getWirkungsgrad() < wirkungsgrad){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getWirkungsgrad()-lastEntry.getWirkungsgrad())/(v.getChargingCycles()-lastEntry.getChargingCycles());
                    if(wirkungsgrad > m*lastEntry.getWirkungsgrad()){
                        /*
                        Der Wirkungsgrad auf der Geraden reicht nicht aus
                         */
                        return false;
                    }
                    return true;
                }
            }
            /*
            Muss nur gespeichert werden, wenn Der Punkt darüber lag, sonst eh egal
             */
            lastEntry = v;
        }
        return true;
    }

    public static Battery getBattery(String pass) throws FileNotFoundException {
        JSONArray listOfBatteriesAsJson = null;
        try {
            listOfBatteriesAsJson = (JSONArray) new JSONParser().parse(new FileReader("./src/main/resources/batteryPass.json"));
       }catch(Exception e){
            e.printStackTrace();
       }
        assert listOfBatteriesAsJson != null;
        var listOfBatteries = new LinkedList<Battery>();
        for (Object listOfCard : listOfBatteriesAsJson) {
            JSONObject obj = (JSONObject) listOfCard;
            listOfBatteries.add(
                new Battery(
                        (String) obj.get("id"),
                        (String) obj.get("hersteller"),
                        ((Long) obj.get("wirkungsgrad")).intValue(),
                        ((Long) obj.get("capacity")).intValue(),
                        ((Long) obj.get("chargingCycles")).intValue(),
                        (long) obj.get("herstellDatum"),
                        ((Long) obj.get("werkCapacity")).intValue()
                )
            );
        }
        for(var b : listOfBatteries){
            if(b.getId().equals(pass)){
                return b;
            }
        }
        return null;
    }

    public static List<Customer> getCustomers() {
        JSONArray listOfCustomersAsJson = null;
        try {
            listOfCustomersAsJson = (JSONArray) new JSONParser().parse(new FileReader("./src/main/resources/customers.json"));
        }catch(Exception e){
            e.printStackTrace();
        }
        assert listOfCustomersAsJson != null;
        var listOfCustomers = new LinkedList<Customer>();
        for (Object listOfCard : listOfCustomersAsJson) {
            JSONObject obj = (JSONObject) listOfCard;
            listOfCustomers.add(
                    new Customer(
                            (String) obj.get("customerName"),
                            ((Long) obj.get("minWirkungsgrad")).intValue(),
                            (long) obj.get("erwarteteLebensdauer"),
                            ((Long) obj.get("preisProBatterie")).intValue(),
                            ((Long) obj.get("chargingCycles")).intValue(),
                            ((Long) obj.get("minCapacity")).intValue()
                    )
            );
        }
        return listOfCustomers;
    }

    public static List<Knowledge> getHistory(String hersteller){
        JSONArray listOfKnowledgeAsJson = null;
        try {
            listOfKnowledgeAsJson = (JSONArray) new JSONParser().parse(new FileReader("./src/main/resources/knowledge.json"));
        }catch(Exception e){
            e.printStackTrace();
        }
        assert listOfKnowledgeAsJson != null;
        var listOfKnowledge = new LinkedList<Knowledge>();
        for (Object listOfCard : listOfKnowledgeAsJson) {
            JSONObject obj = (JSONObject) listOfCard;
            listOfKnowledge.add(
                    new Knowledge(
                            (String) obj.get("hersteller"),
                            ((Long) obj.get("wirkungsgrad")).intValue(),
                            (long) obj.get("lebensdauerNachMessung"),
                            ((Long) obj.get("lebensdauerGesamt")).intValue(),
                            ((Long) obj.get("lebensdauerBisMessung")).intValue(),
                            ((Long) obj.get("chargingCycles")).intValue(),
                            ((Long) obj.get("capacity")).intValue()
                    )
            );
        }
        return listOfKnowledge;
    }
}
