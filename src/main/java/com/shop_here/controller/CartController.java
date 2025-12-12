package com.shop_here.controller;

import com.shop_here.dto.CartItemResponse;
import com.shop_here.dto.CartResponse;
import com.shop_here.model.Cart;
import com.shop_here.model.CartItem;
import com.shop_here.model.User;
import com.shop_here.repository.UserRepository;
import com.shop_here.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<?> viewMyCart(Authentication authentication) {

        String email = authentication.getName();
        Cart cart = cartService.viewMyCart(email);

        List<CartItemResponse> items = new ArrayList<>();
        double totalCartPrice=0;

        for (int i = 0; i < cart.getItems().size(); i++) {
            CartItem cartItem = cart.getItems().get(i);

            double subTotal = cartItem.getSubTotal();
            totalCartPrice += subTotal;

            items.add(new CartItemResponse(cartItem.getProduct().getId(),
                    cartItem.getProduct().getName(),cartItem.getProduct().getPrice(),
                    cartItem.getQuantity(),subTotal));
        }

        CartResponse cartResp =  new CartResponse(cart.getId(),
                cart.getUser().getEmail(),items,totalCartPrice);
        return ResponseEntity.ok(cartResp);
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

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartService.clearCart(user.getId());

        return ResponseEntity.ok("Cart cleared successfully.");
    }

}

