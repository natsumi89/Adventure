package com.example.Adventure.controller;

import com.example.Adventure.CartUtils;
import com.example.Adventure.domain.ShoppingCartsDetail;
import com.example.Adventure.service.ShoppingCartsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class OrderChangeController {
    @Autowired
    private HttpSession session;

    @Autowired
    private CartUtils cartUtils;
    @Autowired
    private ShoppingCartsService shoppingCartsService;
    @GetMapping("/change-order/amount")
    public ResponseEntity<Map<String, Integer>> changeOrderAmount(@RequestParam Integer productId, @RequestParam Integer value) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<ShoppingCartsDetail> cartDetailsList;

        if (userId != null) {
            shoppingCartsService.updateCartAmountByProductId(userId, productId, value);
            cartDetailsList = shoppingCartsService.findShoppingCartsDetailByUserId(userId);
        } else {
            cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
            if (cartDetailsList != null) {
                for (ShoppingCartsDetail detail : cartDetailsList) {
                    if (detail.getProductId().equals(productId)) {
                        detail.setQuantity(value);
                        break;
                    }
                }
                session.setAttribute("cartDetailsList", cartDetailsList);
            }
        }

        int newTotalPrice = cartUtils.calcTotalPrice(cartDetailsList);
        Map<String, Integer> responseMap = new HashMap<>();
        responseMap.put("newTotalPrice", newTotalPrice);
        return ResponseEntity.ok(responseMap);
    }
}