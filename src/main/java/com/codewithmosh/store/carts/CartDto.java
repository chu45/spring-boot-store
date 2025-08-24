package com.codewithmosh.store.carts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private List<CartItemDto> cartItems = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
