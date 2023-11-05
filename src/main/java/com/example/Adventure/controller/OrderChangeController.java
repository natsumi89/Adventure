package com.example.Adventure.controller;

import com.example.Adventure.domain.ShoppingCartsDetail;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class OrderChangeController {
    @Autowired
    private HttpSession session;

    @GetMapping("/change-order/amount")
    public void changeAmount(@RequestParam("productId") int productId, @RequestParam("value") String value,
                             @RequestParam("totalPrice") int totalPrice) {

        // セッションからShoppingCartsDetailのリストを取得
        List<ShoppingCartsDetail> cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");

        // 該当する商品の詳細を取得
        ShoppingCartsDetail cartDetail = cartDetailsList.get(productId);

        // 商品の数量を更新
        cartDetail.setQuantity(Integer.parseInt(value));

        // 更新した詳細をリストにセット
        cartDetailsList.set(productId, cartDetail);

        // 更新したリストをセッションに保存
        session.setAttribute("cartDetailsList", cartDetailsList);
        session.setAttribute("totalPrice", totalPrice);
    }

    }
