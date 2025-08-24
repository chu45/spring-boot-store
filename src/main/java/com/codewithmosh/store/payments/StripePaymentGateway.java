package com.codewithmosh.store.payments;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.orders.Order;
import com.codewithmosh.store.orders.OrderItem;
import com.codewithmosh.store.orders.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
       try {
        var builder = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
        .setCancelUrl(websiteUrl + "/checkout-cancel")
        .putMetadata("order_id", order.getId().toString());
        order.getItems().forEach(item -> {
           var lineItem = createLineItem(item);
           builder.addLineItem(lineItem);
        });
        var session = Session.create(builder.build());
        return new CheckoutSession(session.getUrl());
       } catch (StripeException e) {
       System.out.println(e.getMessage());
       throw new PaymentException();    
       }
    }

    private LineItem createLineItem(OrderItem item) {
        var lineItem = SessionCreateParams.LineItem.builder()
            .setQuantity(Long.valueOf(item.getQuantity()))
            .setPriceData(createPriceData(item))
            .build();
        return lineItem;
    }

    private PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
        .setCurrency("usd")
        .setUnitAmountDecimal(item.getPrice().multiply(BigDecimal.valueOf(100)))
        .setProductData(createProductData(item))
        .build();
    }

    private ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .setName(item.getProduct().getName())
        .build();
    }

    @Override
    public Optional<PaymentResult> handleWebhookRequest(WebHookRequest request) {
        try {
            var payload = request.getPayload();
            // Handle case-insensitive header lookup for Stripe-Signature
            var signature = request.getHeaders().get("stripe-signature");
            if (signature == null) {
                signature = request.getHeaders().get("Stripe-Signature");
            }
            var event = Webhook.constructEvent(payload, signature, webhookSecret);
            switch (event.getType()) {
             case "payment_intent.succeeded" -> {
                 var orderId = extractOrderId(event);
                 return Optional.of(new PaymentResult(orderId, OrderStatus.PAID));
             }
             case "payment_intent.payment_failed" -> {
                var orderId = extractOrderId(event);
                return Optional.of(new PaymentResult(orderId, OrderStatus.FAILED));
             }
             default -> {
                return Optional.empty();
             }
            }
 
         } catch (SignatureVerificationException e) {
             throw new PaymentException("Invalid signature");
         }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
            () -> new PaymentException("could not extract stripe object from event, check sdk and api version")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        var orderId = paymentIntent.getMetadata().get("order_id");
        return Long.valueOf(orderId);
    }
}
