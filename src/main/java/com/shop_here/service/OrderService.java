package com.shop_here.service;

import com.shop_here.model.*;
import com.shop_here.repository.CartRepository;
import com.shop_here.repository.OrderRepository;
import com.shop_here.repository.ProductRepository;
import com.shop_here.utils.enums.OrderStatus;
import com.shop_here.utils.exceptions.AccessDeniedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }


    @Transactional
    public Order placeOrder(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setCreatedAt(new Date());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(cart.getTotalAmount());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            // ↓↓↓ Reduce product stock
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Out of stock: " + product.getName());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // ↓↓↓ Create order item
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setSubtotal(product.getPrice() * cartItem.getQuantity());
            item.setOrder(order);

            orderItems.add(item);
        }

        order.setItems(orderItems);

        // Save order + items
        orderRepository.save(order);

        // Clear cart after order
        cart.getItems().clear();
        cart.setTotalAmount(0);
        cartRepository.save(cart);

        return order;
    }

    public Order getOrderById(Long orderId, User user) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ADMIN can access any order
        if (user.getRole().equals("ROLE_ADMIN")) {
            return order;
        }

        // USER can access only their orders
        if (!order.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        return order;
    }


    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

}

