package com.codewithmosh.store.payments;

import java.util.Optional;

import com.codewithmosh.store.orders.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> handleWebhookRequest(WebHookRequest request);
}
