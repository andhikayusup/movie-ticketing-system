package com.andhikayusup.moviex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "sayHi/")
public class HelloWorld {
    
    @GetMapping
    public String sayHi() {
        return "Hello, World!";
    }
}

