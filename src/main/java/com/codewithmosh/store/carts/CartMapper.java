package com.codewithmosh.store.carts;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
    CartItemDto toDto(CartItem cartItem);
}
