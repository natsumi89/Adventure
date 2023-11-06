package com.example.Adventure;

import com.example.Adventure.domain.ShoppingCartsDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartUtils {

    public Integer calcTotalPrice(List<ShoppingCartsDetail> productsList) {
        Integer totalPrice = 0;
        for(ShoppingCartsDetail product : productsList) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice;
    }
}

