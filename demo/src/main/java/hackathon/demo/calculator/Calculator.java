package hackathon.demo.calculator;

import hackathon.demo.model.*;

import java.io.FileNotFoundException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class Entry{
    public long time;
    public int wirkungsgrad;
    public int chargingCycles;

    public Entry(long time, int wirkungsgrad, int chargingCycles) {
        this.time = time;
        this.wirkungsgrad = wirkungsgrad;
        this.chargingCycles = chargingCycles;
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
        return new Answer(viableCustomers.getFirst().getName(), viableCustomers.getFirst().getPreisProBatterie()*0.9);
    }

    private static boolean wirkungsgradOk(Customer customer, Battery b, List<Knowledge> history){
        //Check whether Wirkungsgrad matches
        //Create history function
        var historyOfWirkungsgrad = new Entry[history.size()+2];
        historyOfWirkungsgrad[0] = new Entry(0, 100, 0);
        for(int i = 1; i < historyOfWirkungsgrad.length-1; i++){
            historyOfWirkungsgrad[i] = new Entry(history.get(i).getTime(), history.get(i).getWirkungsgrad(), history.get(i).getAnzahlGeladen());
        }
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(history.get(historyOfWirkungsgrad.length-1).getTime()+1, 0, Integer.MAX_VALUE);
        System.out.println("Prüfe WirkungsgradAnforderung:");
        var byTimeOk = Calculator.approveValuesForBorderValueByTime(historyOfWirkungsgrad, b, customer.getMinWirkungsgrad(), customer.getErwLebensdauer());
        var byCyclesOk = Calculator.approveValuesForBorderValueByChargingCycles(historyOfWirkungsgrad, b, customer.getMinWirkungsgrad(), customer.getChargingCycles());
        System.out.println("byTimeOk: " + byTimeOk);
        System.out.println("byCyclesOk: " + byCyclesOk);
        return byTimeOk && byCyclesOk;
    }

    private static boolean kapazitaetOk(Customer customer, Battery b, List<Knowledge> history){
        //Check whether Wirkungsgrad matches
        //Create history function
        var historyOfKapazitaet = new Entry[history.size()+2];
        historyOfKapazitaet[0] = new Entry(0, 100, 0);
        for(int i = 1; i < historyOfKapazitaet.length-1; i++){
            historyOfKapazitaet[i] = new Entry(history.get(i).getTime(), history.get(i).getWirkungsgrad(), history.get(i).getAnzahlGeladen());
        }
        historyOfKapazitaet[historyOfKapazitaet.length-1] = new Entry(history.get(historyOfKapazitaet.length-1).getTime()+1, 0, Integer.MAX_VALUE);
        System.out.println("Prüfe Kapazitaetanforderung:");
        var byTimeOk = Calculator.approveValuesForBorderValueByTime(historyOfKapazitaet, b, customer.getMinKapazitaet(), customer.getErwLebensdauer());
        var byCyclesOk = Calculator.approveValuesForBorderValueByChargingCycles(historyOfKapazitaet, b, customer.getMinKapazitaet(), customer.getChargingCycles());
        System.out.println("byTimeOk: " + byTimeOk);
        System.out.println("byCyclesOk: " + byCyclesOk);
        return byTimeOk && byCyclesOk;
    }

    private static boolean approveValuesForBorderValueByTime(Entry[] historyOfValues, Battery b, int borderValue, long erwLebensdauer){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfValues.length+4];
        historyOfValues[0] = new Entry(0, 100, 0);
        historyOfValues[historyOfValues.length-1] = new Entry(historyOfValues[historyOfValues.length-1].getTime()+1, 0, 0);
        for(int i = 1; i < historyOfValues.length; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry((historyOfValues[i-1].getTime() + historyOfValues[i].getTime()) / 2, (historyOfValues[i-1].getWirkungsgrad() + historyOfValues[i].getWirkungsgrad()) / 2, Integer.MAX_VALUE);
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
                if(v.getWirkungsgrad() < borderValue){
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
                if(v.getWirkungsgrad() < borderValue){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getWirkungsgrad()-lastEntry.getWirkungsgrad())/(v.getTime()-lastEntry.getTime());
                    if(borderValue > m*lastEntry.getWirkungsgrad()){
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

    private static boolean approveValuesForBorderValueByChargingCycles(Entry[] historyOfValues, Battery b, int borderValue, long chargingCycles){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfValues.length+4];
        historyOfValues[0] = new Entry(0, 100, 0);
        historyOfValues[historyOfValues.length-1] = new Entry(historyOfValues[historyOfValues.length-1].getTime()+1, 0, Integer.MAX_VALUE);
        for(int i = 1; i < historyOfValues.length; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry(
                    (historyOfValues[i-1].getTime() + historyOfValues[i].getTime()) / 2,
                    (historyOfValues[i-1].getWirkungsgrad() + historyOfValues[i].getWirkungsgrad()) / 2,
                    (historyOfValues[i-1].getChargingCycles() + historyOfValues[i].getChargingCycles()) / 2);
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
                if(v.getWirkungsgrad() < borderValue){
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
                if(v.getWirkungsgrad() < borderValue){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getWirkungsgrad()-lastEntry.getWirkungsgrad())/(v.getChargingCycles()-lastEntry.getChargingCycles());
                    if(borderValue > m*lastEntry.getWirkungsgrad()){
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
        //TODO
        return new Battery(pass, "test", 50, 7000, 2313, new Date().getTime());
    }

    public static List<Customer> getCustomers() {
        //TODO
        return new ArrayList<Customer>();
    }

    public static List<Knowledge> getHistory(String hersteller){
        //TODO
        return new ArrayList<Knowledge>();
    }
}
