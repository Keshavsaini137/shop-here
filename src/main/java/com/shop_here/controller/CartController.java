package com.shop_here.controller;

import com.shop_here.model.Cart;
import com.shop_here.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication authentication) {

        System.out.println("Inside Add : " + productId + quantity + authentication.getName());
        String email = authentication.getName();
        return ResponseEntity.ok(cartService.addToCart(email, productId, quantity));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<Cart> viewMyCart(Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(cartService.viewMyCart(email));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<String> updateQuantity(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(cartService.updateQuantity(email, productId, quantity));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(
            @RequestParam Long productId,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(cartService.removeFromCart(email, productId));
    }
}

