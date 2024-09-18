package com.monkcommerce.coupon_api.UnitTests;

import com.monkcommerce.coupon_api.entities.Coupon;
import com.monkcommerce.coupon_api.entities.CouponType;
import com.monkcommerce.coupon_api.Repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CouponApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CouponRepository couponRepository;

	@BeforeEach
	public void setUp() {
		couponRepository.deleteAll();
	}

	@Test
	void contextLoads() {
		// Context loads test
	}

	@Test
	public void testCreateCoupon() throws Exception {
		Coupon coupon = new Coupon();
		coupon.setId(1L);
		coupon.setType(CouponType.CART_WISE);

		when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

		mockMvc.perform(MockMvcRequestBuilders.post("/coupons")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"type\": \"CART_WISE\"}"))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("CART_WISE"));
	}

	@Test
	public void testGetAllCoupons() throws Exception {
		Coupon coupon = new Coupon();
		coupon.setId(1L);
		coupon.setType(CouponType.CART_WISE);

		couponRepository.save(coupon);

		mockMvc.perform(MockMvcRequestBuilders.get("/coupons"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("CART_WISE"));
	}

	@Test
	public void testGetCouponById() throws Exception {
		Coupon coupon = new Coupon();
		coupon.setId(1L);
		coupon.setType(CouponType.CART_WISE);

		couponRepository.save(coupon);

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

		couponRepository.save(coupon);

		mockMvc.perform(MockMvcRequestBuilders.put("/coupons/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"type\": \"PRODUCT_WISE\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("PRODUCT_WISE"));
	}

	@Test
	public void testDeleteCoupon() throws Exception {
		Coupon coupon = new Coupon();
		coupon.setId(1L);
		coupon.setType(CouponType.CART_WISE);

		couponRepository.save(coupon);

		mockMvc.perform(MockMvcRequestBuilders.delete("/coupons/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testCreateCouponBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/coupons")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"type\": \"INVALID_TYPE\"}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void testGetCouponNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/coupons/999"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testUpdateCouponNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/coupons/999")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"type\": \"PRODUCT_WISE\"}"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testDeleteCouponNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/coupons/999"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}
