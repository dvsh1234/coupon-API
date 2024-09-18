package com.monkcommerce.coupon_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
@Entity
@Data
@Embeddable
public class ProductQuantity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull
    private Integer quantity;

    // Default constructor
    public ProductQuantity() {}

    // Parameterized constructor
    public ProductQuantity(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }}

