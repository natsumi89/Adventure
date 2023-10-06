package com.example.Adventure.controller;

import com.example.Adventure.domain.Products;
import com.example.Adventure.service.ProductsService;
import com.example.Adventure.service.ShoppingCartsService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("")
public class ShoppingCartsController {
    @Autowired
    private ShoppingCartsService shoppingCartsService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private ServletContext application;
    @Autowired
    private HttpSession session;

    @GetMapping("/show-shopping-cart")
    public String index(Model model) {
        // アプリケーションスコープからの商品リスト取得は不要かと思いますので、この部分は削除。
        // カートの商品リストをセッションから取得
        List<Products> cartProductsList = (List<Products>) session.getAttribute("cartProductsList");
        if (cartProductsList == null) {
            cartProductsList = new LinkedList<>();
            session.setAttribute("cartProductsList", cartProductsList);
        } else {
            int totalPrice = calcTotalPrice(cartProductsList);
            session.setAttribute("totalPrice",totalPrice);
        }
        return "shopping-cart";

    }

    @PostMapping("/add-cart")
    public String addCart(Integer productId, Model model) {
        Products products = productsService.load(productId);
        List<Products> cartProductsList = (List<Products>) session.getAttribute("cartProductsList");

        if(cartProductsList == null) {
            cartProductsList = new LinkedList<>();
            session.setAttribute("cartProductsList", cartProductsList);
        }

        cartProductsList.add(products);
        session.setAttribute("cartProductsList", cartProductsList);

        return "redirect:/top/products";

    }

    @PostMapping("/delete-shopping-carts")
    public String deleteShoppingCart(Integer productId, HttpSession session) {
        shoppingCartsService.deleteShoppingCartByProductId(productId);

        // セッションからユーザーIDを取得
        Integer userId = (Integer) session.getAttribute("userId");

        // セッションのカートリストから該当の商品を削除
        List<Products> cartProductsList = (List<Products>) session.getAttribute("cartProductsList");
        if (cartProductsList != null) {
            cartProductsList.removeIf(product -> product.getProductId().equals(productId));
        }

        // セッションに更新したカートリストを設定
        session.setAttribute("cartProductsList", cartProductsList);

        return "redirect:/top/products";
    }




    private Integer calcTotalPrice(List<Products> productsList) {
        Integer totalPrice = 0;
        for(Products products : productsList) {
            totalPrice += products.getPrice();
        }
        return totalPrice;
    }
}
