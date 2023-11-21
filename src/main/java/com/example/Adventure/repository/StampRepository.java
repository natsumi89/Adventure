package com.example.Adventure.repository;

import com.example.Adventure.domain.Stamps;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StampRepository {

    public static final RowMapper<Stamps> STAMPS_ROW_MAPPER = (rs,i) -> {
        Stamps stamps = new Stamps();
        stamps.setStampId(rs.getInt("stamp_id"));
        stamps.setStampDate(rs.getDate("stamp_date"));
        stamps.setUserId(rs.getInt("user_id"));
        stamps.setRegionId(rs.getInt("region_id"));
        return stamps;
    };
}
