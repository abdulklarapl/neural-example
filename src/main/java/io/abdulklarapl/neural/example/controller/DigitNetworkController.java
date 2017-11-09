package io.abdulklarapl.neural.example.controller;

import io.abdulklarapl.neural.example.domain.digit.DigitNetwork;
import io.abdulklarapl.neural.example.domain.digit.Request;
import io.abdulklarapl.neural.example.domain.digit.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Patryk Szlagowski <patryksz@lsnova.pl>
 */
@RestController
public class DigitNetworkController {

    @Autowired
    private DigitNetwork network;

    @RequestMapping(value = "api/digit/recognize", method = RequestMethod.POST)
    public ResponseEntity<Response> recognize(@RequestBody Request request) throws Exception {
        return new ResponseEntity<Response>(network.recognize(request), HttpStatus.OK);
    }

    @RequestMapping(value = "api/digit/train", method = RequestMethod.POST)
    public ResponseEntity train(@RequestBody Request request) throws Exception {
        network.train(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "api/digit/train", method = RequestMethod.GET)
    public ResponseEntity train() throws Exception {
        network.train("/data/train.csv");
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "api/digit/persist", method = RequestMethod.PUT)
    public ResponseEntity<String> persist() throws Exception {
        return new ResponseEntity<String>(network.persist(), HttpStatus.OK);
    }

    @RequestMapping(value = "api/digit/wakeup", method = RequestMethod.GET)
    public ResponseEntity wakeup(
            @RequestParam("timestamp") String timestamp
    ) throws Exception {
        network.wakeup(timestamp);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "api/digit", method = RequestMethod.GET)
    public ResponseEntity dump() {
        return new ResponseEntity(network.dumpSimple(), HttpStatus.OK);
    }
}
