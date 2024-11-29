package com.arkam.microservices.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(
        Long id,
//        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity,
        Long userId,
        @Valid UserDetails userDetails) {

    public record UserDetails(@Email String email, @NotNull String firstName,@NotNull String lastName) {}
}
