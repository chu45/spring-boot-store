package com.codewithmosh.store.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutSession {
    private String checkoutUrl;

}
