package com.example.Adventure.controller;

import com.example.Adventure.service.OrderConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class OrderConfirmationController {
    @Autowired
    private OrderConfirmationService orderConfirmationService;
    @GetMapping("/order-confirmation")
    public String toOrderConfirmation() {
        return "order-confirmation";
    }
}
