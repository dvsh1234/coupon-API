package com.monkcommerce.coupon_api.Repository;

import com.monkcommerce.coupon_api.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}