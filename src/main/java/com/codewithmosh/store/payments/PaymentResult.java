package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResult {
    private Long orderId;
    private OrderStatus paymentStatus;


}
