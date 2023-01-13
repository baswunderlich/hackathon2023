package hackathon.demo.calculator;

import hackathon.demo.model.Answer;
import hackathon.demo.model.Battery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Calculator {
    public static Answer getAnswerForBatteryPass(String pass) throws FileNotFoundException {
        Calculator.getBattery(pass);
        return new Answer("niemand", 0);
    }

    public static Battery getBattery(String pass) throws FileNotFoundException {
        return new Battery("test", 50, 7000, 2313, new Date());
    }
}
