package com.codewithmosh.store.payments;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.carts.CartEmptyException;
import com.codewithmosh.store.carts.CartNotFoundException;
import com.codewithmosh.store.carts.CartRepository;
import com.codewithmosh.store.carts.CartService;
import com.codewithmosh.store.orders.Order;
import com.codewithmosh.store.orders.OrderRepository;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;
    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        if (cart.getCartItems().isEmpty()) {
            throw new CartEmptyException();
        }
        var order = Order.createOrderFromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);
        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(request.getCartId());
            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw new PaymentException();
        }
    }
    public void handleWebhook(WebHookRequest request) {
        var result = paymentGateway.handleWebhookRequest(request);
        result.ifPresent(paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        });
    }

}
