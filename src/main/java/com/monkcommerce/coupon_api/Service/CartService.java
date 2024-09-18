package com.monkcommerce.coupon_api.Service;

import com.monkcommerce.coupon_api.ExceptionHandler.CouponNotFoundException;
import com.monkcommerce.coupon_api.Repository.CouponRepository;
import com.monkcommerce.coupon_api.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CouponRepository couponRepository;

    private Map<Long, Cart> cartStore = new HashMap<>();
    ;
    private Long currentId = 1L;

    public Cart createCart(Cart cart) {
        cart.setId(currentId++);
        cartStore.put(cart.getId(), cart);
        return cart;
    }

    public Optional<Cart> getCartById(Long id) {
        return Optional.ofNullable(cartStore.get(id));
    }

    public Cart updateCart(Long id, Cart cart) {
        if (!cartStore.containsKey(id)) {
            throw new RuntimeException("Cart not found");
        }
        cart.setId(id);
        cartStore.put(id, cart);
        return cart;
    }

    public void deleteCart(Long id) {
        if (!cartStore.containsKey(id)) {
            throw new RuntimeException("Cart not found");
        }
        cartStore.remove(id);
    }

    public Cart applyCoupon(Long couponId, Cart cart) {
        // Fetch coupon details from the repository
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        // Apply coupon logic based on type
        switch (coupon.getType()) {
            case CART_WISE:
                return applyCartWiseCoupon(coupon, cart);
            case PRODUCT_WISE:
                return applyProductWiseCoupon(coupon, cart);
            case BXGY:
                return applyBxGyCoupon(coupon, cart);
            default:
                throw new IllegalArgumentException("Invalid coupon type");
        }
    }

    private Cart applyCartWiseCoupon(Coupon coupon, Cart cart) {
        // Get coupon details
        CouponDetails details = getCouponDetails(coupon);

        // Check if cart total exceeds the threshold
        if (cart.getTotalPrice() > details.getThreshold()) {
            double discount = cart.getTotalPrice() * (details.getDiscount() / 100.0);
            cart.setTotalDiscount(discount);
            cart.setTotalPrice(cart.getTotalPrice() - discount);
        }
        return cart;
    }

    private Cart applyProductWiseCoupon(Coupon coupon, Cart cart) {
        // Get coupon details
        CouponDetails details = getCouponDetails(coupon);
        long productId = details.getProductId();
        double discount = details.getDiscount();

        // Apply discount to the specific product in the cart
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                double itemDiscount = item.getPrice() * item.getQuantity() * (discount / 100.0);
                cart.setTotalDiscount(cart.getTotalDiscount() + itemDiscount);
                cart.setTotalPrice(cart.getTotalPrice() - itemDiscount);
            }
        }
        return cart;
    }

    private Cart applyBxGyCoupon(Coupon coupon, Cart cart) {
        // Get coupon details
        CouponDetails details = getCouponDetails(coupon);
        List<ProductQuantity> buyProducts = details.getBuyProducts();
        List<ProductQuantity> getProducts = details.getProducts();  // Renamed from 'getProducts' to 'products'
        int repetitionLimit = details.getRepetitionLimit();

        // Count the quantity of buy products in the cart
        Map<Long, Integer> buyProductCount = new HashMap<>();
        for (ProductQuantity pq : buyProducts) {
            buyProductCount.put(pq.getProductId(), pq.getQuantity());
        }

        // Check the quantities in the cart
        Map<Long, Integer> cartProductCount = new HashMap<>();
        for (CartItem item : cart.getItems()) {
            cartProductCount.put(item.getProduct().getId(), cartProductCount.getOrDefault(item.getProduct().getId(), 0) + item.getQuantity());
        }

        // Calculate the number of times the coupon can be applied
        int applicableTimes = Integer.MAX_VALUE;
        for (Map.Entry<Long, Integer> entry : buyProductCount.entrySet()) {
            Long productId = entry.getKey();
            Integer requiredQuantity = entry.getValue();
            Integer availableQuantity = cartProductCount.getOrDefault(productId, 0);
            applicableTimes = Math.min(applicableTimes, availableQuantity / requiredQuantity);
        }
        applicableTimes = Math.min(applicableTimes, repetitionLimit);

        // Apply the BxGy coupon
        double totalDiscount = 0.0;
        for (ProductQuantity pq : getProducts) {
            Long getProductId = pq.getProductId();
            Integer getQuantity = pq.getQuantity();

            // Apply free products
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(getProductId)) {
                    int freeQuantity = Math.min(getQuantity, applicableTimes);
                    totalDiscount += freeQuantity * item.getPrice();
                    item.setQuantity(item.getQuantity() + freeQuantity);
                    break;
                }
            }
        }
        cart.setTotalDiscount(cart.getTotalDiscount() + totalDiscount);
        cart.setTotalPrice(cart.getTotalPrice() - totalDiscount);

        return cart;
    }

    private CouponDetails getCouponDetails(Coupon coupon) {
        // Ensure that coupon details are of type CouponDetails
        if (coupon.getCouponDetails() instanceof CouponDetails) {
            return (CouponDetails) coupon.getCouponDetails();
        } else {
            throw new ClassCastException("Coupon details are not of type CouponDetails");
        }
    }
}
