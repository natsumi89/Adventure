package com.example.Adventure.service;

import com.example.Adventure.domain.OrderDetails;
import com.example.Adventure.repository.OrderConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderConfirmationService {
    @Autowired
    private OrderConfirmationRepository orderConfirmationRepository;
    public List<OrderDetails> findAll() {
        return orderConfirmationRepository.findAll();
    }


}