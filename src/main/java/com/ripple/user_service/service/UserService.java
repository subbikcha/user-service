package com.ripple.user_service.service;


import com.ripple.user_service.entity.User;
import com.ripple.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(String id) {
        return repository.findById(id).orElse(null);
    }
}