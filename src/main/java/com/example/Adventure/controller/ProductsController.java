package com.example.Adventure.controller;

import com.example.Adventure.domain.Events;
import com.example.Adventure.domain.Products;
import com.example.Adventure.service.EventService;
import com.example.Adventure.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/top")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private EventService eventService;


    /**
     * @param model
     * @return
     */
    @GetMapping("/products")
    public String top(Model model){
        List<Products> productsList = productsService.findAll();
        List<Events> eventsList = eventService.findAll();

        // 地域ごとに商品をグループ化
        Map<String, List<Products>> productsGroupedByRegion = productsList.stream()
                .collect(Collectors.groupingBy(Products::getRegionName));

        model.addAttribute("productsByRegion", productsGroupedByRegion);
        model.addAttribute("eventsList",eventsList);
        return "top";
    }

    @GetMapping("/showDetail/{product_id}")
    public String detail(@PathVariable("product_id") Integer product_id, Model model) {
        Products products = productsService.load(product_id);
        model.addAttribute("product", products);
        return "product-detail";
    }
    @GetMapping("/back")
    public String back() {
        return "redirect:/top/products";
    }


}
