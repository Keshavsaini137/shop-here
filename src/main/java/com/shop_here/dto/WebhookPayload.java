package com.shop_here.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookPayload {
    private Long orderId;
    private String paymentStatus; // SUCCESS / FAILED
    private String paymentId;
}

