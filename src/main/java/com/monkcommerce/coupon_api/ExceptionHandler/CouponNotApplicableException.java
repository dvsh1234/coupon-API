package com.monkcommerce.coupon_api.ExceptionHandler;

public class CouponNotApplicableException extends RuntimeException {
    public CouponNotApplicableException(String message) {
        super(message);
    }
}

