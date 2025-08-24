package com.codewithmosh.store.carts;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    private CartProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
