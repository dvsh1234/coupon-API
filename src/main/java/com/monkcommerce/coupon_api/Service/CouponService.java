package com.monkcommerce.coupon_api.Service;

import com.monkcommerce.coupon_api.ExceptionHandler.CouponNotApplicableException;
import com.monkcommerce.coupon_api.ExceptionHandler.CouponNotFoundException;
import com.monkcommerce.coupon_api.Repository.CartRepository;
import com.monkcommerce.coupon_api.Repository.CouponRepository;
import com.monkcommerce.coupon_api.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartRepository cartRepository;

    // Create a new coupon
    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    // Retrieve all coupons
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    // Retrieve a specific coupon by its ID
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon with ID " + id + " not found"));
    }

    // Update a specific coupon by its ID
    @Transactional
    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        if (!couponRepository.existsById(id)) {
            throw new CouponNotFoundException("Coupon not found");
        }
        updatedCoupon.setId(id);
        return couponRepository.save(updatedCoupon);
    }

    // Delete a specific coupon by its ID
    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new CouponNotFoundException("Coupon not found");
        }
        couponRepository.deleteById(id);
    }

    // Fetch all applicable coupons for a given cart
    public List<ApplicableCoupon> getApplicableCoupons(Cart cart) {
        List<Coupon> allCoupons = couponRepository.findAll();
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();

        for (Coupon coupon : allCoupons) {
            if (isApplicable(coupon, cart)) {
                Double discount = calculateDiscount(coupon, cart);
                if (discount > 0) {
                    applicableCoupons.add(new ApplicableCoupon(coupon.getId(), coupon.getType(), discount));
                }
            }
        }
        return applicableCoupons;
    }

    // Apply a specific coupon to the cart and return the updated cart
    public Cart applyCoupon(Long couponId, Cart cart) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with ID: " + couponId));

        if (!isApplicable(coupon, cart)) {
            throw new CouponNotApplicableException("Coupon not applicable to this cart");
        }

        Double discount = calculateDiscount(coupon, cart);
        if (discount <= 0) {
            throw new CouponNotApplicableException("Coupon discount is zero and cannot be applied");
        }

        return applyDiscountToCart(cart, discount);
    }

    private boolean isApplicable(Coupon coupon, Cart cart) {
        CouponDetails couponDetails = (CouponDetails) coupon.getCouponDetails();
        CouponType couponType = coupon.getType();

        switch (couponType) {
            case CART_WISE:
                return cart.getTotalPrice() > couponDetails.getThreshold();
            case PRODUCT_WISE:
                return cart.getItems().stream()
                        .anyMatch(item -> item.getProduct().getId().equals(couponDetails.getProductId()));
            case BXGY:
                return isBxGyApplicable(coupon, cart);
            default:
                return false;
        }
    }

    private boolean isBxGyApplicable(Coupon coupon, Cart cart) {
        CouponDetails couponDetails = (CouponDetails) coupon.getCouponDetails();
        List<ProductQuantity> buyProducts = couponDetails.getBuyProducts();
        List<ProductQuantity> getProducts = couponDetails.getProducts();

        Map<Long, Integer> cartProductQuantities = cart.getItems().stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), CartItem::getQuantity));

        int totalBuyQuantity = buyProducts.stream()
                .mapToInt(buyProduct -> cartProductQuantities.getOrDefault(buyProduct.getProductId(), 0))
                .sum();

        int buyThreshold = buyProducts.stream().mapToInt(ProductQuantity::getQuantity).sum();
        return totalBuyQuantity >= buyThreshold;
    }

    private Double calculateDiscount(Coupon coupon, Cart cart) {
        CouponDetails couponDetails = (CouponDetails) coupon.getCouponDetails();

        switch (coupon.getType()) {
            case CART_WISE:
                return cart.getTotalPrice() * (couponDetails.getDiscount() / 100);
            case PRODUCT_WISE:
                return cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(couponDetails.getProductId()))
                        .mapToDouble(item -> item.getPrice() * item.getQuantity() * (couponDetails.getDiscount() / 100))
                        .sum();
            case BXGY:
                return calculateBxGyDiscount(coupon, cart);
            default:
                return 0.0;
        }
    }

    private Double calculateBxGyDiscount(Coupon coupon, Cart cart) {
        CouponDetails details = (CouponDetails) coupon.getCouponDetails();
        List<ProductQuantity> buyProducts = details.getBuyProducts();
        List<ProductQuantity> getProducts = details.getProducts();
        int repetitionLimit = details.getRepetitionLimit();

        Map<Long, Integer> cartProductQuantities = cart.getItems().stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), CartItem::getQuantity));

        int totalBuyQuantity = buyProducts.stream()
                .mapToInt(buyProduct -> cartProductQuantities.getOrDefault(buyProduct.getProductId(), 0))
                .sum();

        int buyThreshold = buyProducts.stream().mapToInt(ProductQuantity::getQuantity).sum();
        int applicableRepetitions = totalBuyQuantity / buyThreshold;
        int limitedRepetitions = Math.min(applicableRepetitions, repetitionLimit);

        // Calculate the total discount based on the 'get' products
        double totalDiscount = 0.0;
        for (ProductQuantity getProduct : getProducts) {
            Long productId = getProduct.getProductId();
            int getQuantity = getProduct.getQuantity();
            double productPrice = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .map(CartItem::getPrice)
                    .orElse(0.0);

            int totalGetQuantity = limitedRepetitions * getQuantity;
            totalDiscount += totalGetQuantity * productPrice;
        }

        return totalDiscount;
    }

    private Cart applyDiscountToCart(Cart cart, Double discount) {
        Double totalAmount = cart.getTotalPrice() - discount;
        cart.setTotalPrice(totalAmount);
        return cart;
    }

    // Inner class to represent an applicable coupon
    public static class ApplicableCoupon {
        private final CouponType couponType;
        private Long couponId;
        private Double discount;

        public ApplicableCoupon(Long couponId, CouponType type, Double discount) {
            this.couponId = couponId;
            this.couponType = type;
            this.discount = discount;
        }

        // Getters
        public Long getCouponId() {
            return couponId;
        }

        public CouponType getCouponType() {
            return couponType;
        }

        public Double getDiscount() {
            return discount;
        }
    }
}
