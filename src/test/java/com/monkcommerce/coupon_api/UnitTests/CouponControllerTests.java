package com.monkcommerce.coupon_api.UnitTests;
import com.monkcommerce.coupon_api.Controller.CouponController;
import com.monkcommerce.coupon_api.entities.Coupon;
import com.monkcommerce.coupon_api.entities.CouponType;
import com.monkcommerce.coupon_api.Service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@WebMvcTest(CouponController.class)
public class CouponControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);

        when(couponService.createCoupon(any(Coupon.class))).thenReturn(coupon);

        mockMvc.perform(MockMvcRequestBuilders.post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\": \"CART_WISE\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("CART_WISE"));
    }

    @Test
    public void testGetCouponById() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);

        when(couponService.getCouponById(anyLong())).thenReturn(coupon);

        mockMvc.perform(MockMvcRequestBuilders.get("/coupons/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("CART_WISE"));
    }

    @Test
    public void testUpdateCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);

        when(couponService.updateCoupon(anyLong(), any(Coupon.class))).thenReturn(coupon);

        mockMvc.perform(MockMvcRequestBuilders.put("/coupons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\": \"PRODUCT_WISE\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("PRODUCT_WISE"));
    }

    @Test
    public void testDeleteCoupon() throws Exception {
        doNothing().when(couponService).deleteCoupon(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/coupons/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
