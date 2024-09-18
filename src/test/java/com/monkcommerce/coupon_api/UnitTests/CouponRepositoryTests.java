package com.monkcommerce.coupon_api.UnitTests;
import com.monkcommerce.coupon_api.entities.Coupon;
import com.monkcommerce.coupon_api.entities.CouponType;
import com.monkcommerce.coupon_api.Repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CouponRepositoryTests {

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    public void setUp() {
        couponRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindCoupon() {
        Coupon coupon = new Coupon();
        coupon.setType(CouponType.CART_WISE);
        coupon = couponRepository.save(coupon);

        Optional<Coupon> foundCoupon = couponRepository.findById(coupon.getId());
        assertTrue(foundCoupon.isPresent());
        assertEquals(CouponType.CART_WISE, foundCoupon.get().getType());
    }

    @Test
    public void testDeleteCoupon() {
        Coupon coupon = new Coupon();
        coupon.setType(CouponType.CART_WISE);
        coupon = couponRepository.save(coupon);

        couponRepository.deleteById(coupon.getId());

        Optional<Coupon> foundCoupon = couponRepository.findById(coupon.getId());
        assertFalse(foundCoupon.isPresent());
    }
}

