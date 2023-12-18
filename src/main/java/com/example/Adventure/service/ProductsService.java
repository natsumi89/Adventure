package com.example.Adventure.service;

import com.example.Adventure.domain.Products;

import java.util.List;

public interface ProductsService {
    List<String> getSearchProduct(String query);

    List<Products> findAll();

    Products load(Integer product_id);

    List<Products> searchProducts(String query);
    List<String> convertProductsToProductNames(List<Products> productsList);

    List<Products> findTopProducts(int limit);
}



