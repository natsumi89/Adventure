package com.example.Adventure.controller;

import com.example.Adventure.domain.OrderDetails;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        session.setAttribute("total_price", totalPrice); // totalPriceをsessionにセット
        model.addAttribute("totalPrice", totalPrice);

        return "order-confirmation";
    }
    @PostMapping("/order/to-order-complete")
    public String toOrderComplete(@Valid OrdersForm ordersForm, BindingResult result,
                                  @RequestParam("pay") Integer paymentMethod, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        Integer totalPrice = (Integer) session.getAttribute("total_price");

        // cartDetailsList を宣言および初期化
        List<ShoppingCartsDetail> cartDetailsList = new ArrayList<>(); // 明示的に初期化


        if (result.hasErrors()) {
            // バリデーションエラーがある場合、ログにエラーメッセージを出力してみる
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + ": " + error.getDefaultMessage());
            }
            model.addAttribute("ordersForm", ordersForm);
            return orderConfirmation(ordersForm, model);
        }

            Orders orders = new Orders();
            orders.setUserId((Integer) session.getAttribute("userId"));
            orders.setTotalPrice(totalPrice);
            orders.setTelephone(ordersForm.getTelephone());
            orders.setZipCode(ordersForm.getZipCode());
            orders.setAddress(ordersForm.getAddress());
            orders.setPaymentMethod(paymentMethod);
            orders.setOrderDate(new Date());
            orders.setStatus("Order Placed");
            orders.setRegionId(ordersForm.getRegionId());

            System.out.println("Before saving order. Status: " + orders.getStatus());
            orderRepository.save(orders);
            System.out.println("After saving order. Status: " + orders.getStatus());
        for (ShoppingCartsDetail cartDetail : cartDetailsList) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(orders.getOrderId());
            orderDetails.setProductId(cartDetail.getProductId());
            orderDetails.setQuantity(cartDetail.getQuantity());
            orderDetails.setSubTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());
            orderRepository.saveOrderDetails(orderDetails);
        }

            // 注文が確定されたらショッピングカートを空にする
            if (userId != null) {
                shoppingCartsService.deleteAllItemsFromCartByUserId(userId);
            } else {
                session.removeAttribute("cartDetailsList");
            }

        return "redirect:/order-completed";
    }


    @GetMapping("/order-completed")
        public String orderComplete() {
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
