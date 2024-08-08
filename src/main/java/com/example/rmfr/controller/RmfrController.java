package com.example.rmfr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class RmfrController {

    public String main(Principal principal) {
        return "/index.html";
    };

}
