package com.example.Adventure.repository;

import com.example.Adventure.domain.Orders;
import com.example.Adventure.domain.Stamps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.example.Adventure.repository.OrderRepository.ORDERS_ROW_MAPPER;

@Repository
public class StampRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<Stamps> STAMPS_ROW_MAPPER = (rs, i) -> {
        Stamps stamps = new Stamps();
        stamps.setStampId(rs.getInt("stamp_id"));
        stamps.setStampDate(rs.getDate("stamp_date"));
        stamps.setUserId(rs.getInt("user_id"));
        stamps.setRegionId(rs.getInt("region_id"));
        return stamps;
    };

    public void saveStamp(Stamps stamp) {
        String sql = "INSERT INTO stamps (user_id, region_id, stamp_date) VALUES(:userId, :regionId, :stampDate)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(stamp);
        template.update(sql, param);
    }

    public List<Integer> findRegionIdsByOrderId(Integer orderId) {
        String sql = "SELECT DISTINCT p.region_id " +
                "FROM order_details od " +
                "JOIN products p ON od.product_id = p.product_id " +
                "WHERE od.order_id = :orderId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderId);
        List<Integer> regionIds = template.queryForList(sql, param, Integer.class);
        return regionIds != null ? regionIds : Collections.emptyList();
    }

    public List<Orders> findOrdersByUserId(Integer userId) {
        String sql = "SELECT * FROM orders WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        return template.query(sql, param, ORDERS_ROW_MAPPER);
    }

    public void updateOrderTotalPrice(Integer orderId, Integer totalPrice) {
        String sql = "UPDATE orders SET total_price = :totalPrice WHERE order_id = :orderId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("totalPrice", totalPrice)
                .addValue("orderId", orderId);
        template.update(sql, param);
    }

    public List<Stamps> findStampsByUserId(Integer userId) {
        String sql = "SELECT * FROM stamps WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        return template.query(sql, param, STAMPS_ROW_MAPPER);
    }

    public List<Stamps> findStampsByUserIdAndRegionId(Integer userId, Integer regionId) {
        String sql = "SELECT * FROM stamps WHERE user_id = :userId AND region_id = :regionId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("regionId", regionId);
        return template.query(sql, param, STAMPS_ROW_MAPPER);
    }
}
