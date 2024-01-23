package com.example.Adventure.controller;

import com.example.Adventure.domain.OrderDetails;
import com.example.Adventure.domain.Orders;
import com.example.Adventure.domain.ShoppingCartsDetail;
import com.example.Adventure.domain.Stamps;
import com.example.Adventure.form.OrdersForm;
import com.example.Adventure.repository.OrderRepository;
import com.example.Adventure.repository.StampRepository;
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
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private StampRepository stampRepository;

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
                session.setAttribute("cartDetailsList", cartDetailsList);
            }
        }

        System.out.println("Cart Details List: " + cartDetailsList);

        model.addAttribute("cartDetailsList", cartDetailsList);

        int totalPrice = calcTotalPrice(cartDetailsList);

        if (userId != null) {
            List<Stamps> existingStamps = stampRepository.findStampsByUserId(userId);
            if (!existingStamps.isEmpty() && existingStamps.get(0).getStamps() % 10 == 1) {
                double discountedPrice = 0.9 * totalPrice;
                totalPrice = (int) discountedPrice;
            }
        }
        session.setAttribute("totalPrice", totalPrice);
        model.addAttribute("totalPrice", totalPrice);
        System.out.println("合駅金額" + totalPrice);

        return "order-confirmation";
    }


    @PostMapping("/order/to-order-complete")
    public String toOrderComplete(@Valid OrdersForm ordersForm, BindingResult result,
                                  @RequestParam("pay") Integer paymentMethod, Model model) {

        List<ShoppingCartsDetail> cartDetailsList;
        Integer userId = (Integer) session.getAttribute("userId");
        Integer totalPrice = (Integer) session.getAttribute("totalPrice");

        if (userId != null) {
            cartDetailsList = shoppingCartsService.findShoppingCartsDetailByUserId(userId);
        } else {
            cartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
            if (cartDetailsList == null) {
                cartDetailsList = new ArrayList<>();
                session.setAttribute("cartDetailsList", cartDetailsList);
            }
        }

        System.out.println("Entering toOrderComplete method.");
        System.out.println("Cart Details List in toOrderComplete: " + cartDetailsList);

        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + ": " + error.getDefaultMessage());
            }
            model.addAttribute("ordersForm", ordersForm);
            return orderConfirmation(ordersForm, model);
        }

        Orders orders = new Orders();
        orders.setUserId((Integer) session.getAttribute("userId"));
        orders.setTotalPrice((Integer) session.getAttribute("totalPrice"));
        orders.setTelephone(ordersForm.getTelephone());
        orders.setZipCode(ordersForm.getZipCode());
        orders.setAddress(ordersForm.getAddress());
        orders.setPaymentMethod(paymentMethod);
        orders.setOrderDate(new Date());
        orders.setStatus("Order Placed");
        orders.setRegionId(ordersForm.getRegionId());

        // 保存前に注文IDを取得
        Integer orderId = orderRepository.saveAndGetOrderId(orders);
        // 保存後に注文情報を再度データベースから取得
        Orders savedOrder = orderRepository.load(orderId);

        // 注文商品ごとにOrderDetailsに情報を設定して保存
        for (ShoppingCartsDetail cartDetail : cartDetailsList) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(orderId);
            orderDetails.setProductId(cartDetail.getProductId());
            orderDetails.setQuantity(cartDetail.getQuantity());
            orderDetails.setSubTotalPrice(cartDetail.getPrice() * cartDetail.getQuantity());

            orderRepository.saveOrderDetails(orderDetails);
        }

        Stamps stamp = new Stamps();
        stamp.setUserId(userId);
        stamp.setOrderId(orderId);
        stamp.setStampDate(new Date());

        List<Stamps> existingStamps = stampRepository.findStampsByUserId(userId);
        if (!existingStamps.isEmpty()) {
            Stamps lastStamp = existingStamps.get(existingStamps.size() - 1);
            stamp.setStamps(lastStamp.getStamps());
            stamp.setCardNumber(lastStamp.getCardNumber());
        } else {
            stamp.setStamps(0);
            stamp.setCardNumber(0);
        }

        stamp.stampPressed();
        if (stamp != null) {
            model.addAttribute("myStamp", stamp);
        }

        System.out.println("Model Attributes: " + model.asMap());

        System.out.println("Stamps after pressing: " + stamp.getStamps());
        System.out.println("Card Number after pressing: " + stamp.getCardNumber());

        stampRepository.saveStamp(stamp);

        // 注文が確定されたらショッピングカートを空にする
        if (userId != null) {
            shoppingCartsService.deleteAllItemsFromCartByUserId(userId);
        } else {
            session.removeAttribute("cartDetailsList");
        }
        return "redirect:/order-completed";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error-page/400";
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
