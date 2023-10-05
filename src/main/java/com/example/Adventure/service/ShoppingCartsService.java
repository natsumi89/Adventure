package com.example.Adventure.service;

import com.example.Adventure.domain.ShoppingCarts;
import com.example.Adventure.repository.ShoppingCartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartsService {
    @Autowired
    private ShoppingCartsRepository shoppingCartsRepository;

    public List<ShoppingCarts> findAll() {
        return shoppingCartsRepository.findAll();
    }
}
