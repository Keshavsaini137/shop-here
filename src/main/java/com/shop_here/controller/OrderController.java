package com.shop_here.controller;

import com.shop_here.model.Order;
import com.shop_here.model.User;
import com.shop_here.repository.OrderRepository;
import com.shop_here.repository.UserRepository;
import com.shop_here.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> checkout(Authentication auth) {

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.placeOrder(user.getId());

        return ResponseEntity.ok(order);
    }
}

