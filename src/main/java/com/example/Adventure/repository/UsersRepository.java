package com.example.Adventure.repository;

import com.example.Adventure.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsersRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<Users> USERS_ROW_MAPPER = (rs, i) -> {
        Users users = new Users();

        users.setUserId(rs.getInt("user_id"));
        users.setLastName(rs.getString("last_name"));
        users.setFirstName(rs.getString("first_name"));
        users.setBirthDate(rs.getDate("birth_date"));
        users.setEmail(rs.getString("email"));
        users.setPassword(rs.getString("password"));

        return users;

    };

    public List<Users> findAll() {
        String sql = "SELECT user_id, last_name,first_name,birth_date, email, password FROM users ORDER BY user_id";
        List<Users> usersList = template.query(sql, USERS_ROW_MAPPER);
        return usersList;

    }

    public void insert(Users users) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("lastName",users.getLastName())
                .addValue("firstName",users.getFirstName()).addValue("birthDate",users.getBirthDate())
        .addValue("email",users.getEmail()).addValue("password",users.getPassword());
        String sql = "INSERT into users(last_name,first_name,birth_date, email, password)" +
                "VALUES(:lastName,:firstName,:birthDate,:email,:password)";
        template.update(sql,param);

    }

    public Users findByEmail(String email) {
        String sql = "SELECT user_id, last_name,first_name,birth_date, email, password FROM users WHERE email=:email";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email",email);
        List<Users> usersList = template.query(sql,param,USERS_ROW_MAPPER);
        if(usersList.size() == 0) {
            return null;
        }
        return usersList.get(0);
    }


    public void delete(Integer userId){
        String sql = "DELETE FROM users WHERE user_id=:userId";

        SqlParameterSource param = new MapSqlParameterSource().addValue("user_id",userId);
        template.update(sql,param);

    }
}
