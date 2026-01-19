package com.shop_here.controller;

import com.shop_here.dto.WebhookPayload;
import com.shop_here.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<String> handlePaymentWebhook(
            @RequestBody WebhookPayload payload,
            @RequestHeader("X-Signature") String signature) {

        //  Verify signature (VERY IMPORTANT)
        if (!verifySignature(payload, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        // Update payment
        paymentService.processWebhook(payload);

        return ResponseEntity.ok("Webhook processed");
    }

    private boolean verifySignature(WebhookPayload payload, String signature) {
        // Dummy for now — real logic later
        return true;
    }
}

