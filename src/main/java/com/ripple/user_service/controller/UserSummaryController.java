package com.ripple.user_service.controller;

import com.ripple.user_service.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserSummaryController {

    private static final String ORDER_SERVICE_URL = "http://localhost:8082";

    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> getUserSummary(@PathVariable String id) {
        RestTemplate restTemplate = new RestTemplate();

        // Fetch user
        User user = restTemplate.getForObject(
            "http://localhost:8081/users/" + id, User.class
        );

        // Directly call order-service — ARCHITECTURE VIOLATION:
        // user-service should not depend on order-service
        List<?> orders = restTemplate.getForObject(
            ORDER_SERVICE_URL + "/orders/user/" + id, List.class
        );

        Map<String, Object> summary = new HashMap<>();
        summary.put("user", user);
        summary.put("orderCount", orders != null ? orders.size() : 0);
        summary.put("totalOrdersFromOrderService", orders);

        return ResponseEntity.ok(summary);
    }
}
