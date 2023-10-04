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

        users.setUserId(rs.getInt("userId"));
        users.setUserName(rs.getString("userName"));
        users.setEmail(rs.getString("email"));
        users.setPassword(rs.getString("password"));

        return users;
    };

    public List<Users> findAll() {
        String sql = "SELECT user_id, user_name, email, password FROM users ORDER BY user_id";
        List<Users> usersList = template.query(sql, USERS_ROW_MAPPER);
        return usersList;
    }



    public void insert(Users users) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name",users.getUserName())
        .addValue("email",users.getEmail()).addValue("password",users.getPassword());
        String sql = "INSERT into users(user_name,email,password)" +
                "VALUES(:userName,:email,:password)";
        template.update(sql,param);
    }

    public Users findByEmailAndPassword(String email,String password) {
        String sql = "SELECT user_id,user_name,email,password FROM users WHERE email=:email AND password=:password";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email",email).addValue("password",password);
        List<Users> usersList = template.query(sql,param,USERS_ROW_MAPPER);
        if(usersList.size() == 0) {
            return null;
        }
        return usersList.get(0);

    }
}
