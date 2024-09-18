package com.monkcommerce.coupon_api.Controller;


import com.monkcommerce.coupon_api.Service.CouponService;
import com.monkcommerce.coupon_api.entities.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return couponService.createCoupon(coupon);
    }

    @GetMapping
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/{id}")
    public Optional<Coupon> getCouponById(@PathVariable Long id) {
        return Optional.ofNullable(couponService.getCouponById(id));
    }

    @PutMapping("/{id}")
    public Coupon updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
        return couponService.updateCoupon(id, coupon);
    }

    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
    }
}
