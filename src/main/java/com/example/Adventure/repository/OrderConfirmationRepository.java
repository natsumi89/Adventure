package com.example.Adventure.repository;

import com.example.Adventure.domain.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderConfirmationRepository {
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

    public List<OrderDetails> findAll() {
        String sql = "SELECT order_detail_id,order_id,product_id,quantity,subtotal_price FROM order_details ORDER BY order_detail";
        List<OrderDetails> orderDetailsList = template.query(sql,ORDER_DETAILS_ROW_MAPPER);
        return orderDetailsList;

    }
}
