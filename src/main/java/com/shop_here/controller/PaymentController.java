package com.shop_here.controller;

import com.shop_here.model.Payment;
import com.shop_here.model.User;
import com.shop_here.repository.UserRepository;
import com.shop_here.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @PostMapping("/initiate/{orderId}")
    public ResponseEntity<Payment> initiate(
            @PathVariable Long orderId,
            Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(paymentService.initiatePayment(orderId, user));
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<Payment> confirm(
            @PathVariable Long orderId,
            @RequestParam boolean success) {

        return ResponseEntity.ok(paymentService.confirmPayment(orderId, success));
    }
}

