package com.monkcommerce.coupon_api.Controller;

import com.monkcommerce.coupon_api.Service.CartService;
import com.monkcommerce.coupon_api.Service.CouponService;
import com.monkcommerce.coupon_api.entities.Cart;
import com.monkcommerce.coupon_api.entities.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CouponService couponService;

    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    @GetMapping("/{id}")
    public Optional<Cart> getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable Long id, @RequestBody Cart cart) {
        return cartService.updateCart(id, cart);
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
    }

    @PostMapping("/{cartId}/apply-coupon/{couponId}")
    public Cart applyCoupon(@PathVariable Long cartId, @PathVariable Long couponId) {
        // Fetch cart by ID
        Cart cart = cartService.getCartById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

        // Apply coupon to the cart
        return cartService.applyCoupon(couponId, cart);
    }
}
