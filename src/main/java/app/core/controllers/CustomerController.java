package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.JWT.JwtUtil;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.services.CustomerService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private JwtUtil jwtUtil;

	public CustomerController() {
		super();
	}

	private CustomerService getService(String token) {
		customerService.setCustomerID(jwtUtil.extractClientId(token));
		return customerService;
	}

	@PostMapping("/coupons")
	public Coupon purchaseCoupon(@RequestBody Coupon coupon, @RequestHeader String token) {
		try {
			return getService(token).purchaseCoupon(coupon);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase coupon - failed - " + e.getMessage());
		}

	}

	@GetMapping("/coupons")
	public List<Coupon> getCustomerCoupons(@RequestHeader String token) {
		try {
			return getService(token).getCustomerCoupons();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting all coupons - failed - " + e.getMessage());
		}

	}

	@GetMapping("/coupons/category/{category}")
	public List<Coupon> getCustomerCoupons(@PathVariable Category category, @RequestHeader String token) {
		try {
			return getService(token).getCustomerCoupons(category);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting coupons by category - failed - " + e.getMessage());
		}
	}

	@GetMapping("/coupons/price/{maxPrice}")
	public List<Coupon> getCustomerCoupons(@PathVariable double maxPrice, @RequestHeader String token) {
		try {
			return getService(token).getCustomerCoupons(maxPrice);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting coupons by price - failed - " + e.getMessage());
		}
	}

	@GetMapping()
	public Customer getCustomerDetails(@RequestHeader String token) {
		try {
			return getService(token).getCustomerDetails();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting company details - failed - " + e.getMessage());
		}
	}

	@GetMapping("/coupons-for-purchase")
	public List<Coupon> getCouponsForPurchase(@RequestHeader String token) {
		try {
			return getService(token).couponsForPurchaseByCustomer();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting coupons - failed - " + e.getMessage());
		}
	}

}
