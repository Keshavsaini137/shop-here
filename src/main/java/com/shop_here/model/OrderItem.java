package com.shop_here.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String productName;

    private double price;

    private int quantity;

    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}

