package hackathon.demo.calculator;

import hackathon.demo.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class Entry{
    public long time;
    public int value;

    public Entry(long time, int value) {
        this.time = time;
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        var customersViable = new LinkedList<Customer>();

        return new Answer("niemand", 0);
    }

    private static boolean wirkungsgradOk(Customer customer, List<Knowledge> history){
        //Check whether Wirkungsgrad matches
        //Create history function
        var historyOfWirkungsgrad = new Entry[history.size()+2];
        historyOfWirkungsgrad[0] = new Entry(0, 100);
        for(int i = 1; i < historyOfWirkungsgrad.length-1; i++){
            historyOfWirkungsgrad[i] = new Entry(history.get(i).getTime(), history.get(i).getWirkungsgrad());
        }
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(history.get(historyOfWirkungsgrad.length-1).getTime()+1, 0);

        //Soften hard jumps by doing one smoothing iteration
        var softendHistoryOfWirkungsgrad = new Entry[history.size()+4];
        historyOfWirkungsgrad[0] = new Entry(0, 100);
        historyOfWirkungsgrad[historyOfWirkungsgrad.length-1] = new Entry(history.get(historyOfWirkungsgrad.length-1).getTime()+1, 0);
        for(int i = 1; i < historyOfWirkungsgrad.length; i++){
            softendHistoryOfWirkungsgrad[i] = new Entry((historyOfWirkungsgrad[i-1].getTime() + historyOfWirkungsgrad[i].getTime()) / 2, (historyOfWirkungsgrad[i-1].getValue() + historyOfWirkungsgrad[i].getValue()) / 2);
        }

        //Start comparing values with necessary wirkungsgrad
        Entry lastEntry = null;
        for(var v : softendHistoryOfWirkungsgrad){
            if(v.getTime() <= v.getTime()+customer.getErwLebensdauer()){
                /*
                Wenn der Zeitpunkt der Messung vor der erwLebensdauer Ende liegt
                    => ÜBERPRÜFEN
                */
                if(v.getValue() < customer.getMinWirkungsgrad()){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     */
                    return false;
                }
            }else{
                /*
                Wenn der Zeitpunkt danach liegt und trotzdem über dem noetigen Wirkungsgrad, muss es trotzdem passen.
                Sons überprüfen wir mithile einer Geraden zwischen den zwei Datenpnukten
                */
                if(v.getValue() < customer.getMinWirkungsgrad()){
                    /*
                    Der Wirkungsgrad reicht nicht aus
                     => Überprüfen anhand von Gerade zwischen den zwei Datenpunkten
                     */
                    var m = (v.getValue()-lastEntry.getValue())/(v.getTime()-lastEntry.getTime());

                    return false;
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
        return new Battery("test", 50, 7000, 2313, new Date());
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
