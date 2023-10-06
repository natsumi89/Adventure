package com.example.Adventure.service;

import com.example.Adventure.domain.Products;
import com.example.Adventure.domain.ShoppingCarts;
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


    public void deleteShoppingCartByProductId(Integer productId) {
        shoppingCartsRepository.deleteShoppingCartByProductId(productId);
    }

    public List<Products> getAllProductsInCart(Integer userId) {
        // DBからショッピングカートの全商品を取得
        List<ShoppingCarts> cartItems = shoppingCartsRepository.findShoppingCartsByUserId(userId);

        // ここで必要に応じて、Productsテーブルから実際の商品データを取得するロジックを追加する
        // サンプルとしては、productRepositoryを使用して各product_idに一致する商品を取得する方法など

        List<Products> productsList = new ArrayList<>();
        for (ShoppingCarts item : cartItems) {
            Products product = productsRepository.load(item.getProductId());
            productsList.add(product);
        }
        return productsList;
    }

    public void deleteShoppingCart(Integer cartId) {
        shoppingCartsRepository.deleteShoppingCart(cartId);
    }

    public void updateOrAddToCart(Integer userId, Integer productId, Integer quantity) {
        List<ShoppingCarts> existingCartItems = shoppingCartsRepository.findShoppingCartsByUserId(userId);

        // 既存のカートアイテムを検索して、指定された商品IDに一致するものがあるか確認
        ShoppingCarts existingItem = null;
        for (ShoppingCarts item : existingCartItems) {
            if (item.getProductId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            // 既存のカートアイテムがある場合、数量を更新
            // この例では、数量が単純に1増加しますが、必要に応じて変更してください
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartsRepository.updateCartItemQuantity(existingItem);  // このメソッドはShoppingCartsRepositoryに追加する必要があります
        } else {
            // カートアイテムが存在しない場合、新しいカートアイテムを追加
            ShoppingCarts newItem = new ShoppingCarts();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            shoppingCartsRepository.insertCartItem(newItem);  // このメソッドはShoppingCartsRepositoryに追加する必要があります
        }
    }
}
