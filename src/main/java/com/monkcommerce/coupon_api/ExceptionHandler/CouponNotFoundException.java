package com.monkcommerce.coupon_api.ExceptionHandler;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}

