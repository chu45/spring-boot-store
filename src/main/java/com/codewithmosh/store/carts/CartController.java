package com.codewithmosh.store.carts;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.products.ProductNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cartDto = cartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add an item to a cart")
    public ResponseEntity<CartItemDto> addItemToCart(@Parameter(description = "The ID of the cart") @PathVariable UUID cartId, @RequestBody AddItemToCartRequest addItemToCartRequest) {
        var cartItemDto = cartService.addItemToCart(cartId, addItemToCartRequest.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        var cartDto = cartService.getCart(cartId);
        return cartDto;
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCartItem(@PathVariable UUID cartId, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, updateCartItemRequest.getQuantity());
        return cartItemDto;
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFoundException(CartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in cart"));
    }
}
