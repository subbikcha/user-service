package com.ripple.user_service.service;

import com.ripple.user_service.dto.AddRewardsRequest;
import com.ripple.user_service.dto.CreateUserRequest;
import com.ripple.user_service.dto.UpdateUserRequest;
import com.ripple.user_service.dto.WalletTopUpRequest;
import com.ripple.user_service.entity.User;
import com.ripple.user_service.exception.UserNotFoundException;
import com.ripple.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(String userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> getAllUsers() {
        return repository.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                .toList();
    }

    public List<User> getUsersByTier(String tier) {
        return repository.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()) && tier.equals(u.getTier()))
                .toList();
    }

    public User createUser(CreateUserRequest req) {
        User user = User.builder()
                .userId(UUID.randomUUID().toString().substring(0, 8))
                .userName(req.getUserName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .address(req.getAddress())
                .tier(req.getTier() != null ? req.getTier() : "new")
                .rewardPoints(req.getRewardPoints() != null ? req.getRewardPoints() : 0)
                .walletBalance(req.getWalletBalance() != null ? req.getWalletBalance() : 0.0)
                .isActive(true)
                .build();
        return repository.save(user);
    }

    public User updateUser(String userId, UpdateUserRequest req) {
        User user = getUser(userId);
        if (req.getUserName() != null) user.setUserName(req.getUserName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhoneNumber() != null) user.setPhoneNumber(req.getPhoneNumber());
        if (req.getAddress() != null) user.setAddress(req.getAddress());
        if (req.getTier() != null) user.setTier(req.getTier());
        return repository.save(user);
    }

    public void deleteUser(String userId) {
        repository.deleteById(userId);
    }

    public User addRewardPoints(String userId, AddRewardsRequest req) {
        if (req.getPoints() == null) {
            throw new IllegalArgumentException("points must not be null");
        }
        User user = getUser(userId);
        int current = user.getRewardPoints() != null ? user.getRewardPoints() : 0;
        user.setRewardPoints(current + req.getPoints());
        return repository.save(user);
    }

    public User topUpWallet(String userId, WalletTopUpRequest req) {
        User user = getUser(userId);
        double current = user.getWalletBalance() != null ? user.getWalletBalance() : 0.0;
        user.setWalletBalance(current + req.getAmount());
        return repository.save(user);
    }
}
