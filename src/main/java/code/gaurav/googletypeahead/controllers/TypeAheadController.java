package code.gaurav.googletypeahead.controllers;


import code.gaurav.googletypeahead.services.TypeAheadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1_2/typeahead")
public class TypeAheadController {

    @Autowired
    private TypeAheadService typeAheadService;

    @GetMapping("/{pattern}")
    public List<String> getAllStringStartingWithPattern(@PathVariable("pattern") String pattern) throws Exception {
        try {
            return typeAheadService.findAllStringForPattern(pattern);
        }catch (Exception e){
            throw new Exception("Internal Server Error");
        }
    }

    @PostMapping("/{pattern}")
    public ResponseEntity<HttpStatus> saveThePattern(@PathVariable("pattern") String pattern){
        try {
            typeAheadService.savePattern(pattern);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/timeDecay/{time}")
    public ResponseEntity<HttpStatus> decayFrequency(@PathVariable("time") Double time){
        try{
            typeAheadService.decayFrequency(time);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updatefrequency")
    public ResponseEntity<HttpStatus> updateAllFrequency(){
        try {
            typeAheadService.updateAllFrequency();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
