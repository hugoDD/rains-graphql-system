package com.rains.system.service;

import com.rains.system.domain.User;
import com.rains.system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private AccountRepository repository;
    public User findByAccount(String userName) {
        return repository.findByAccount(userName);
    }
}
