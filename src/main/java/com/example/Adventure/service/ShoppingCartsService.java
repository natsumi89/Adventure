package com.example.Adventure.service;

import com.example.Adventure.domain.Products;
import com.example.Adventure.domain.ShoppingCarts;
import com.example.Adventure.domain.ShoppingCartsDetail;
import com.example.Adventure.repository.ProductsRepository;
import com.example.Adventure.repository.ShoppingCartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ShoppingCartsService {
    @Autowired
    private ShoppingCartsRepository shoppingCartsRepository;
    @Autowired
    private ProductsRepository productsRepository;

    public List<ShoppingCarts> findAll() {
        return shoppingCartsRepository.findAll();
    }

    public List<ShoppingCartsDetail> findShoppingCartsDetailByUserId(Integer userId) {
        // DBからショッピングカートの全商品を取得
        List<ShoppingCarts> cartItems = shoppingCartsRepository.findShoppingCartsByUserId(userId);

        // ここで必要に応じて、Productsテーブルから実際の商品データを取得するロジックを追加する
        // サンプルとしては、productRepositoryを使用して各product_idに一致する商品を取得する方法など

        List<Products> productsList = new ArrayList<>();
        for (ShoppingCarts item : cartItems) {
            Products product = productsRepository.load(item.getProductId());
            productsList.add(product);
        }
        return shoppingCartsRepository.findShoppingCartsDetailByUserId(userId);
    }

    public void deleteShoppingCart(Integer cartId) {
        shoppingCartsRepository.deleteShoppingCart(cartId);
    }

    public void updateOrAddToCart(Integer userId, Integer productId, Integer quantity) {
        ShoppingCarts cartItem = new ShoppingCarts();
        cartItem.setUserId(userId);  // <-- これを確認してください。
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        saveCartItem(cartItem);
    }


    public Integer getProductIdByCartId(Integer cartId) {
        return shoppingCartsRepository.getProductIdByCartId(cartId);
    }

    public void saveCartItem(ShoppingCarts cartItem) {
        ShoppingCarts existingItem = shoppingCartsRepository.findByUserIdAndProductId(cartItem.getUserId(), cartItem.getProductId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            shoppingCartsRepository.updateCartItemQuantityByUserIdAndProductId(existingItem); // ここを変更
        } else {
            shoppingCartsRepository.insertCartItem(cartItem);
        }
    }

    public void deleteAllItemsFromCartByUserId(Integer userId) {
        shoppingCartsRepository.deleteAllItemsFromCartByUserId(userId);
    }

}

