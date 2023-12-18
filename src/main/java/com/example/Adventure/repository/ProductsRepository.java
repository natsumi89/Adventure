package com.example.Adventure.repository;

import com.example.Adventure.ProductsRowMapper;
import com.example.Adventure.domain.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductsRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static final RowMapper<Products> PRODUCTS_ROW_MAPPER = (rs,i) -> {
        Products products = new Products();
        products.setProductId(rs.getInt("product_id"));
        products.setRegionId(rs.getInt("region_id"));
        products.setProducerId(rs.getInt("producer_id"));
        products.setProductName(rs.getString("product_name"));
        products.setDescription(rs.getString("description"));
        products.setPrice(rs.getInt("price"));
        products.setImageUrl(rs.getString("image_url"));
        products.setRegionName(rs.getString("region_name"));

        return products;
    };

    public List<Products> findAll() {
        String sql = "SELECT p.product_id, p.region_id, p.producer_id, p.product_name, p.description, p.price, p.image_url, r.region_name " +
                "FROM products p JOIN regions r ON p.region_id = r.region_id " +
                "ORDER BY p.region_id";
        List<Products> productsList = template.query(sql, PRODUCTS_ROW_MAPPER);
        return productsList;
    }

    public Products load(Integer productId) {
        String sql = "SELECT p.product_id, p.region_id, p.producer_id, p.product_name, p.description, p.price, p.image_url, r.region_name " +
                "FROM products p JOIN regions r ON p.region_id = r.region_id " +
                "WHERE p.product_id = :product_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("product_id",productId);
        Products products = template.queryForObject(sql,param,PRODUCTS_ROW_MAPPER);
        return products;
    }

    public List<String> findRegion() {
        String sql = "SELECT DISTINCT r.region_name FROM regions r LEFT OUTER JOIN products p ON r.region_id = p.region_id ORDER BY r.region_id";
        return template.query(sql, (rs, rowNum) -> rs.getString("region_name"));
    }

    public void save(Products products) {
        String sql = "INSERT INTO products (region_id,producer_id,product_name,description,price,image_url) VALUES(:regionId,:producerId,:productName,:description,:imageUrl)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(products);
        template.update(sql,param);

    }

    public void delete(Products products) {
        String sql = "DELETE product_id,region_id,producer_id,product_name,description,price,image_url FROM products WHERE product_id=:productId";
        SqlParameterSource param = new BeanPropertySqlParameterSource(products);
        template.update(sql,param);

    }

    public List<Products> getAllProductsInCart(Integer userId) {
        String sql = "SELECT p.* " +
                "FROM shopping_carts sc " +
                "INNER JOIN products p ON sc.product_id = p.product_id " +
                "WHERE sc.user_id=:userId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        return template.query(sql, param, PRODUCTS_ROW_MAPPER);
    }
    public List<Products> searchProducts(String query) {
        String sql = "SELECT * FROM products WHERE product_name LIKE :query";
        String searchQuery = "%" + query + "%";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("query", searchQuery);

        return template.query(sql, parameters, new ProductsRowMapper());
    }

    public List<Products> findTopProducts(int limit) {
        String sql = "SELECT p.product_id, p.region_id, p.producer_id, p.product_name, p.description, p.price, p.image_url, r.region_name " +
                "FROM products p " +
                "JOIN regions r ON p.region_id = r.region_id " +
                "JOIN (SELECT product_id, SUM(quantity) as purchase_count FROM order_details GROUP BY product_id) od " +
                "ON p.product_id = od.product_id " +
                "ORDER BY od.purchase_count DESC " +
                "LIMIT :limit";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("limit", limit);

        return template.query(sql, params, PRODUCTS_ROW_MAPPER);
    }

}
