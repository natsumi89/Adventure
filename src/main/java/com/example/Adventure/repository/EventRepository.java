package com.example.Adventure.repository;

import com.example.Adventure.domain.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;
    public static final RowMapper<Events> EVENTS_ROW_MAPPER = (rs,i) -> {
        Events events = new Events();
        events.setEventId(rs.getInt("event_id"));
        events.setRegionId(rs.getInt("region_id"));
        events.setEventName(rs.getString("event_name"));
        events.setDescription(rs.getString("description"));
        events.setEventDate(rs.getDate("event_date"));
        events.setEventLocation(rs.getString("event_location"));

        return events;
    };

    public Events load(Integer eventId) {
        String sql = "SELECT event_id, region_id, event_name, description, event_date, event_location FROM Events WHERE event_id=:eventId ORDER BY event_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("eventId", eventId);
        Events events = template.queryForObject(sql,param,EVENTS_ROW_MAPPER);
        return events;
    }

    public List<Events> findAll() {
        String sql = "SELECT event_id,region_id,event_name,description,event_date,event_location FROM Events";
        List<Events> eventsList = template.query(sql,EVENTS_ROW_MAPPER);
        return eventsList;
    }
}
