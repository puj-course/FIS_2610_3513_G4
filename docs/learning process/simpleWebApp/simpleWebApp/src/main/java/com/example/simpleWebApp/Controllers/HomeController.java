package com.example.simpleWebApp.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String greet(){
        System.out.println("metodo greet");
        return "Bienvenido a mi puta pagina, JASJASJAJ";
    }

    @RequestMapping("/about")
    public String about(){
        return "tu mami";
    }
}
