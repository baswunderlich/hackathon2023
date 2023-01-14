package hackathon.demo.calculator;

import hackathon.demo.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;

public class Calculator {
    public static Answer getAnswerForBatteryPass(String pass) throws FileNotFoundException {
        var battery = Calculator.getBattery(pass);
        if(battery == null){
            throw new FileNotFoundException();
        }
        var customers = Calculator.getCustomers();
        var history = Calculator.getHistory(battery.getHersteller());
        Collections.sort(history);
        var viableCustomers = new LinkedList<Customer>();

        for(var c : customers){
            System.out.println("_____________________\n+" +
                    "Prüfung ob Batterie von " + battery.getHersteller() +
                    "\nein Match für " + c.getName());
            if(wirkungsgradOk(c, battery, history)){
                System.out.println("Ein Match mit " + c.getName());
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

    public static boolean wirkungsgradOk(Customer c, Battery b, List<Knowledge> history){
        var ageBattery = new Date().getTime() - b.getHerstellungsdatum();
        Knowledge lastEntry = null;
        var batteryShouldLiveFor = ageBattery+c.getErwLebensdauer();
        for(var entry: history){
            System.out.println(entry.getAgeAtMessung() + " , " + entry.getWirkungsgrad());
            if(entry.getAgeAtMessung() > batteryShouldLiveFor){
                continue;
            }
            System.out.println("entry.getWirkungsgrad(): " +entry.getWirkungsgrad());
            System.out.println("c.getMinWirkungsgrad(): " +c.getMinWirkungsgrad());
            if(entry.getWirkungsgrad() < c.getMinWirkungsgrad()){
                /*
                Datenpunkt besitzt nicht ausreichenden Wirkungsgrad nötig
                 */
                System.out.println("Wirkungsgrad zu klein");
                return false;
            }
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
