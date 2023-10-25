package com.example.Adventure.repository;

import com.example.Adventure.domain.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductsRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<Products> PRODUCTS_ROW_MAPPER = (rs,i) -> {
        Products products = new Products();
        products.setProductId(rs.getInt("product_id"));
        products.setRegionId(rs.getInt("region_id"));
        products.setProducerId(rs.getInt("producer_id"));
        products.setProductName(rs.getString("product_name"));
        products.setDescription(rs.getString("description"));
        products.setPrice(rs.getInt("price"));
        products.setImageUrl(rs.getString("image_url"));


        return products;
    };

    public List<Products> findAll() {
        String sql = "SELECT product_id, region_id, producer_id, product_name, description, price,image_url FROM products ORDER BY region_id,product_id";
        List<Products> productsList = template.query(sql, PRODUCTS_ROW_MAPPER);
        return productsList;
    }

    public Products load(Integer productId) {
        String sql = "SELECT product_id,region_id,producer_id,product_name,description,price,image_url FROM products WHERE product_id = :product_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("product_id",productId);
        Products products = template.queryForObject(sql,param,PRODUCTS_ROW_MAPPER);
        return products;
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

}
