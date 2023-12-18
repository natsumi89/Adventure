package com.example.Adventure.repository;

import com.example.Adventure.domain.Products;
import com.example.Adventure.domain.ShoppingCarts;
import com.example.Adventure.domain.ShoppingCartsDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public static final RowMapper<ShoppingCartsDetail> SHOPPING_CARTS_DETAIL_ROW_MAPPER = (rs, i) -> {
        ShoppingCartsDetail detail = new ShoppingCartsDetail();
        detail.setCartId(rs.getInt("cart_id"));
        detail.setUserId(rs.getInt("user_id"));
        detail.setProductId(rs.getInt("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setProductName(rs.getString("product_name"));
        detail.setPrice(rs.getInt("price"));
        detail.setDescription(rs.getString("description"));
        detail.setImageUrl(rs.getString("image_url"));
        return detail;
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

    public void setShoppingCart(List<Products> list, int userId) {
        String deleteMerchandiseInShoppingCart = "DELETE FROM shopping_carts WHERE user_id=:user_id;";
        SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", userId);
        template.update(deleteMerchandiseInShoppingCart, param);
        for (Products products : list) {
            String setShoppingCartSql = "INSERT INTO shopping_carts(user_id,product_id,quantity)\n"
                    + "VALUES(:user_id, :product_id, 1);";
            SqlParameterSource param2 = new MapSqlParameterSource().addValue("product_id", products.getProductId())
                    .addValue("user_id", userId);
            template.update(setShoppingCartSql, param2);
        }
    }

    public List<ShoppingCarts> findShoppingCartsByUserId(Integer userId) {
        String sql = "SELECT * FROM shopping_carts WHERE user_id=:userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        return template.query(sql, param, SHOPPING_CARTS_ROW_MAPPER);
    }

    public void insertCartItem(ShoppingCarts cartItem) {
        String sql = "INSERT INTO shopping_carts(user_id, product_id, quantity) VALUES(:userId, :productId, :quantity)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", cartItem.getUserId())
                .addValue("productId", cartItem.getProductId())
                .addValue("quantity", cartItem.getQuantity());
        template.update(sql, param);
    }

    public Integer getProductIdByCartId(Integer cartId) {
        try {
            String sql = "SELECT product_id FROM shopping_carts WHERE cart_id=:cartId";
            SqlParameterSource param = new MapSqlParameterSource().addValue("cartId", cartId);
            return template.queryForObject(sql, param, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ShoppingCarts findByUserIdAndProductId(Integer userId, Integer productId) {
        String sql = "SELECT * FROM shopping_carts WHERE user_id=:userId AND product_id=:productId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("productId", productId);

        try {
            return template.queryForObject(sql, params, SHOPPING_CARTS_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null; // or handle as per your requirement
        }
    }

    public void updateCartItemQuantityByUserIdAndProductId(ShoppingCarts cartItem) {
        String sql = "UPDATE shopping_carts SET quantity=:quantity WHERE user_id=:userId AND product_id=:productId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("quantity", cartItem.getQuantity())
                .addValue("userId", cartItem.getUserId())
                .addValue("productId", cartItem.getProductId());
        template.update(sql, param);
    }

    public List<ShoppingCartsDetail> findShoppingCartsDetailByUserId(Integer userId) {
        String sql = "SELECT sc.*, p.product_name, p.price, p.description,p.image_url " +
                "FROM shopping_carts sc " +
                "INNER JOIN products p ON sc.product_id = p.product_id " +
                "WHERE sc.user_id=:userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        return template.query(sql, param, SHOPPING_CARTS_DETAIL_ROW_MAPPER);
    }

    public void deleteAllItemsFromCartByUserId(Integer userId) {
        String sql = "DELETE FROM shopping_carts WHERE user_id=:userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        template.update(sql, param);
    }
}
