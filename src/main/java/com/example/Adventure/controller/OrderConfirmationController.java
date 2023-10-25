package com.example.Adventure.controller;

import com.example.Adventure.domain.Orders;
import com.example.Adventure.domain.Users;
import com.example.Adventure.form.OrdersForm;
import com.example.Adventure.repository.OrderRepository;
import com.example.Adventure.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("")
public class OrderConfirmationController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/order-confirmation")
    public String orderConfirmation(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
            // セッションにユーザ情報がない場合の処理（例: ログインページへのリダイレクト）
            return "redirect:/login";
        }
        OrdersForm ordersForm = new OrdersForm();
        ordersForm.setUserId(user.getUserId());
        ordersForm.setTelephone(user.getTelephone());
        ordersForm.setZipCode(user.getZipCode());
        ordersForm.setAddress(user.getAddress());

        model.addAttribute("ordersForm", ordersForm);
        return "order-confirmation";
    }

    @PostMapping("/to-order-complete")
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
