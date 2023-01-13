package hackathon.demo.calculator;

import hackathon.demo.model.Answer;
import hackathon.demo.model.Battery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Calculator {
    public static Answer getAnswerForBatteryPass(String pass) throws FileNotFoundException {
        Calculator.getBattery(pass);
        return new Answer("niemand", 0);
    }

    public static Battery getBattery(String pass) throws FileNotFoundException {
        File f = new File("batteries/" + pass + ".json");
        FileReader reader = new FileReader(f);

    }
}
