package com.example.Adventure.controller;

import com.example.Adventure.domain.Events;
import com.example.Adventure.domain.Products;
import com.example.Adventure.service.EventService;
import com.example.Adventure.service.ProductsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @Autowired
    private HttpSession session;


    /**
     * @param model
     * @return
     */
    @GetMapping("/products")
    public String top(Model model) {
        List<Products> productsList = productsService.findAll();
        List<Events> eventsList = eventService.findAll();

        Map<String, List<Products>> productsGroupedByRegion = productsList.stream()
                .collect(Collectors.groupingBy(product -> product.getRegionName() != null ? product.getRegionName() : "Unknown"));


        model.addAttribute("productsByRegion", productsGroupedByRegion);
        model.addAttribute("eventsList", eventsList);
        return "top";
    }
    @GetMapping("/auto-complete")
    @ResponseBody
    public List<String> autoComplete(@RequestParam("name") String name) {
        if (name.length() < 2) {
            // 入力が2文字未満の場合は空のリストを返す
            return Collections.emptyList();
        }

        List<Products> productsList = productsService.searchProducts(name);
        List<String> productNames = productsService.convertProductsToProductNames(productsList);
        return productNames;
    }

    @PostMapping("/search")
    public String searchResult(@RequestParam("name") String name, Model model) {
        if (name == null || name.isEmpty()) {
            model.addAttribute("noMerchandise", "検索内容を入力してください");
            return "top";
        }

        List<Products> searchProductsList = productsService.searchProducts(name);
        if (searchProductsList.isEmpty()) {
            model.addAttribute("noMerchandise", "当てはまる商品がありません");
        }

        List<Events> eventsList = eventService.findAll();

        Map<String, List<Products>> searchProductsGroupedByRegion = searchProductsList.stream()
                .collect(Collectors.groupingBy(product -> {
                    String regionName = product.getRegionName();
                    return regionName != null ? regionName : "";
                }));


        model.addAttribute("productsByRegion", searchProductsGroupedByRegion);
        model.addAttribute("eventsList", eventsList);

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

//    Map<String, List<Products>> productsGroupedByRegion = productsList.stream()
//            .collect(Collectors.groupingBy(product -> Objects.requireNonNullElse(product.getRegionName(), "Unknown")));

}

