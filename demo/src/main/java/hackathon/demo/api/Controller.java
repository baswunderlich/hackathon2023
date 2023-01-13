package hackathon.demo.api;

import hackathon.demo.calculator.Calculator;
import hackathon.demo.model.Answer;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class Controller {
    public Answer getAnswerForBatteryPass(String pass){
        try {
            return Calculator.getAnswerForBatteryPass(pass);
        } catch (FileNotFoundException e) {
            System.out.println("Batterypass not found")M;
            return null;

        }
    }
}