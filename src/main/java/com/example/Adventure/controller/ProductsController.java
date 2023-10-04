package com.example.Adventure.controller;

import com.example.Adventure.domain.Products;
import com.example.Adventure.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/top")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @GetMapping("/products")
    public String top(Model model){
        List<Products> productsList = productsService.findAll();
        model.addAttribute("productsList",productsList);
        return "top";

    }

    @GetMapping("/detail/{product_id}")
    public String detail(@PathVariable("product_id") Integer product_id,Model model) {
        Products products = productsService.load(product_id);
        return "merchandise-detail";

    }

//    @GetMapping("/save/{product_id}")
//    public String save(@PathVariable("product_id")Integer product_id,Model model) {
//        Products products = productsService.save(product_id);
//        return ":/redirect/top";
//    }

}
