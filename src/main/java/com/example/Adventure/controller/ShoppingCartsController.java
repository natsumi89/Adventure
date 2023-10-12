package com.example.Adventure.controller;

import com.example.Adventure.domain.Products;
import com.example.Adventure.domain.ShoppingCartsDetail;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
        List<ShoppingCartsDetail> cartDetailsList;
        Integer userId = (Integer) session.getAttribute("userId");

        if(userId != null) {
            cartDetailsList = shoppingCartsService.findShoppingCartsDetailByUserId(userId);
        } else {
            cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
            if(cartDetailsList == null) {
                cartDetailsList = new ArrayList<>();
            }
        }

        model.addAttribute("cartDetailsList", cartDetailsList);
        int totalPrice = calcTotalPrice(cartDetailsList);
        model.addAttribute("totalPrice", totalPrice);
        return "shopping-cart";
    }

    @PostMapping("/add-cart")
    public String addCart(Integer productId, Model model) {
        Products product = productsService.load(productId);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            shoppingCartsService.updateOrAddToCart(userId, productId, 1);
        } else {
            // ログインしていない場合の処理
            List<ShoppingCartsDetail> cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
            if (cartDetailsList == null) {
                cartDetailsList = new ArrayList<>();
            }

            boolean productExistsInCart = false;
            for (ShoppingCartsDetail detail : cartDetailsList) {
                if (detail.getProductId().equals(productId)) {
                    detail.setQuantity(detail.getQuantity() + 1);
                    productExistsInCart = true;
                    break;
                }
            }

            if (!productExistsInCart) {
                ShoppingCartsDetail newCartItem = new ShoppingCartsDetail();
                newCartItem.setProductId(productId);
                newCartItem.setProductName(product.getProductName());
                newCartItem.setPrice(product.getPrice());
                newCartItem.setDescription(product.getDescription());
                newCartItem.setQuantity(1);
                newCartItem.generateUniqueId();
                newCartItem.setImageUrl(product.getImageUrl());
                cartDetailsList.add(newCartItem);
            }

            session.setAttribute("cartDetailsList", cartDetailsList);
        }

        return "redirect:/top/products";
    }

    @PostMapping("/delete-shopping-carts")
    public String deleteCart(Model model, @RequestParam String cartId) {
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("Received cartId: " + cartId);

        if (userId != null) {
            shoppingCartsService.deleteShoppingCart(Integer.parseInt(cartId));
        } else {
            // ログインしていない場合の処理
            List<ShoppingCartsDetail> cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
            if (cartDetailsList != null) {
                cartDetailsList.removeIf(detail -> detail.getCartId().intValue() == Integer.parseInt(cartId));
                session.setAttribute("cartDetailsList", cartDetailsList);
            }
        }

        return "redirect:/top/products";
    }

    private Integer calcTotalPrice(List<ShoppingCartsDetail> productsList) {
        Integer totalPrice = 0;
        for(ShoppingCartsDetail product : productsList) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice;
    }
}
