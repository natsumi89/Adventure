package com.example.Adventure.service;

import com.example.Adventure.domain.Users;
import com.example.Adventure.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class UsersService implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);


    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Trying to load user by email: " + email);
        Users user = findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが存在しません。");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public void insert(Users users) {
//        users.setPassword(passwordEncoder.encode(users.getPassword()));
        usersRepository.insert(users);

    }
    public void someMethod() {
        logger.info("This is an info message");
        logger.error("This is an error message");
    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
    public void delete(Integer userId) {
        usersRepository.delete(userId);
    }

}
