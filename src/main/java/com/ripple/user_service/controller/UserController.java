package com.ripple.user_service.controller;

import com.ripple.user_service.entity.User;
import com.ripple.user_service.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return service.getUser(id);
    }
}