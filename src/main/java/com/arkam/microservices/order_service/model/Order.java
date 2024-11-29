package com.arkam.microservices.order_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SKU Code is mandatory")
    @Size(max = 50, message = "SKU Code cannot exceed 50 characters")
    private String skuCode;

    @NotNull(message = "Price is mandatory")
//    @Digits(integer = 1, fraction = 2, message = "Price must be a valid monetary amount with up to 8 digits and 2 decimal places")
    @PositiveOrZero(message = "Price must be zero or a positive value")
    private BigDecimal price;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

}