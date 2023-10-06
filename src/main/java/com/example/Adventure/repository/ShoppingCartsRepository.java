package com.example.Adventure.repository;

import com.example.Adventure.domain.ShoppingCarts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShoppingCartsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<ShoppingCarts> SHOPPING_CARTS_ROW_MAPPER = (rs,i) -> {
        ShoppingCarts shoppingCarts = new ShoppingCarts();

        shoppingCarts.setCartId(rs.getInt("cart_id"));
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

    public void deleteShoppingCart(Integer cartId) {
        String sql = "DELETE FROM shopping_carts WHERE cart_id=:cartId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("cartId",cartId);
        template.update(sql,param);

    }

    public void deleteShoppingCartByProductId(Integer productId) {
        String sql = "DELETE FROM shopping_carts WHERE product_id=:productId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("productId", productId);
        template.update(sql, param);
    }

    public List<ShoppingCarts> findShoppingCartsByUserId(Integer userId) {
        String sql = "SELECT * FROM shopping_carts WHERE user_id=:userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        return template.query(sql, param, SHOPPING_CARTS_ROW_MAPPER);
    }



}
