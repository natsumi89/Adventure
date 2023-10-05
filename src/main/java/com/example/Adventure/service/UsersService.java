package com.example.Adventure.service;

import com.example.Adventure.domain.Users;
import com.example.Adventure.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public void insert(Users users) {
        usersRepository.insert(users);
    }

    public Users findByEmailAndPassword(String email,String password) {
        return usersRepository.findByEmailAndPassword(email,password);
    }
    public void delete(Integer userId) {
        usersRepository.delete(userId);
    }

}
