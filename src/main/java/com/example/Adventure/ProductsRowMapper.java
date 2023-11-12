package com.example.Adventure;

import com.example.Adventure.domain.Products;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsRowMapper implements RowMapper<Products> {
    @Override
    public Products mapRow(ResultSet rs, int rowNum) throws SQLException {
        Products products = new Products();
        products.setProductId(rs.getInt("product_id"));
        products.setRegionId(rs.getInt("region_id"));
        products.setProducerId(rs.getInt("producer_id"));
        products.setProductName(rs.getString("product_name"));
        products.setDescription(rs.getString("description"));
        products.setPrice(rs.getInt("price"));
        products.setImageUrl(rs.getString("image_url"));
//        products.setRegionName(rs.getString("region_name"));
        return products;
    }
}