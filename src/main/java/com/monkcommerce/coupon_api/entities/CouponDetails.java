package com.monkcommerce.coupon_api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CouponDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double discount;
    private Double threshold;
    private Long productId;

    @ElementCollection
    @CollectionTable(name = "coupon_buy_products", joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "product_id")
    private List<ProductQuantity> buyProducts;

    @ElementCollection
    @CollectionTable(name = "coupon_products", joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "product_id")
    private List<ProductQuantity> products;  // Renamed from 'getProducts' to 'products'

    private Integer repetitionLimit;
}
