package com.example.Adventure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class StampController {

    @GetMapping("/to-stamp-card")
    public String toStampCard() {
        return "stamp";
    }
}
