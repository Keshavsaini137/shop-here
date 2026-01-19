package com.shop_here.service;

import com.shop_here.dto.WebhookPayload;
import com.shop_here.model.Order;
import com.shop_here.model.Payment;
import com.shop_here.model.User;
import com.shop_here.repository.OrderRepository;
import com.shop_here.repository.PaymentRepository;
import com.shop_here.utils.enums.OrderStatus;
import com.shop_here.utils.enums.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment initiatePayment(Long orderId, User user) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new RuntimeException("Invalid order state");
        }

        return paymentRepository.save(
                Payment.builder()
                        .order(order)
                        .amount(order.getTotalAmount())
                        .status(PaymentStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    public Payment confirmPayment(Long orderId, boolean success) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        return paymentRepository.save(payment);
    }

    @Transactional
    public void processWebhook(WebhookPayload payload) {

        Order order = orderRepository.findById(payload.getOrderId())
                .orElseThrow();

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow();

        if ("SUCCESS".equals(payload.getPaymentStatus())) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PLACED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
        }

        paymentRepository.save(payment);
        orderRepository.save(order);
    }

}

