package t1.test;

import openapi.api.TestApi;
import openapi.model.InputString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/test")
public class MainController implements TestApi {

    MainService mainService;

    public MainController(MainService mainService){
        this.mainService = mainService;
    }

    @Override
    @PostMapping
    public ResponseEntity<String> findCharsFrequency(@RequestBody @Valid InputString s){
        String result = mainService.getCharsFrequency(s.getInputString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
