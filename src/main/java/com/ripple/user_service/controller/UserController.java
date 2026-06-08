package com.ripple.user_service.controller;

import com.ripple.user_service.dto.AddRewardsRequest;
import com.ripple.user_service.dto.CreateUserRequest;
import com.ripple.user_service.dto.UpdateUserRequest;
import com.ripple.user_service.dto.WalletTopUpRequest;
import com.ripple.user_service.entity.User;
import com.ripple.user_service.repository.UserRepository;
import com.ripple.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;
    private final UserRepository repository;

    public UserController(UserService service, UserRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = service.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("count", users.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id).orElse(null));
    }

    @GetMapping("/tier/{tier}")
    public ResponseEntity<List<User>> getUsersByTier(@PathVariable String tier) {
        return ResponseEntity.ok(service.getUsersByTier(tier));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(service.updateUser(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rewards")
    public ResponseEntity<User> addRewards(@PathVariable String id, @RequestBody AddRewardsRequest req) {
        User user = repository.findById(id).orElseThrow();
        int current = user.getRewardPoints() != null ? user.getRewardPoints() : 0;
        user.setRewardPoints(current + req.getPoints());
        return ResponseEntity.ok(repository.save(user));
    }

    @PostMapping("/{id}/wallet/topup")
    public ResponseEntity<User> topUpWallet(@PathVariable String id, @RequestBody WalletTopUpRequest req) {
        return ResponseEntity.ok(service.topUpWallet(id, req));
    }
}
