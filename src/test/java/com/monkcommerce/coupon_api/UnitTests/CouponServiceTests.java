package com.monkcommerce.coupon_api.UnitTests;

import com.monkcommerce.coupon_api.Repository.CouponRepository;
import com.monkcommerce.coupon_api.Service.CouponService;
import com.monkcommerce.coupon_api.entities.Cart;
import com.monkcommerce.coupon_api.entities.Coupon;
import com.monkcommerce.coupon_api.entities.CouponType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class CouponServiceTests {

    @Autowired
    private CouponService couponService;

    @MockBean
    private CouponRepository couponRepository;

    @Test
    public void testApplyCoupon() {
        // Arrange
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);
        coupon.setExpirationDate(Date.valueOf(LocalDate.now().plusDays(1))); // Not expired

        // Mock repository
        Mockito.when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        Cart cart = new Cart();
        cart.setTotalPrice(200.0);

        // Act
        Cart updatedCart = couponService.applyCoupon(1L, cart);

        // Assert
        Assertions.assertNotNull(updatedCart);
        Assertions.assertEquals(180.0, updatedCart.getTotalPrice()); // Assuming 10% discount
    }
}

