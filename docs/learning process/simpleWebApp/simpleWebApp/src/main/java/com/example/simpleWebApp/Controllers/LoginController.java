package com.example.simpleWebApp.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;

public class LoginController {
    @RequestMapping("/login")
    public String greet(){
        System.out.println("metodo greet");
        return "Login....";
    }
}
