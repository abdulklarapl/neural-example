package io.abdulklarapl.neural.example.controller;

import io.abdulklarapl.neural.example.domain.digit.DigitNetwork;
import io.abdulklarapl.neural.example.domain.digit.Request;
import io.abdulklarapl.neural.example.domain.digit.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
