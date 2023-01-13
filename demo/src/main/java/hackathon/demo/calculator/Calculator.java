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
            if(wirkungsgradOk(c, battery, history) && kapazitaetOk(c, battery, history)){
                viableCustomers.add(c);
            }
        }

        Collections.sort(viableCustomers);

        return new Answer(viableCustomers.getFirst().getName(), viableCustomers.getFirst().getPreisProBatterie());
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
        return Calculator.approveValuesForBorderValueByTime(historyOfWirkungsgrad, b, customer.getMinWirkungsgrad(), customer.getErwLebensdauer())
                && Calculator.approveValuesForBorderValueByChargingCycles(historyOfWirkungsgrad, b, customer.getMinWirkungsgrad(), customer.getChargingCycles());
    }

    private static boolean kapazitaetOk(Customer customer, Battery b, List<Knowledge> history){
        //Check whether Wirkungsgrad matches
        //Create history function
        var historyOfWirkungsgrad = new Entry[history.size()+2];
        historyOfWirkungsgrad[0] = new Entry(0, 100, 0);
        for(int i = 1; i < historyOfWirkungsgrad.length-1; i++){
            historyOfWirkungsgrad[i] = new Entry(history.get(i).getTime(), history.get(i).getWirkungsgrad(), history.get(i).getAnzahlGeladen());
        }
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(history.get(historyOfWirkungsgrad.length-1).getTime()+1, 0, Integer.MAX_VALUE);
        return Calculator.approveValuesForBorderValueByTime(historyOfWirkungsgrad, b, customer.getMinKapazitaet(), customer.getErwLebensdauer())
                && Calculator.approveValuesForBorderValueByChargingCycles(historyOfWirkungsgrad, b, customer.getMinKapazitaet(), customer.getChargingCycles());
    }

    private static boolean approveValuesForBorderValueByTime(Entry[] historyOfWirkungsgrad, Battery b, int borderValue, long erwLebensdauer){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfWirkungsgrad.length+4];
        historyOfWirkungsgrad[0] = new Entry(0, 100, 0);
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(historyOfWirkungsgrad[historyOfWirkungsgrad.length-1].getTime()+1, 0, 0);
        for(int i = 1; i < historyOfWirkungsgrad.length; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry((historyOfWirkungsgrad[i-1].getTime() + historyOfWirkungsgrad[i].getTime()) / 2, (historyOfWirkungsgrad[i-1].getWirkungsgrad() + historyOfWirkungsgrad[i].getWirkungsgrad()) / 2, Integer.MAX_VALUE);
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

    private static boolean approveValuesForBorderValueByChargingCycles(Entry[] historyOfWirkungsgrad, Battery b, int borderValue, long chargingCycles){
        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[historyOfWirkungsgrad.length+4];
        historyOfWirkungsgrad[0] = new Entry(0, 100, 0);
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(historyOfWirkungsgrad[historyOfWirkungsgrad.length-1].getTime()+1, 0, Integer.MAX_VALUE);
        for(int i = 1; i < historyOfWirkungsgrad.length; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry(
                    (historyOfWirkungsgrad[i-1].getTime() + historyOfWirkungsgrad[i].getTime()) / 2,
                    (historyOfWirkungsgrad[i-1].getWirkungsgrad() + historyOfWirkungsgrad[i].getWirkungsgrad()) / 2,
                    (historyOfWirkungsgrad[i-1].getChargingCycles() + historyOfWirkungsgrad[i].getChargingCycles()) / 2);
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
        return new Battery(pass, "test", 50, 7000, 2313, new Date());
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
