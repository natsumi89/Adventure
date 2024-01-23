package com.example.Adventure.service;

import com.example.Adventure.domain.OrderDetails;
import com.example.Adventure.domain.Orders;
import com.example.Adventure.repository.OrderRepository;
import com.example.Adventure.repository.StampRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderConfirmationService {

    @Autowired
    private OrderRepository orderConfirmationRepository;
    @Autowired
    private StampRepository stampRepository;
    @Autowired
    private HttpSession session;

    public void save(Orders orders) {
        // ユーザーが既に注文しているか確認
        if (orderConfirmationRepository.existsByUserIdAndStatus(orders.getUserId(), "Order Placed")) {
            throw new RuntimeException("同じユーザーが既に注文しています。");
        }

        Integer loggedInUserId = (Integer) session.getAttribute("userId");
        orders.setUserId(loggedInUserId);
        System.out.println("After setting User ID: " + orders.getUserId());
        orderConfirmationRepository.save(orders);
        System.out.println("Saved Order ID: " + orders.getOrderId());

//        // スタンプ押印処理
//        if (shouldApplyStamp(orders)) {
//            Stamps stamp = new Stamps();
//            stamp.setUserId(orders.getUserId());
//            stamp.setRegionId(orders.getRegionId());
//            stamp.setStampDate(Date.from(Instant.now()));
//
//            // スタンプに関する情報をログで確認
//            System.out.println("Stamp User ID: " + stamp.getUserId());
//            System.out.println("Stamp Region ID: " + stamp.getRegionId());
//
//            stampRepository.saveStamp(stamp);
//            System.out.println("Saved Stamp ID: " + stamp.getStampId());
//        }
//
//        // スタンプ割引処理
//        if (shouldApplyDiscount(orders.getUserId())) {
//            applyDiscount(orders.getUserId());
//        }
    }
//    private boolean shouldApplyStamp(Orders orders) {
//        List<Integer> productRegionIds = stampRepository.findRegionIdsByOrderId(orders.getOrderId());
//        int userId = orders.getUserId();
//
//        for (Integer regionId : productRegionIds) {
//            List<Stamps> userStamps = stampRepository.findStampsByUserIdAndRegionId(userId, regionId);
//
//            Stamps existingStamp = userStamps.stream()
//                    .filter(stamp -> Objects.equals(stamp.getOrderId(), orders.getOrderId()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (existingStamp != null) {
//                int newStamps = existingStamp.getStamps() + 1;
//                existingStamp.setStamps(newStamps);
//
//                if (newStamps % 10 == 0) {
//                    existingStamp.setCardNumber(existingStamp.getCardNumber() + 1);
//                }
//
//                stampRepository.saveStamp(existingStamp);
//            } else {
//                Stamps newStamp = new Stamps();
//                newStamp.setUserId(userId);
//                newStamp.setOrderId(orders.getOrderId());
//                newStamp.setStampDate(Date.from(Instant.now()));
//                newStamp.setStamps(1);
//
//                if (newStamp.getStamps() % 10 == 0) {
//                    newStamp.setCardNumber(newStamp.getCardNumber() + 1);
//                }
//
//                stampRepository.saveStamp(newStamp);
//            }
//        }
//
//        return true;
//    }
//    private boolean shouldApplyDiscount(Integer userId) {
//        // スタンプが10個以上たまったかどうかの条件判定
//        List<Stamps> stamps = stampRepository.findStampsByUserId(userId);
//
//        if (stamps != null) {
//            // Total Stampsをログに出力
//            long totalStamps = stamps.stream()
//                    .filter(stamp -> stamp.getStamps() != null)  // null チェックを追加
//                    .mapToLong(stamp -> stamp.getStamps() != null ? stamp.getStamps() : 0)
//                    .sum();
//
//            boolean applyDiscount = totalStamps >= 10;  // 10個以上のスタンプがたまったら割引を適用
//            System.out.println("Total Stamps: " + totalStamps);
//            System.out.println("Apply Discount: " + applyDiscount);
//
//            return applyDiscount;
//        } else {
//            return false;  // スタンプが取得できない場合は割引を適用しない
//        }
//    }
//
//    private void applyDiscount(Integer userId) {
//        List<Orders> userOrders = stampRepository.findOrdersByUserId(userId);
//
//        for (Orders order : userOrders) {
//            List<Stamps> userStamps = stampRepository.findStampsByUserId(userId);
//            long totalStamps = userStamps.stream()
//                    .filter(stamp -> stamp.getStamps() != null)
//                    .mapToLong(Stamps::getStamps)
//                    .sum();
//
//            if (totalStamps >= 10) {
//                int discountedTotalPrice = (int) (order.getTotalPrice() * 0.9);  // 10%割引
//                stampRepository.updateOrderTotalPrice(order.getOrderId(), discountedTotalPrice);
//
//                // 割引情報をDBに格納
//                Stamps discountStamp = new Stamps();
//                discountStamp.setUserId(userId);
//                discountStamp.setStampDate(Date.from(Instant.now()));
//                discountStamp.setStamps(-10);  // マイナスのスタンプで割引を表現
//
//                // 以下の行を追加して割引情報を保存
//                stampRepository.saveStamp(discountStamp);
//            }
//        }
//    }
    public void saveOrderDetails(OrderDetails orderDetails) {
        System.out.println("Received OrderDetails - Order ID: " + orderDetails.getOrderId() + ", Product ID: " + orderDetails.getProductId());
        orderConfirmationRepository.saveOrderDetails(orderDetails);
    }
}