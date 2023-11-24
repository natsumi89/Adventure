package com.example.Adventure.service;

import com.example.Adventure.domain.Orders;
import com.example.Adventure.domain.Stamps;
import com.example.Adventure.repository.OrderRepository;
import com.example.Adventure.repository.StampRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

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
        System.out.println("Before setting User ID: " + orders.getUserId());

        Integer loggedInUserId = (Integer) session.getAttribute("userId");
        orders.setUserId(loggedInUserId);
        System.out.println("After setting User ID: " + orders.getUserId());
        orderConfirmationRepository.save(orders);
        System.out.println("Saved Order ID: " + orders.getOrderId());
        // スタンプ押印処理
        if (shouldApplyStamp(orders)) {
            Stamps stamp = new Stamps();
            stamp.setUserId(orders.getUserId());
            stamp.setRegionId(orders.getRegionId());
            stamp.setStampDate(Date.from(Instant.now()));

            // スタンプに関する情報をログで確認
            System.out.println("Stamp User ID: " + stamp.getUserId());
            System.out.println("Stamp Region ID: " + stamp.getRegionId());

            stampRepository.saveStamp(stamp);
            System.out.println("Saved Stamp ID: " + stamp.getStampId());
        }

        // スタンプ割引処理
        if (shouldApplyDiscount(orders.getUserId())) {
            applyDiscount(orders.getUserId());
        }
    }

    private boolean shouldApplyStamp(Orders orders) {
        // 注文完了時にスタンプを押す条件判定
        // 例: 注文した商品のregion_idごとにスタンプを押す

        List<Integer> productRegionIds = stampRepository.findRegionIdsByOrderId(orders.getOrderId());
        int userId = orders.getUserId();

        for (Integer regionId : productRegionIds) {
            List<Stamps> userStamps = stampRepository.findStampsByUserIdAndRegionId(userId, regionId);
            long totalStampsForRegion = userStamps.stream()
                    .filter(stamp -> stamp.getStamps() != null)
                    .mapToLong(Stamps::getStamps)
                    .sum();

            // スタンプを押す条件を適宜調整
            if (totalStampsForRegion < 10) {
                // まだ10個未満の場合はスタンプを押す
                Stamps newStamp = new Stamps();
                newStamp.setUserId(userId);
                newStamp.setRegionId(regionId);
                newStamp.setStampDate(Date.from(Instant.now()));
                stampRepository.saveStamp(newStamp);
            }
        }

        return true;  // ここでは単純に常に true を返しています。実際の条件に応じて変更してください。
    }
    private boolean shouldApplyDiscount(Integer userId) {
        // スタンプが10個以上たまったかどうかの条件判定
        List<Stamps> stamps = stampRepository.findStampsByUserId(userId);
        System.out.println("Stamps: " + stamps);

        if (stamps != null) {
            long totalStamps = stamps.stream()
                    .filter(stamp -> stamp.getStamps() != null)  // null チェックを追加
                    .mapToLong(Stamps::getStamps)
                    .sum();
            return totalStamps >= 10;  // 10個以上のスタンプがたまったら割引を適用
        } else {
            return false;  // スタンプが取得できない場合は割引を適用しない
        }
    }

    private void applyDiscount(Integer userId) {
        // 割引ロジックの実装
        // 例: 注文の合計金額に割引を適用
        List<Orders> userOrders = stampRepository.findOrdersByUserId(userId);
        for (Orders order : userOrders) {
            int discountedTotalPrice = (int) (order.getTotalPrice() * 0.9);  // 10%割引
            stampRepository.updateOrderTotalPrice(order.getOrderId(), discountedTotalPrice);
        }
    }
}