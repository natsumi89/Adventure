package com.example.Adventure.controller;

import com.example.Adventure.service.ShoppingCartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class ShoppingCartsController {
    @Autowired
    private ShoppingCartsService shoppingCartsService;

    @GetMapping("/show-shopping-cart")
    public String showShoppingCart() {
        return "shopping-cart";
    }

//    @PostMapping("/add-cart")
//    public String
}
