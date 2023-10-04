package com.example.Adventure.service;

import com.example.Adventure.domain.Products;
import com.example.Adventure.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

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
}
