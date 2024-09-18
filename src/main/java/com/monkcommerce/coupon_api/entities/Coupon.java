package com.monkcommerce.coupon_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponType type; // CART_WISE, PRODUCT_WISE, BXGY

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Embedded
    private List<CouponDetails> couponDetails;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date")
    private Date expirationDate;}



