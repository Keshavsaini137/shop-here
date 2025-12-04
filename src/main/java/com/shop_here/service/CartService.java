package com.shop_here.service;


import com.shop_here.model.Cart;
import com.shop_here.model.CartItem;
import com.shop_here.model.Product;
import com.shop_here.model.User;
import com.shop_here.repository.CartItemRepository;
import com.shop_here.repository.CartRepository;
import com.shop_here.repository.ProductRepository;
import com.shop_here.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get or Create Cart
    private Cart getOrCreateCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().user(user).build()
                ));
    }

    // ✅ Add to Cart
    public String addToCart(String email, Long productId, int quantity) {
try{
        Cart cart = getOrCreateCart(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        cartItemRepository.save(item);
        return "Item added to cart";
        } catch(Exception e){
            System.out.println("EXCEPTION : " + e.getMessage() + Arrays.toString(e.getStackTrace()));
            return "Exception";
        }
    }

    // ✅ View My Cart
    public Cart viewMyCart(String email) {

        Cart cart = getOrCreateCart(email);
        return cart;
    }

    // ✅ Update Quantity
    public String updateQuantity(String email, Long productId, int quantity) {

        Cart cart = getOrCreateCart(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return "Quantity updated";
    }

    // ✅ Remove from Cart
    public String removeFromCart(String email, Long productId) {

        Cart cart = getOrCreateCart(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cartItemRepository.delete(item);
        return "Item removed from cart";
    }
}

