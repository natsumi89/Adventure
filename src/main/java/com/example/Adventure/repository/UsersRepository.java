package com.example.Adventure.repository;

import com.example.Adventure.config.SecurityConfig;
import com.example.Adventure.domain.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsersRepository {
    private static final Logger logger = LoggerFactory.getLogger(UsersRepository.class);

    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private SecurityConfig securityConfig;

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
        logger.info("Inserting user with email: " + users.getEmail());
        SqlParameterSource param = new MapSqlParameterSource().addValue("lastName",users.getLastName())
                .addValue("firstName",users.getFirstName()).addValue("birthDate",users.getBirthDate())
        .addValue("email",users.getEmail()).addValue("password", users.getPassword());
        String sql = "INSERT into users(last_name,first_name,birth_date, email, password,role)" +
                "VALUES(:lastName,:firstName,:birthDate,:email,:password,'ROLE_USER')";
        template.update(sql,param);
        logger.info("User inserted successfully.");


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

    public Users findByEmailAndPassword(String email,String password) {
        String sql = "SELECT user_id, last_name,first_name,birth_date, email, password FROM users WHERE email=:email AND password=:password";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email",email).addValue("password",password);
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
