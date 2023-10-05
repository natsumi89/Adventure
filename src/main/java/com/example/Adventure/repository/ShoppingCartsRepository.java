package com.example.Adventure.repository;

import com.example.Adventure.domain.ShoppingCarts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShoppingCartsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<ShoppingCarts> SHOPPING_CARTS_ROW_MAPPER = (rs,i) -> {
        ShoppingCarts shoppingCarts = new ShoppingCarts();

        shoppingCarts.setCartsId(rs.getInt("cart_id"));
        shoppingCarts.setUserId(rs.getInt("user_id"));
        shoppingCarts.setProductId(rs.getInt("product_id"));
        shoppingCarts.setQuantity(rs.getInt("quantity"));

        return shoppingCarts;
    };

    public List<ShoppingCarts> findAll() {
        String sql = "SELECT cart_id,user_id,product_id,quantity FROM shopping_carts ORDER BY cart_id";
        List<ShoppingCarts> shoppingCartsList = template.query(sql,SHOPPING_CARTS_ROW_MAPPER);
        return shoppingCartsList;
    }
}
