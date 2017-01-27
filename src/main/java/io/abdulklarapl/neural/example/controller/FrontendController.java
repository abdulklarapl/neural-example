package io.abdulklarapl.neural.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Patryk Szlagowski <patryksz@lsnova.pl>
 */
@Controller
public class FrontendController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
