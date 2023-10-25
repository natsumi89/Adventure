package com.example.Adventure.repository;

import com.example.Adventure.domain.OrderDetails;
import com.example.Adventure.domain.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<OrderDetails> ORDER_DETAILS_ROW_MAPPER = (rs,i) -> {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailId(rs.getInt("order_detail_id"));
        orderDetails.setOrderId(rs.getInt("order_id"));
        orderDetails.setProductId(rs.getInt("product_id"));
        orderDetails.setQuantity(rs.getInt("quantity"));
        orderDetails.setSubTotalPrice(rs.getInt("subtotal_price"));
        return orderDetails;
    };

    public static final RowMapper<Orders> ORDERS_ROW_MAPPER = (rs,i) -> {
        Orders orders = new Orders();
        orders.setOrderId(rs.getInt("order_id"));
        orders.setUserId(rs.getInt("user_id"));
        orders.setTotalPrice(rs.getInt("total_price"));
        orders.setOrderDate(rs.getDate("order_date"));
        orders.setStatus(rs.getString("status"));
        orders.setAddress(rs.getString("address"));
        orders.setZipCode(rs.getInt("zip_code"));
        orders.setTelephone(rs.getInt("telephone"));
        orders.setPaymentMethod(rs.getString("payment_method"));
        return orders;
    };

    public List<OrderDetails> findAll() {
        String sql = "SELECT order_id, user_id, total_price, order_date, status,address,zip_code,telephone,payment_method FROM orders ORDER BY order_id";
        return template.query(sql, ORDER_DETAILS_ROW_MAPPER);
    }

    public Orders load(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_price, order_date, status,address,zip_code,telephone,payment_method FROM orders WHERE order_id = :orderId ORDER BY order_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("order_id",orderId);
        Orders orders = template.queryForObject(sql,param,ORDERS_ROW_MAPPER);
        return orders;
    }

    public void save(Orders orders) {
        String sql = "INSERT INTO orders (order_id, user_id, total_price, order_date, status,address,zip_code,telephone,payment_method) VALUES(:orderId, :userId, :totalPrice, :orderDate, :status,:address,:zipCode,:telephone,:paymentMethod)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(orders);
        template.update(sql,param);

    }
}
