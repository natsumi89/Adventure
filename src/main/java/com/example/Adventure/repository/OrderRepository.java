package com.example.Adventure.repository;

import com.example.Adventure.domain.OrderDetails;
import com.example.Adventure.domain.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<OrderDetails> ORDER_DETAILS_ROW_MAPPER = (rs, i) -> {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailId(rs.getInt("order_detail_id"));
        orderDetails.setOrderId(rs.getInt("order_id"));
        orderDetails.setProductId(rs.getInt("product_id"));
        orderDetails.setQuantity(rs.getInt("quantity"));
        orderDetails.setSubTotalPrice(rs.getInt("subtotal_price"));
        return orderDetails;
    };

    public static final RowMapper<Orders> ORDERS_ROW_MAPPER = (rs, i) -> {
        Orders orders = new Orders();
        orders.setUserId(rs.getInt("user_id"));
        orders.setTotalPrice(rs.getInt("total_price"));
        orders.setOrderDate(rs.getDate("order_date"));
        orders.setStatus(rs.getString("status"));
        orders.setAddress(rs.getString("address"));
        orders.setZipCode(rs.getString("zip_code"));
        orders.setTelephone(rs.getString("telephone"));
        orders.setPaymentMethod(rs.getInt("payment_method"));
        return orders;
    };

    public List<OrderDetails> findAll() {
        String sql = "SELECT order_id, user_id, total_price, order_date, status,address,zip_code,telephone,payment_method FROM orders ORDER BY order_id";
        return template.query(sql, ORDER_DETAILS_ROW_MAPPER);
    }

    public Orders load(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_price, order_date, status,address,zip_code,telephone,payment_method FROM orders WHERE order_id = :orderId ORDER BY order_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderId);
        Orders orders = template.queryForObject(sql, param, ORDERS_ROW_MAPPER);
        return orders;
    }

    public void save(Orders orders) {
        String sql = "INSERT INTO orders (user_id, total_price, order_date, status,address,zip_code,telephone,payment_method) VALUES(:userId, :totalPrice, :orderDate, :status,:address,:zipCode,:telephone,:paymentMethod)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(orders);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder, new String[]{"order_id"}); // "order_id"は自動生成される主キーの列名

        if (keyHolder.getKeys() != null) {
            Map<String, Object> keys = keyHolder.getKeys();
            if (keys.containsKey("order_id")) {
                orders.setOrderId(((Number) keys.get("order_id")).intValue());
            } else {
                // order_idが見つからない場合の処理
            }
        } else {
            // キーが取得できなかった場合の処理
        }
    }

    public boolean existsByUserIdAndStatus(Integer userId, String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = :userId AND status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("status", status);

        return template.queryForObject(sql, param, Integer.class) > 0;
    }

    @Transactional
    public void saveOrderDetails(OrderDetails orderDetails) {
        try {
            String insertSql = "INSERT INTO order_details (order_id, product_id, quantity, subtotal_price, purchase_count) " +
                    "VALUES (:orderId, :productId, :quantity, :subTotalPrice, :purchaseCount)";

            String updateSql = "UPDATE order_details " +
                    "SET quantity = quantity + :quantity - 1, " +
                    "subtotal_price = :subTotalPrice, " +
                    "purchase_count = purchase_count + :purchaseCount " +
                    "WHERE order_id = :orderId AND product_id = :productId";

            // 正しい数量を設定
            int correctQuantity = orderDetails.getQuantity();

            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("orderId", orderDetails.getOrderId())
                    .addValue("productId", orderDetails.getProductId())
                    .addValue("quantity", correctQuantity)
                    .addValue("subTotalPrice", orderDetails.getSubTotalPrice())
                    .addValue("purchaseCount", orderDetails.getPurchaseCount()); // purchaseCountはオブジェクトから取得

            // まず挿入を試みる
            template.update(insertSql, param);

            // 挿入が失敗した場合は更新を試みる（重複エラーが発生した場合）
            template.update(updateSql, param);

            System.out.println("OrderDetails saved successfully - Order ID: " + orderDetails.getOrderId() + ", Product ID: " + orderDetails.getProductId());
        } catch (DataAccessException e) {
            // エラーログを追加
            System.out.println("Error saving order details: " + e.getMessage());
            throw e;  // 例外を再スローして呼び出し元で確認
        }
    }

    public int getTotalPurchaseCountByProductId(int productId) {
        String sql = "SELECT COALESCE(SUM(purchase_count), 0) FROM order_details WHERE product_id = :productId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("productId", productId);
        return template.queryForObject(sql, param, Integer.class);
    }
    public Integer saveAndGetOrderId(Orders orders) {
        String sql = "INSERT INTO orders (user_id, total_price, order_date, status, address, zip_code, telephone, payment_method) " +
                "VALUES (:userId, :totalPrice, :orderDate, :status, :address, :zipCode, :telephone, :paymentMethod)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(orders);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder, new String[]{"order_id"}); // "order_id"は自動生成される主キーの列名

        if (keyHolder.getKeys() != null) {
            Map<String, Object> keys = keyHolder.getKeys();
            if (keys.containsKey("order_id")) {
                return ((Number) keys.get("order_id")).intValue();
            } else {
                // order_idが見つからない場合の処理
                throw new RuntimeException("Failed to retrieve order ID after saving");
            }
        } else {
            // キーが取得できなかった場合の処理
            throw new RuntimeException("Failed to retrieve keys after saving");
        }
    }
}
