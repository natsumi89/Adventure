package com.example.Adventure.controller;

import com.example.Adventure.domain.Orders;
import com.example.Adventure.domain.ShoppingCartsDetail;
import com.example.Adventure.form.OrdersForm;
import com.example.Adventure.repository.OrderRepository;
import com.example.Adventure.repository.UsersRepository;
import com.example.Adventure.service.OrderConfirmationService;
import com.example.Adventure.service.ProductsService;
import com.example.Adventure.service.ShoppingCartsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @Autowired
    private HttpSession session;

    @Autowired
    private ShoppingCartsService shoppingCartsService;
    @Autowired
    private OrderConfirmationService orderConfirmationService;

    @GetMapping("/order/order-confirmation")
    public String orderConfirmation(OrdersForm ordersForm, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<ShoppingCartsDetail> cartDetailsList;

        model.addAttribute("ordersForm", new OrdersForm());

        if (userId != null) {
            cartDetailsList = shoppingCartsService.findShoppingCartsDetailByUserId(userId);
        } else {
            cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
            if (cartDetailsList == null) {
                cartDetailsList = new ArrayList<>();
            }
        }

        model.addAttribute("cartDetailsList", cartDetailsList);
        int totalPrice = calcTotalPrice(cartDetailsList);
        model.addAttribute("totalPrice", totalPrice);

        return "order-confirmation";
    }

    @PostMapping("/order/to-order-complete")
    public String toOrderComplete(@Valid OrdersForm ordersForm, BindingResult result, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (result.hasErrors()) {
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

        if (userId != null) {
            shoppingCartsService.deleteAllItemsFromCartByUserId(userId);
        } else {
            session.removeAttribute("cartDetailsList");
        }

        return "order-completed";
    }

    private Integer calcTotalPrice(List<ShoppingCartsDetail> productsList) {
        Integer totalPrice = 0;
        for (ShoppingCartsDetail product : productsList) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice;
    }
}
