package com.example.Adventure.controller;

import com.example.Adventure.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("")
public class AutoCompleteController {

    @Autowired
    private ProductsService productsService;

    /**
     * @param name 検索で使用する文字列
     * @return 検索結果のJSON形式List
     */
    @GetMapping("/auto-complete")
    @ResponseBody
    public List<String> autoComplete(@RequestParam("name") String name) {
        if (name.length() < 2) {
            // 入力が2文字未満の場合は空のリストを返す
            return Collections.emptyList();
        }
        return productsService.getSearchProduct(name);
    }
}


