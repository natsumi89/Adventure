package com.example.Adventure.controller;

import com.example.Adventure.domain.Orders;
import com.example.Adventure.domain.Products;
import com.example.Adventure.form.OrdersForm;
import com.example.Adventure.repository.OrderRepository;
import com.example.Adventure.repository.UsersRepository;
import com.example.Adventure.service.ProductsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class OrderConfirmationController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsService productsService;

    @GetMapping("/order/order-confirmation")
    public String orderConfirmation(Model model, HttpSession session,Orders orders) {
        List<Products> cartMerchandiseList = (List<Products>) session.getAttribute("cartProductsList");

//        for (Products products : cartProductsList) {
//            if (products.getQuantity() == null) {
//                products.setAmount(1);
//            }
//        }
//
//        OrdersForm ordersForm = new OrdersForm();
//        ordersForm.setUserId(users.getUserId());
//        ordersForm.setTelephone(user.getTelephone());
//        ordersForm.setZipCode(user.getZipCode());
//        ordersForm.setAddress(user.getAddress());

//        Orders order = new Orders();
//        order.setUserId(.getUserId());
//        ordersForm.setTelephone(user.getTelephone());
//        ordersForm.setZipCode(user.getZipCode());
//        ordersForm.setAddress(user.getAddress());



//        model.addAttribute("order", order);
        List<Products> cartDetailsList = (List<Products>) session.getAttribute("cartDetailsList");
        if (cartDetailsList == null) {
            cartDetailsList = new ArrayList<>();
            session.setAttribute("cartDetailsList", cartDetailsList);
        }
        return "order-confirmation";
    }

    @PostMapping("/order/to-order-complete")
    public String toOrderComplete(@Validated OrdersForm ordersForm, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("ordersForm", ordersForm);
            return "order-confirmation";
        }

        Orders orders = new Orders();
        orders.setUserId(ordersForm.getUserId());
        orders.setTotalPrice(ordersForm.getTotalPrice());
        orders.setTelephone(ordersForm.getTelephone());
        orders.setZipCode(ordersForm.getZipCode());
        orders.setAddress(ordersForm.getAddress());
        orders.setPaymentMethod(ordersForm.getPaymentMethod());
        orders.setOrderDate(new Date());  // 現在の日付を設定
        orders.setStatus("Order Placed");  // ここでステータスを設定

        orderRepository.save(orders);  // データベースに保存

        return "order-complete";
    }
}
