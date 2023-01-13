package hackathon.demo.api;

import hackathon.demo.calculator.Calculator;
import hackathon.demo.model.Answer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class Controller {
    @GetMapping("answer")
    public Answer getAnswerForBatteryPass(String pass){
        return new Answer("Lieblings AG", 10);
    }
}