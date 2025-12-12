package com.shop_here.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long cartId;
    private String userEmail;
    private List<CartItemResponse> items;
    private double totalAmount;
}

