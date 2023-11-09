package com.example.Adventure.service;

import com.example.Adventure.domain.OrderDetails;
import com.example.Adventure.domain.Orders;
import com.example.Adventure.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderConfirmationService {
    @Autowired
    private OrderRepository orderConfirmationRepository;
    public List<OrderDetails> findAll() {
        return orderConfirmationRepository.findAll();
    }

    public void saveOrderDetails(OrderDetails orderDetails){
        orderConfirmationRepository.saveOrderDetails(orderDetails);
    }

    public void save(Orders orders) {
        orderConfirmationRepository.save(orders);
    }


}
