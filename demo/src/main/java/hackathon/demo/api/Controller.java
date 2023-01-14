package hackathon.demo.api;

import hackathon.demo.calculator.Calculator;
import hackathon.demo.model.Answer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@CrossOrigin(origins = "*")
@RestController
public class Controller {
    @GetMapping("answer")
    @CrossOrigin(origins = "*")
    public Answer getAnswerForBatteryPass(String pass){
        try {
            return Calculator.getAnswerForBatteryPass(pass);
        } catch (FileNotFoundException e) {
            return new Answer("Keinen solchen Pass gefunden", -1, null);
        }
    }
}