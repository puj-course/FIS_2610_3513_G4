package com.ceiba.fashtoll.utilities;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class InitialThingsToDo {

    //pendiente
    @RequestMapping
    public String home() {
        return "bienvenido a fashtoll";
    }
}
