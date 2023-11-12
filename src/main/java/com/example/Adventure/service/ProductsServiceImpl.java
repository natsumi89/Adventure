package com.example.Adventure.service;

import com.example.Adventure.domain.Products;
import com.example.Adventure.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public List<String> getSearchProduct(String query) {
        List<Products> products = productsRepository.searchProducts(query);
        List<String> productNames = convertProductsToProductNames(products);
        return productNames;
    }

    public List<String> convertProductsToProductNames(List<Products> productsList) {
        List<String> productNames = new ArrayList<>();
        for (Products product : productsList) {
            productNames.add(product.getProductName());
        }
        return productNames;
    }

    public List<Products> searchProducts(String query) {
        return productsRepository.searchProducts(query);
    }

    public List<Products> findAll() {
        return productsRepository.findAll();
    }

    public Products load(Integer product_id) {
        return productsRepository.load(product_id);
    }

    public void save(Products products) {
        productsRepository.save(products);
    }

    public void delete(Products products) {
        productsRepository.delete(products);
    }

    public List<String> findRegions() {
        return productsRepository.findRegion();
    }
}

